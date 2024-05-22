package com.remotelab.internalApi.mappers.implementations;

import com.remotelab.internalApi.domain.dto.VirtualMachineDTO;
import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.internalApi.mappers.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VirtualMachineMapper implements Mapper<VirtualMachineEntity, VirtualMachineDTO> {

    // lines 14-16 facilitates for dependency injection
    private ModelMapper modelMapper;

    public VirtualMachineMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public VirtualMachineDTO mapTo(VirtualMachineEntity virtualMachineEntity) {

        // use the function provided by model mapper to convert between DTO and entity
        return modelMapper.map(virtualMachineEntity, VirtualMachineDTO.class);
    }

    @Override
    public VirtualMachineEntity mapFrom(VirtualMachineDTO virtualMachineDTO) {

        // use the function provided by model mapper to convert between DTO and entity
        return modelMapper.map(virtualMachineDTO, VirtualMachineEntity.class);
    }
}
