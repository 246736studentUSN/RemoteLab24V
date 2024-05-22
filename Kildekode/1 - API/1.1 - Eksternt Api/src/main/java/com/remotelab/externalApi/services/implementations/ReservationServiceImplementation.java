package com.remotelab.externalApi.services.implementations;

import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.repositories.ReservationRepository;
import com.remotelab.externalApi.services.ReservationService;
import com.remotelab.externalApi.services.VirtualMachineService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationServiceImplementation implements ReservationService {

    // lines 21-30 facilitate for dependency injection
    private ReservationRepository reservationRepository;
    private VirtualMachineService virtualMachineService;


    public ReservationServiceImplementation(ReservationRepository reservationRepository,
                                            VirtualMachineService virtualMachineService){
        this.reservationRepository = reservationRepository;
        this.virtualMachineService = virtualMachineService;
    }

    @Override
    public ReservationEntity saveReservation(ReservationEntity receivedReservationEntity, Long virtualMachineId, String userEmail, CredentialsEntity credentials) {

        // since the DTO only contains start and end time we need to set email and credentials entity manually
        receivedReservationEntity.setUserEmail(userEmail);
        receivedReservationEntity.setCredentialsEntity(credentials);

        // make sure the virtual machine with the virtual machine ID actually exists
        Optional<VirtualMachineEntity> virtualMachineEntity = virtualMachineService.findSpecific(virtualMachineId);

        // if it exists, set virtual machine entity manually
        if(virtualMachineEntity.isPresent()){
            receivedReservationEntity.setVirtualMachineEntity(virtualMachineEntity.get());
        }

        // return the created reservation entity
        return reservationRepository.save(receivedReservationEntity);
    }

    @Override
    public Optional<ReservationEntity> findOneByEmail(String userEmail) {

        // find reservation with email and return the result
        return reservationRepository.findReservationByUserEmail(userEmail);
    }

    @Override
    public void deleteReservation(String userEmail) {

        // delete reservation by mail
        reservationRepository.deleteByUserEmail(userEmail);
    }

    @Override
    public List<ReservationEntity> getNumberOfReservationsInTimeslot(LocalDateTime start) {

        // call custom query to find all reservations with given start time, return list of these reservations
        return reservationRepository.findNumberOfEntriesFromStartTime(start);
    }

    @Override
    public List<ReservationEntity> findAll() {

        //return list of all reservations
        return StreamSupport.stream(
                        reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()
                );
    }
}
