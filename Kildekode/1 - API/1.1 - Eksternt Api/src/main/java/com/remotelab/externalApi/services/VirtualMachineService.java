package com.remotelab.externalApi.services;

import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;

import java.util.List;
import java.util.Optional;

public interface VirtualMachineService {

    // method for saving virtual machine
    VirtualMachineEntity save(VirtualMachineEntity virtualMachineEntity);

    // method for finding specific virtual machine by the given ID
    Optional<VirtualMachineEntity> findSpecific(Long virtualMachineId);

    // method for finding all virtual machines
    List<VirtualMachineEntity> findAll();

    // method to check if the provided virtual machine exists, returns true or false
    boolean virtualMachineExists(Long virtualMachineId);

    // method to update all attributes of a virtual machine
    VirtualMachineEntity fullUpdate(VirtualMachineEntity virtualMachineEntity);

    // method to update some of the attributes of a given virtual machine
    VirtualMachineEntity partialUpdate(VirtualMachineEntity updatedVirtualMachine);

    // void-method for deleting a virtual machine
    void deleteVirtualMachine(Long virtualMachineId);
}
