#!/bin/bash

# Flush all existing rules
iptables -F
iptables -t nat -F
iptables -t mangle -F
iptables -t raw -F

# Delete all custom chains
iptables -X
iptables -t nat -X
iptables -t mangle -X
iptables -t raw -X

# Set default policies to ACCEPT (optional, but recommended to ensure network functionality)
iptables -P INPUT ACCEPT
iptables -P FORWARD ACCEPT
iptables -P OUTPUT ACCEPT

# Remove iptables-persistent saved rules
sudo rm -f /etc/iptables/rules.v4
sudo rm -f /etc/iptables/rules.v6

# Optionally, you can uninstall iptables-persistent if you no longer want to use it
sudo apt-get remove -y iptables-persistent

echo "All iptables rules have been flushed, deleted, and iptables-persistent rules have been cleared."

# Reboot the system
sudo reboot
