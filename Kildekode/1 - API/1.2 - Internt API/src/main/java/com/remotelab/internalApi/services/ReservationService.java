package com.remotelab.internalApi.services;

import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;

import java.util.List;

// "ReservationServiceImplementation" implements this interface
public interface ReservationService {

    // method for saving reservation, returns reservation entity and takes the
    // two parameters "receivedReservationEntity" and "virtualMachineId"
    ReservationEntity saveReservation(ReservationEntity receivedReservationEntity, Long virtualMachineId);

    // Method for finding all reservations, returns a list with the found reservations
    List<ReservationEntity> findAll();

    // void-method for deleting reservation by the provided virtual machine ID
    void deleteReservationByVirtualMachine(VirtualMachineEntity virtualMachineEntity);

    // method the checks if the provided virtual machine exists, returns true or false
    boolean virtualMachineExists(VirtualMachineEntity virtualMachineEntity);

    // void-method for deleting reservation by reservation number (primary key)
    void deleteById(Long reservationNumber);
}
