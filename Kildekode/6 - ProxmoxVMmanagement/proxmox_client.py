from proxmoxer import ProxmoxAPI # Imports the necessary modules from other places
from config import PROXMOX_HOST, PROXMOX_USER, PROXMOX_PASSWORD

# https://proxmoxer.github.io/docs/2.0/ Documentation for Proxmoxer

class ProxmoxClient:
    def __init__(self): # Ran at the start, initalizes the session to the Proxmox VE API with the credentials from config.py. Disables SSL verification due to self-signed certificate on the server
        self.client = ProxmoxAPI(PROXMOX_HOST, user=PROXMOX_USER, password=PROXMOX_PASSWORD, verify_ssl=False)

    def start_vm(self, vm_id): # Starts the VM with the provided VM id
        self.client.nodes("pve").qemu(vm_id).status().start().post()

    def stop_vm(self, vm_id): # Stops the VM with the provided VM id
        self.client.nodes("pve").qemu(vm_id).status().shutdown().post()
    
    def create_ticket(self): # Creates a ticket with the provided credentials and returns the response to the calling process
        return self.client.access().ticket().post(username=PROXMOX_USER, password=PROXMOX_PASSWORD)

