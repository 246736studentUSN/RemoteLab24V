package com.remotelab.externalApi.services.implementations;

import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.repositories.VirtualMachineRepository;
import com.remotelab.externalApi.services.VirtualMachineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class VirtualMachineServiceImplementation implements VirtualMachineService {

    // lines 17-21 facilitates for dependency injection
    private VirtualMachineRepository virtualMachineRepository;

    public VirtualMachineServiceImplementation(VirtualMachineRepository virtualMachineRepository){
        this.virtualMachineRepository = virtualMachineRepository;
    }

    @Override
    public VirtualMachineEntity save(VirtualMachineEntity virtualMachineEntity) {

        // save received virtual machine to database
        return virtualMachineRepository.save(virtualMachineEntity);
    }

    @Override
    public Optional<VirtualMachineEntity> findSpecific(Long virtualMachineId) {

        // see if virtual machine with the given ID exists in database, return the variable
        Optional<VirtualMachineEntity> virtualMachineEntity = virtualMachineRepository.findById(virtualMachineId);
        return virtualMachineEntity;
    }

    @Override
    public List<VirtualMachineEntity> findAll() {

        // find all virtual machines from database
        Iterable<VirtualMachineEntity> virtualMachines = virtualMachineRepository.findAll();

        // go through each one, add to list and then return to caller
        return StreamSupport.stream(virtualMachines.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean virtualMachineExists(Long virtualMachineId) {

        // return a true or false value depending on if the virtual machine exists or not
        return virtualMachineRepository.existsById(virtualMachineId);
    }

    @Override
    public VirtualMachineEntity fullUpdate(VirtualMachineEntity virtualMachineEntity) {

        // save, and overwrite, existing virtual machine
        return virtualMachineRepository.save(virtualMachineEntity);
    }

    @Override
    public VirtualMachineEntity partialUpdate(VirtualMachineEntity virtualMachineEntity){

        // find the virtual machine, then map over each attribute
        // only the fields containing values will be updated
        // save the half-updated entity to database and return to caller, otherwise return exception
        return virtualMachineRepository.findById(virtualMachineEntity.getVirtualMachineId()).map(existingVM ->{
            Optional.ofNullable(virtualMachineEntity.getVirtualMachineName()).ifPresent(existingVM::setVirtualMachineName);
            Optional.ofNullable(virtualMachineEntity.getConnectionAddress()).ifPresent(existingVM::setConnectionAddress);
            return virtualMachineRepository.save(existingVM);
        }).orElseThrow(() -> new RuntimeException("Virtual Machine Not Found"));
    }

    @Override
    public void deleteVirtualMachine(Long virtualMachineId){

        // call repository and delete the given virtual machine
        virtualMachineRepository.deleteById(virtualMachineId);
    }
}
