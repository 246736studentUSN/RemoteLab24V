package com.remotelab.externalApi.controllers;

import com.remotelab.externalApi.domain.dto.VirtualMachineDTO;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.mappers.implementations.VirtualMachineMapper;
import com.remotelab.externalApi.services.VirtualMachineService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class VirtualMachineController {

    // lines 21-29 facilitates for dependency injection
    private VirtualMachineService virtualMachineService;
    private VirtualMachineMapper virtualMachineMapper;

    public VirtualMachineController(VirtualMachineService virtualMachineService,
                                    VirtualMachineMapper virtualMachineMapper){

        this.virtualMachineService = virtualMachineService;
        this.virtualMachineMapper = virtualMachineMapper;
    }

    @PostMapping(path = "/virtualmachines")
    public ResponseEntity<VirtualMachineDTO> createVirtualMachine(@RequestBody VirtualMachineDTO virtualMachineDTO){

        // map from DTO to virtual machine entity
        VirtualMachineEntity virtualMachineEntity = virtualMachineMapper.mapFrom(virtualMachineDTO);

        // save virtual machine entity to database
        VirtualMachineEntity savedVirtualMachine = virtualMachineService.save(virtualMachineEntity);

        // return response consisting of the saved virtual machine DTO and http 201 created
        return new ResponseEntity<>(virtualMachineMapper.mapTo(savedVirtualMachine), HttpStatus.CREATED);
    }

    @GetMapping(path = "/virtualmachines/{virtualMachineId}")
    public ResponseEntity<VirtualMachineDTO> getSpecificVirtualMachine(@PathVariable("virtualMachineId") Long virtualMachineId){

        // extract path variable and pass it as parameter to the "findSpecific"-method
        Optional<VirtualMachineEntity> foundVirtualMachine = virtualMachineService.findSpecific(virtualMachineId);

        // if virtual machine is found, return response consisting of virtual machine DTO and http 200 ok,
        // otherwise return 404 not found
        return foundVirtualMachine.map(virtualMachineEntity -> {
            return new ResponseEntity<>(virtualMachineMapper.mapTo(virtualMachineEntity), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/virtualmachines")
    public List<VirtualMachineDTO> getAllVirtualMachines(){

        // create a list and call the virtual machine service to find all virtual machines
        List<VirtualMachineEntity> virtualMachinesEntity = virtualMachineService.findAll();

        // map through each element of the list and transform it into a DTO
        // create new list and add each DTO, when every element has been mapped over the list can be returned to client
        return virtualMachinesEntity.stream().map(virtualMachineMapper::mapTo).collect(Collectors.toList());
    }

    @PutMapping(path = "/virtualmachines/{virtualMachineId}")
    public ResponseEntity<VirtualMachineDTO> fullUpdate(@PathVariable("virtualMachineId") Long virtualMachineId,
                                                        @RequestBody VirtualMachineDTO virtualMachineDTO){

        // check if the virtual machine to be updated actually exists, if it does not - return http 404 not found
        if(!virtualMachineService.virtualMachineExists(virtualMachineId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // map from DTO to entity
        VirtualMachineEntity virtualMachineEntity = virtualMachineMapper.mapFrom(virtualMachineDTO);

        // make sure to set the virtual machine ID equal to the one in database
        // when save-method is called the row with the corresponding ID will be updated
        virtualMachineEntity.setVirtualMachineId(virtualMachineId);

        // save the updated version to database (overwriting the old values)
        VirtualMachineEntity savedVirtualMachine = virtualMachineService.fullUpdate(virtualMachineEntity);

        // return response consisting of virtual machine DTO and http 200 ok
        return new ResponseEntity<>(virtualMachineMapper.mapTo(savedVirtualMachine), HttpStatus.OK);
    }


    @PatchMapping(path = "/virtualmachines/{virtualMachineId}")
    public ResponseEntity<VirtualMachineDTO> partialUpdate(@PathVariable("virtualMachineId") Long virtualMachineId,
                                                           @RequestBody VirtualMachineDTO virtualMachineDTO){

        // check if the virtual machine exists, if it does not - return http status 404
        if(!virtualMachineService.virtualMachineExists(virtualMachineId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // map from DTO to entity
        VirtualMachineEntity updatedVirtualMachine = virtualMachineMapper.mapFrom(virtualMachineDTO);

        // make sure to set the virtual machine ID equal to the one in database
        // when save-method is called the row with the corresponding ID will be updated
        updatedVirtualMachine.setVirtualMachineId(virtualMachineId);

        // partial method is called, this means that only certain fields has to be updated
        VirtualMachineEntity savedVirtualMachine = virtualMachineService.partialUpdate(updatedVirtualMachine);

        // return response consisting of DTO and http 200 ok
        return new ResponseEntity<>(virtualMachineMapper.mapTo(savedVirtualMachine), HttpStatus.OK);
    }

    @DeleteMapping(path = "/virtualmachines/{virtualMachineId}")
    public ResponseEntity deleteVirtualMachine(@PathVariable("virtualMachineId") Long virtualMachineId){

        // call virtual machine service to delete virtual machine with specific ID
        virtualMachineService.deleteVirtualMachine(virtualMachineId);

        // return http 204 no content
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
