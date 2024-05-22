import sys
import subprocess
import time
import signal
import mysql.connector
from mysql.connector import pooling
from collections import defaultdict
import datetime 
import keyring
from keyrings.alt.file import PlaintextKeyring
import logging

# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# Set the keyring backend explicitly
keyring.set_keyring(PlaintextKeyring())

# Define your service and username
service_id = "database_service"
username = "db_user"

try:
    # Retrieve the password from the keyring
    password = keyring.get_password(service_id, username)
    if not password:
        raise ValueError("Password not found in keyring.")
    logger.info("Password retrieved successfully from keyring.")
except Exception as e:
    logger.error(f"Error retrieving password from keyring: {e}")
    raise

DEVELOPER_MODE = False  # Toggle developer mode for testing
CHECK_INTERVAL = 30  # Interval in seconds between each check
DATABASE_CONFIG = {
    "host": "localhost",
    "user": username,
    "password": password,
    "database": "intern",
    "pool_name": "my_pool",
    "pool_size": 10
}

ip_list = []  # List to track IP addresses managed by the script
# Dictionary to track active timeslots for IPs
active_timeslots = {}
# Signal handler for graceful shutdown
def signal_handler(sig, frame):
    for ip in ip_list:
        delete_commands = [
            ["sudo", "iptables", "-D", "INPUT", "-s", ip, "-j", "ACCEPT"],
            ["sudo", "iptables", "-D", "OUTPUT", "-s", ip, "-j", "ACCEPT"],
            ["sudo", "iptables", "-D", "FORWARD", "-s", ip, "-j", "ACCEPT"],
            ["sudo", "iptables", "-D", "INPUT", "-s", ip, "-j", "DROP"],
            ["sudo", "iptables", "-D", "FORWARD", "-s", ip, "-j", "DROP"],
            ["sudo", "iptables", "-D", "OUTPUT", "-s", ip, "-j", "DROP"]        
        ]
        for cmd in delete_commands:
            try:
                subprocess.run(cmd, check=True)
            except subprocess.CalledProcessError:
                continue  # Ignore errors for non-existent rules
    print('Stopping gracefully...')
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)
signal.signal(signal.SIGTERM, signal_handler)

# Database connection pool setup
def create_pool(config):
    try:
        pool = pooling.MySQLConnectionPool(
            pool_name=config["pool_name"],
            pool_size=config["pool_size"],
            pool_reset_session=True,
            host=config["host"],
            user=config["user"],
            password=config["password"],
            database=config["database"]
        )
        return pool
    except mysql.connector.Error as e:
        print(f"Error creating MySQL connection pool: {e}")
        raise e

pool = create_pool(DATABASE_CONFIG)

# Fetch reservation details from the database
def fetch_reservation_details(pool):
    try:
        connection = pool.get_connection()
        cursor = connection.cursor()
        query = """
        SELECT virtual_machines.connection_address AS ip_address,
               reservations.start_time AS start_time,
               reservations.end_time AS end_time
        FROM reservations
        JOIN virtual_machines ON reservations.virtual_machine_id = virtual_machines.vm_id
        """
        cursor.execute(query)
        result = cursor.fetchall()
        reservations = []
        for ip, start, end in result:
            reservations.append({'ip_address': ip, 'start_time': start, 'end_time': end})
        return reservations
    finally:
        cursor.close()
        connection.close()

# Fetch all IP addresses from the virtual_machines table
def fetch_all_ips(pool):
    try:
        connection = pool.get_connection()
        cursor = connection.cursor()
        query = "SELECT connection_address FROM virtual_machines"
        cursor.execute(query)
        result = cursor.fetchall()
        all_ips = [ip[0] for ip in result]
        return all_ips
    finally:
        cursor.close()
        connection.close()

# Block all IP addresses using iptables
def block_all_ips(pool):
    all_ips = fetch_all_ips(pool)
    for ip in all_ips:
        ip_list.append(ip)  # Add IP to the list of managed IPs
        print(ip)
        manage_firewall("DROP", ip)

def GetTime():
    global currtime
    time = datetime.datetime.now()
    currtime = time.strftime('%H:%M:%S')
    return currtime


# Parsing time-only strings
def parse_time(time_str):
    if isinstance(time_str, str):
        return datetime.strptime(time_str, "%H:%M:%S")  # Adjusted to correct format conversion
    return time_str


def merge_intervals(reservations):
    sorted_reservations = sorted(reservations, key=lambda x: x['start_time'])
    merged = []
    current_start, current_end = None, None

    for res in sorted_reservations:
        start = res['start_time']
        end = res['end_time']
        if current_end is None or start < current_end:
            if current_end is not None and start >= current_start:
                merged.append((current_start, current_end))
            current_start, current_end = start, end
        else:
            current_end = max(current_end, end)

    if current_end is not None:
        merged.append((current_start, current_end))
    return merged


def apply_firewall_rules(merged_intervals, ip_address):
    """
    Apply firewall rules based on merged reservation intervals.
    """
    # Get the current time as a datetime.time object
    current_time = GetTime()
    # Format the current time to 'HH:MM' format
    #formatted_time = datetime.strftime(current_time, "%H:%M:%S") if isinstance(current_time, str) else current_time
  
    # Print the formatted time
    
    print("end", current_time)
    #Removes any expired timeslots
    for ip in list(active_timeslots.keys()):
        print(current_time)
        print("active_timeslots[ip]['end']")
        if current_time >= active_timeslots[ip]['end']:
            print(f"Timeslot ended for IP {ip}, checking for new bookings.")
            manage_firewall("DROP", ip)
            del active_timeslots[ip]

    for start, end in merged_intervals:
        # Check if the IP is currently in an active timeslot
        start = str(start)
        end = str(end)
        if ip_address in active_timeslots:
            continue  # Skip processing if within an active timeslot
        else:
            print(current_time)
            print("start", start)
            if start <= current_time < end:
                # Set the IP as active and store the end time
                active_timeslots[ip_address] = {'start': start, 'end': end}
                manage_firewall("ACCEPT", ip_address)
                print(f"ACCEPT rule applied for {ip_address} from {start} to {end}")
            else:
                manage_firewall("DROP", ip_address)
                print(f"DROP rule applied for {ip_address}")

def manage_firewall(action, ip):
    """
    Add or remove firewall rules for a given IP based on the specified action.
    """
    ip_list.append(ip) if ip not in ip_list else None
    delete_commands = [
        ["sudo", "iptables", "-D", "INPUT", "-s", ip, "-j", "ACCEPT"],
        ["sudo", "iptables", "-D", "OUTPUT", "-s", ip, "-j", "ACCEPT"],
     
        ["sudo", "iptables", "-D", "INPUT", "-s", ip, "-j", "DROP"],

        ["sudo", "iptables", "-D", "OUTPUT", "-s", ip, "-j", "DROP"]
    ]
    for cmd in delete_commands:
        subprocess.run(cmd, stderr=subprocess.DEVNULL)  # Ignore errors for non-existent rules
    print("action", action)
    command_input = ["sudo", "iptables", "-A", "INPUT", "-s", ip, "-j", action]
    command_output = ["sudo", "iptables", "-A", "OUTPUT", "-s", ip, "-j", action]
    subprocess.run(command_input, check=True)

    subprocess.run(command_output, check=True)
    subprocess.run(["sudo", "netfilter-persistent", "save"], check=True)

# Main program logic
def run_continuously():
    while True:
        cycle_start_time = time.time()  # Record start time of the cycle
        try:
            
            reservation_details = fetch_reservation_details(pool)
            reservations_by_ip = defaultdict(list)
            for res in reservation_details:
                reservations_by_ip[res['ip_address']].append(res)

            for ip, res_list in reservations_by_ip.items():
                merged = merge_intervals(res_list)
                apply_firewall_rules(merged, ip)
            
            
        except Exception as e:
            print(f"Error occurred: {e}")
            
            
        cycle_duration = time.time() - cycle_start_time
        sleep_time = max(0, CHECK_INTERVAL - cycle_duration)  # Calculate dynamic sleep time
        time.sleep(sleep_time)  # Sleep accordingly to maintain the cycle rate

def main():
    block_all_ips(pool)
    if DEVELOPER_MODE:
        if len(sys.argv) > 3:
            ip_address = sys.argv[1]
            start_time = sys.argv[2]
            end_time = sys.argv[3]
        else:
            ip_address = "10.100.101.174"
            start_time = datetime.now().time()
            end_time = (start_time + datetime(minutes=10))
        print("HEI")
        command_input = ["sudo", "iptables", "-A", "FORWARD", "-s", ip_address, "-j", "DROP"]
        subprocess.run(command_input, check=True)
        run_continuously()
    else:
        run_continuously()

if __name__ == "__main__":
    main()
