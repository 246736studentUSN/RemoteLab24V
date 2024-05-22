import mysql.connector # Imports the necessary modules
from config import DB_HOST, DB_USER, DB_PASSWORD, DB_NAME

# https://dev.mysql.com/doc/connector-python/en/ Documentation for mysql connector

def connect_to_database(): # The function connects to the database using credentials obtained from config.py
    try:
        connection = mysql.connector.connect(
            host=DB_HOST,
            user=DB_USER,
            password=DB_PASSWORD,
            database=DB_NAME
        )
        return connection
    except mysql.connector.Error as error:
        print("Failed to connect to the database:", error)
        return None

def fetch_reservations(): # Retrieves all the content from the table 'reservations' with the SELECT statement
    connection = connect_to_database()
    if not connection:
        return []

    try:
        cursor = connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM reservations")
        reservations = cursor.fetchall()
        return reservations
    except mysql.connector.Error as error:
        print("Error fetching reservations:", error)
        return []
    finally:
        cursor.close()
        connection.close()

def fetch_virtual_machines(): # Retrieves all the content from the table 'virtual_machines' with the SELECT statement
    connection = connect_to_database()
    if not connection:
        return []

    try:
        cursor = connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM virtual_machines")
        virtual_machines = cursor.fetchall()
        return virtual_machines
    except mysql.connector.Error as error:
        print("Error fetching virtual machines:", error)
        return []
    finally:
        cursor.close()
        connection.close()

def fetch_credentials(): # Retrieves all the content from the table 'credentials' with the SELECT statement
    connection = connect_to_database()
    if not connection:
        return []

    try:
        cursor = connection.cursor(dictionary=True)
        cursor.execute("SELECT * FROM credentials")
        credentials = cursor.fetchall()
        return credentials
    except mysql.connector.Error as error:
        print("Error fetching virtual machines:", error)
        return []
    finally:
        cursor.close()
        connection.close()

def update_credentials(user_email, cookie, csrf_token): # Updates the contents of a row in the 'credentials' table where 'user_email' matches with the provided parameter. Uses the UPDATE statement
    connection = connect_to_database()
    if not connection:
        return False

    try:
        cursor = connection.cursor()
        query = "UPDATE credentials SET cookie = %s, csrf_token = %s WHERE user_email = %s"
        cursor.execute(query, (cookie, csrf_token, user_email))
        connection.commit()
        return True
    except mysql.connector.Error as error:
        print("Error inserting credentials:", error)
        return False
    finally:
        cursor.close()
        connection.close()
