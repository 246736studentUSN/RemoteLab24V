package com.remotelab.externalApi.mappers.implementations;

import com.remotelab.externalApi.domain.dto.ReservationDTO;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.mappers.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper implements Mapper<ReservationEntity, ReservationDTO> {

    // facilitating for dependency injection
    private ModelMapper modelMapper;

    public ReservationMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    // using the model mapper library to define mapping-functions
    @Override
    public ReservationDTO mapTo(ReservationEntity reservationEntity) {
        return modelMapper.map(reservationEntity, ReservationDTO.class);
    }

    // using the model mapper library to define mapping-functions
    @Override
    public ReservationEntity mapFrom(ReservationDTO reservationDTO) {
        return modelMapper.map(reservationDTO, ReservationEntity.class);
    }
}
