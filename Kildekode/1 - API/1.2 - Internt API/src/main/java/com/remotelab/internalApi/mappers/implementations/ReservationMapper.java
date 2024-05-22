package com.remotelab.internalApi.mappers.implementations;

import com.remotelab.internalApi.domain.dto.ReservationDTO;
import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.mappers.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper implements Mapper<ReservationEntity, ReservationDTO> {

    // lines 14-16 facilitates for dependency injection
    private ModelMapper modelMapper;

    public ReservationMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public ReservationDTO mapTo(ReservationEntity reservationEntity) {

        // use the function provided by model mapper to convert between DTO and entity
        return modelMapper.map(reservationEntity, ReservationDTO.class);
    }

    @Override
    public ReservationEntity mapFrom(ReservationDTO reservationDTO) {

        // use the function provided by model mapper to convert between DTO and entity
        return modelMapper.map(reservationDTO, ReservationEntity.class);
    }
}
