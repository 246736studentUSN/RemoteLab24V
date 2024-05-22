package com.remotelab.externalApi.services;

import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.domain.entities.ReservationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
    - Find all reservations within given timeslot (in list)
    - If number of reservations is => 20: return http.notOk - too many reservations
    - Else: See which virtual machines are used, choose the next one
    - Add to reservationEntity

    - From post-controller:
        - Call method to get **number** of reservations within timeslot
            - If number is >= 20, return http status not ok

        - Call method that keeps track of available virtual machines
            - This method should return a VM-ID that can be used for the reservation

        - Create reservation in the given timeslot with the given virtual machine

        To further discuss method from virtual machine:
            - There should be a global variable within the virtualMachineServiceImplementation that:
                - is an array
                - Holds VM-IDs of vacant VM
                - If VM is in use, the value of the index is set to -1
                - Else, the index is the same value as the VM-ID


        Check timeslots
        Find available VM
        Create reservation



        VALIDATION SERVICE (FROM POST-METHOD IN CONTROLLER)

        Nå er det sjekket om antall reservasjoner overstiger 20, hvis det ikke overstiger denne verdien kan en ny reservasjon legges til
        Da må alle virtuelle maskiner innenfor denne tidsrammen hentes ut (slik at vi ser hvilke som er i bruk)
        List<VirtualMachineEntity> liste
        for(int i = 0; i < numberOfVirtualMachines; i++){
            if(!liste.contains(i)){
                return virtualMachineService.findById(i);
            }
        }
 */

public interface ReservationService {

    // method for saving a reservation, returns the resulting reservation entity
    ReservationEntity saveReservation(ReservationEntity receivedReservationEntity, Long virtualMachineId, String userEmail, CredentialsEntity credentials);

    // method for finding all reservations
    List<ReservationEntity> findAll();

    // method for finding one specific method by email
    Optional<ReservationEntity> findOneByEmail(String userEmail);

    // void-method for deleting reservation based on email
    void deleteReservation(String userEmail);

    // method for finding all reservations with the date and time passed in as argument
    List<ReservationEntity> getNumberOfReservationsInTimeslot(LocalDateTime start);
}
