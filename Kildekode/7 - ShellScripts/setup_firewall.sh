#!/bin/bash

# Flush existing iptables rules
iptables -F
iptables -X
iptables -t nat -F
iptables -t nat -X
iptables -t mangle -F
iptables -t mangle -X


# Allow SSH
iptables -A INPUT -p tcp --dport 22 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 22 -j ACCEPT


# Set default policies to DROP
iptables -P INPUT DROP
iptables -P FORWARD DROP
iptables -P OUTPUT DROP

# Allow loopback traffic
iptables -A INPUT -i lo -j ACCEPT
iptables -A OUTPUT -o lo -j ACCEPT

# Allow established connections
iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
iptables -A OUTPUT -m state --state ESTABLISHED,RELATED -j ACCEPT


# Allow MySQL connections on localhost and remote
iptables -A INPUT -p tcp --dport 3306 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 3306 -j ACCEPT

# Allow MySQL X Protocol
iptables -A INPUT -p tcp --dport 33060 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 33060 -j ACCEPT

# Allow DNS traffic
iptables -A INPUT -p udp --dport 53 -j ACCEPT
iptables -A OUTPUT -p udp --sport 53 -j ACCEPT
iptables -A INPUT -p tcp --dport 53 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 53 -j ACCEPT



# Allow HTTP traffic
iptables -A INPUT -p tcp --dport 8080 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 8080 -j ACCEPT
iptables -A INPUT -p tcp --dport 8081 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 8081 -j ACCEPT
iptables -A INPUT -p tcp --dport 8006 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 8006 -j ACCEPT

# Allow noVNC traffic
iptables -A INPUT -p tcp --dport 5900 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 5900 -j ACCEPT
iptables -A INPUT -p tcp --dport 6080 -j ACCEPT
iptables -A OUTPUT -p tcp --sport 6080 -j ACCEPT

# Allow ICMP (ping)
#iptables -A INPUT -p icmp --icmp-type echo-request -j ACCEPT
#iptables -A OUTPUT -p icmp --icmp-type echo-reply -j ACCEPT
#iptables -A OUTPUT -p icmp --icmp-type echo-request -j ACCEPT
#iptables -A INPUT -p icmp --icmp-type echo-reply -j ACCEPT

# Allow apt traffic (HTTP and HTTPS)
iptables -A OUTPUT -p tcp --dport 80 -j ACCEPT
iptables -A OUTPUT -p tcp --dport 443 -j ACCEPT
iptables -A INPUT -p tcp --sport 80 -j ACCEPT
iptables -A INPUT -p tcp --sport 443 -j ACCEPT

# Allow all incoming and outgoing traffic from 10.100.101.62
sudo iptables -A INPUT -s 10.100.101.62 -j ACCEPT
sudo iptables -A OUTPUT -d 10.100.101.62 -j ACCEPT

# Allow all traffic from and to 10.100.101.0/24
iptables -A INPUT -s 10.100.101.20 -j ACCEPT
iptables -A OUTPUT -d 10.100.101. -j ACCEPT

# Log dropped packets (optional)
iptables -A INPUT -j LOG --log-prefix "IPTables-Dropped: " --log-level 4
iptables -A OUTPUT -j LOG --log-prefix "IPTables-Dropped: " --log-level 4

# Save iptables rules
# Install iptables-persistent if not installed
if ! dpkg -l | grep -qw iptables-persistent; then
    echo "Installing iptables-persistent..."
    apt-get update
    DEBIAN_FRONTEND=noninteractive apt-get install -y iptables-persistent
fi

# Save iptables rules
netfilter-persistent save

# Ensure iptables rules are restored on reboot
systemctl enable netfilter-persistent
echo "Firewall rules have been configured and saved."
