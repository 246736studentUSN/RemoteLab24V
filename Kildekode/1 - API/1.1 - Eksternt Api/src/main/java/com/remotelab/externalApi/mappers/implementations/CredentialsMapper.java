package com.remotelab.externalApi.mappers.implementations;

import com.remotelab.externalApi.domain.dto.CredentialsDTO;
import com.remotelab.externalApi.domain.entities.CredentialsEntity;

import com.remotelab.externalApi.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CredentialsMapper implements Mapper<CredentialsEntity, CredentialsDTO> {

    // facilitating for dependency injection
    private ModelMapper modelMapper;

    public CredentialsMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    // using the model mapper library to define mapping-functions
    @Override
    public CredentialsDTO mapTo(CredentialsEntity credentialsEntity) {
        return modelMapper.map(credentialsEntity, CredentialsDTO.class);
    }

    // using the model mapper library to define mapping-functions
    @Override
    public CredentialsEntity mapFrom(CredentialsDTO credentialsDTO) {
        return modelMapper.map(credentialsDTO, CredentialsEntity.class);
    }
}
