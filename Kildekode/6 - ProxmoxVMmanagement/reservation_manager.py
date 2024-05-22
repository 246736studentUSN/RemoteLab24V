from datetime import datetime
import time
from database import fetch_reservations, fetch_virtual_machines, update_credentials
from proxmox_client import ProxmoxClient

# Imports the necessary modules

shift_val = 114 # Creates a shift value that corresponds to the id of the VM at the start of the range of VMs in RemoteLabActivePool minus 1
proxmox_client = ProxmoxClient() # Initializes the object instance of ProxmoxClient

def process_reservation(reservation, active_timeslots, current_time): # Checks if any reservations has entered the active timeperiod, if so the reservations is entered in the list of active reservations, also the chosen VM is started and a PVEAuthCookie and CSRFPrevention Token is provided to the users' row in the 'credentials' table
    start_time = reservation["start_time"]
    end_time = reservation["end_time"]
    vm_id = reservation["virtual_machine_id"]
    user_email = reservation["user_email"]

    if start_time <= current_time < end_time and check_active_timeslots(active_timeslots, vm_id):
        active_timeslots.append({"vm_id": vm_id, "start_time": start_time, "end_time": end_time, "user_email": user_email})
        for slot in active_timeslots:
            test = proxmox_client.create_ticket()
            update_credentials(slot["user_email"], test["ticket"], test["CSRFPreventionToken"])
        proxmox_client.start_vm(vm_id + shift_val)

def fetch_and_process_reservations(active_timeslots): # Checks if any reservations is out of time, if so it removes it out of the list active reservations and stops the VM. There is also check if anything fails. If it fails all VMs are stopped to be safe
    reservations = fetch_reservations()
    current_time = datetime.now()
    condition = lambda x: x['end_time'] <= current_time

    try:
        removed_reservations = [d for d in active_timeslots if condition(d)]
        active_timeslots[:] = [d for d in active_timeslots if not condition(d)]

        for reservation in removed_reservations:
            vm_id = reservation["vm_id"]
            proxmox_client.stop_vm(vm_id + shift_val)
            print(f"Stoppet VM {vm_id}.")

        for reservation in reservations:
            process_reservation(reservation, active_timeslots, current_time)
    except Exception as e:
        stop_all_vms()
        raise e

def stop_all_vms(): # The function to stop all VMs fecthed from the 'virtual_machines' table
    virtual_machines = fetch_virtual_machines()

    for virtual_machine in virtual_machines:
        vm_id = virtual_machine["vm_id"]
        proxmox_client.stop_vm(vm_id + shift_val)
        print("Stoppet alle VM.")

def check_active_timeslots(active_timeslots, vm_id): # Function to check if a reservation exists in the active timeslots list
    for slot in active_timeslots:
        if slot['vm_id'] == vm_id:
            return False
    return True

def main(): # The start point for the program, called on from main.py
    active_timeslots = []
    while True:
        fetch_and_process_reservations(active_timeslots)
        time.sleep(60)

