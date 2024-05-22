package com.remotelab.externalApi.mappers.implementations;

import com.remotelab.externalApi.domain.dto.VirtualMachineDTO;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.mappers.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class VirtualMachineMapper implements Mapper<VirtualMachineEntity, VirtualMachineDTO> {

    // facilitating for dependency injection
    private ModelMapper modelMapper;

    public VirtualMachineMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    // using the model mapper library to define mapping-functions
    @Override
    public VirtualMachineDTO mapTo(VirtualMachineEntity virtualMachineEntity) {
        return modelMapper.map(virtualMachineEntity, VirtualMachineDTO.class);
    }

    // using the model mapper library to define mapping-functions
    @Override
    public VirtualMachineEntity mapFrom(VirtualMachineDTO virtualMachineDTO) {
        return modelMapper.map(virtualMachineDTO, VirtualMachineEntity.class);
    }
}
