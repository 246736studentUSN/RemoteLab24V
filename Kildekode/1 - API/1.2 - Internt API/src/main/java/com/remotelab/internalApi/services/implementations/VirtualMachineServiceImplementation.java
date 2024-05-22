package com.remotelab.internalApi.services.implementations;

import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.internalApi.repositories.VirtualMachineRepository;
import com.remotelab.internalApi.services.VirtualMachineService;
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

        // save virtual machine to database
        return virtualMachineRepository.save(virtualMachineEntity);
    }

    @Override
    public Optional<VirtualMachineEntity> findSpecific(Long virtualMachineId) {

        // call database and see if the virtual machine exists
        Optional<VirtualMachineEntity> virtualMachineEntity = virtualMachineRepository.findById(virtualMachineId);

        // return the result
        return virtualMachineEntity;
    }

    @Override
    public List<VirtualMachineEntity> findAll() {

        // find all virtual machines from the table
        Iterable<VirtualMachineEntity> virtualMachines = virtualMachineRepository.findAll();

        // return a list of the found virtual machines to caller
        return StreamSupport.stream(virtualMachines.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public boolean virtualMachineExists(Long virtualMachineId) {

        // returns true or false based on the result
        return virtualMachineRepository.existsById(virtualMachineId);
    }

    @Override
    public VirtualMachineEntity fullUpdate(VirtualMachineEntity virtualMachineEntity) {

        // sends received object to database as a save-method, this will replace the existing content with the new
        return virtualMachineRepository.save(virtualMachineEntity);
    }

    @Override
    public VirtualMachineEntity partialUpdate(VirtualMachineEntity virtualMachineEntity){

        // get the virtual machine to be changed and map over it
        // if the received virtual machine (from parameter) has the given attribute, it will be updated in database
        // return the saved entity or an exception if the virtual machine is not found
        return virtualMachineRepository.findById(virtualMachineEntity.getVirtualMachineId()).map(existingVM ->{
            Optional.ofNullable(virtualMachineEntity.getVirtualMachineName()).ifPresent(existingVM::setVirtualMachineName);
            Optional.ofNullable(virtualMachineEntity.getConnectionAddress()).ifPresent(existingVM::setConnectionAddress);
            return virtualMachineRepository.save(existingVM);
        }).orElseThrow(() -> new RuntimeException("Virtual Machine Not Found"));
    }

    @Override
    public void deleteVirtualMachine(Long virtualMachineId){

        // delete virtual machine from database using the provided ID
        virtualMachineRepository.deleteById(virtualMachineId);
    }

    @Override
    public Optional<VirtualMachineEntity> findByName(String connectionAddress) {

        // return the virtual machine based on the provided connetion address
        return virtualMachineRepository.findByConnectionAddress(connectionAddress);
    }
}
