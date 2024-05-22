package com.remotelab.externalApi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="virtual_machines")
public class VirtualMachineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vm_id")
    private Long virtualMachineId;

    @Column(name = "vm_name")
    private String virtualMachineName;

    @Column(name = "connection_address")
    private String connectionAddress;
}