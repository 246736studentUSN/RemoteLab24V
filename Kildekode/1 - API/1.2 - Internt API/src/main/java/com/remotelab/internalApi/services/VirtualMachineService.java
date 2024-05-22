package com.remotelab.internalApi.services;

import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;

import java.util.List;
import java.util.Optional;

// interface is implemented in the class "VirtualMachineServiceImplementation"
public interface VirtualMachineService {

    // saves the received virtual machine to database, returns the saved virtual machine
    VirtualMachineEntity save(VirtualMachineEntity virtualMachineEntity);

    // returns an optional, but tries to find specific virtual machine by the given ID
    Optional<VirtualMachineEntity> findSpecific(Long virtualMachineId);

    // finds all virtual machines and returns a list consisting of these
    List<VirtualMachineEntity> findAll();

    // check if the provided virtual machine exists, returns true or false
    boolean virtualMachineExists(Long virtualMachineId);

    // updates entire virtual machine, returns the updated virtual machine
    VirtualMachineEntity fullUpdate(VirtualMachineEntity virtualMachineEntity);

    // updates parts of the virtual machine, returns the updated virtual machine
    VirtualMachineEntity partialUpdate(VirtualMachineEntity updatedVirtualMachine);

    // void-method for deleting virtual machine
    void deleteVirtualMachine(Long virtualMachineId);

    // find virtual machine by connection address, returns an optional
    Optional<VirtualMachineEntity> findByName(String connectionAddress);
}
