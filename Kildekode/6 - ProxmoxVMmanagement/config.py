import keyring
from keyrings.alt.file import PlaintextKeyring
import logging
# Configure logging
logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

# https://keyring.readthedocs.io/en/latest/ Documentation for keyrings
# https://docs.python.org/3/howto/logging.html Documentation for logging

# Sets the keyring 
keyring.set_keyring(PlaintextKeyring())

# Defines the service and usernames for the credentials. Should be removed after being ran once
db_service_id = "database_service"
db_username = "****"
db_password = '****'  

proxmox_service_id = "proxmox_service"
proxmox_username = "****"
proxmox_password = "****"  

# Stores passwords in the keyring, only needs to run once
try:
    keyring.set_password(db_service_id, db_username, db_password)
    logger.info("Database password has been securely stored in the keyring.")

    keyring.set_password(proxmox_service_id, proxmox_username, proxmox_password)
    logger.info("Proxmox password has been securely stored in the keyring.")
except Exception as e:
    logger.error(f"Error storing password in keyring: {e}")


# Retrieves passwords from the keyring by specifying service name and username
try:
    db_password = keyring.get_password(db_service_id, db_username)
    if db_password:
        logger.info("Retrieved database password from the keyring.")
    else:
        logger.error("Failed to retrieve database password from the keyring.")

    proxmox_password = keyring.get_password(proxmox_service_id, proxmox_username)
    if proxmox_password:
        logger.info("Retrieved Proxmox password from the keyring.")
    else:
        logger.error("Failed to retrieve Proxmox password from the keyring.")
except Exception as e:
    logger.error(f"Error retrieving password from keyring: {e}")

# Stores the values into variables which is to be used by other programs.
DB_HOST = "localhost"
DB_USER = db_username
DB_PASSWORD = db_password
DB_NAME = "ekstern"

PROXMOX_HOST = "10.100.101.20"
PROXMOX_USER = proxmox_username
PROXMOX_PASSWORD = proxmox_password

# Use the retrieved credentials in your application
logger.info(f"Database host: {DB_HOST}, User: {DB_USER}")
logger.info(f"Proxmox host: {PROXMOX_HOST}, User: {PROXMOX_USER}")
