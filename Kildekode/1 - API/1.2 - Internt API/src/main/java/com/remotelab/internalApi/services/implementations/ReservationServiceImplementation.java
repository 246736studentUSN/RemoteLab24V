package com.remotelab.internalApi.services.implementations;

import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.internalApi.repositories.ReservationRepository;
import com.remotelab.internalApi.services.ReservationService;
import com.remotelab.internalApi.services.VirtualMachineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationServiceImplementation implements ReservationService {

    // lines 19-27 facilitates for dependency injection
    private ReservationRepository reservationRepository;
    private VirtualMachineService virtualMachineService;


    public ReservationServiceImplementation(ReservationRepository reservationRepository,
                                            VirtualMachineService virtualMachineService){
        this.reservationRepository = reservationRepository;
        this.virtualMachineService = virtualMachineService;
    }

    @Override
    public ReservationEntity saveReservation(ReservationEntity receivedReservationEntity, Long virtualMachineId) {

        // find virtual machine based on the provided ID
        Optional<VirtualMachineEntity> virtualMachineEntity = virtualMachineService.findSpecific(virtualMachineId);

        // check if virtual machine exists
        if(virtualMachineEntity.isPresent()){

            // set the virtual machine entity of the received reservation
            receivedReservationEntity.setVirtualMachineEntity(virtualMachineEntity.get());
        }

        // return the created reservation entity
        return reservationRepository.save(receivedReservationEntity);
    }

    @Override
    public List<ReservationEntity> findAll() {

        // find all reservations and add them to list, send list back to caller
        return StreamSupport.stream(
                        reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList()
                );
    }

    @Override
    public void deleteReservationByVirtualMachine(VirtualMachineEntity virtualMachineEntity) {

        // find all reservations
        Iterable<ReservationEntity> allReservations = reservationRepository.findAll();

        // go through each reservation and check if the reservations virtual machine is equal to the one provided in parameter
        allReservations.forEach(reservation -> {

            // if it is:
            if(reservation.getVirtualMachineEntity().equals(virtualMachineEntity)){

                // delete from database
                reservationRepository.deleteById(reservation.getReservationNumber());
            }
        });
    }

    @Override
    public boolean virtualMachineExists(VirtualMachineEntity virtualMachineEntity) {

        // check if the provided virtual machine exists, return true or false based on the result
        return reservationRepository.existsByVirtualMachineEntity(virtualMachineEntity);
    }

    @Override
    public void deleteById(Long reservationNumber) {

        // delete reservation based on its primary key, the reservation number
        reservationRepository.deleteById(reservationNumber);
    }
}
