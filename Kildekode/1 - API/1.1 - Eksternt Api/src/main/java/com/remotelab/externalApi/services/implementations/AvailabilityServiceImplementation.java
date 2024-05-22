package com.remotelab.externalApi.services.implementations;


import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.services.AvailabilityService;
import com.remotelab.externalApi.services.ReservationService;
import com.remotelab.externalApi.services.VirtualMachineService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityServiceImplementation implements AvailabilityService {

    // lines 20-27 facilitates for dependency injection
    private ReservationService reservationService;
    private VirtualMachineService virtualMachineService;


    public AvailabilityServiceImplementation(ReservationService reservationService, VirtualMachineService virtualMachineService) {
        this.reservationService = reservationService;
        this.virtualMachineService = virtualMachineService;
    }


    // method for finding available virtual machine, takes on parameter which is the requested start time
    public VirtualMachineEntity findAvailableVirtualMachine(LocalDateTime startTime) {

        // find number of reservations within the requested timeslot
        List<ReservationEntity> allReservationsWithinTimeslot = reservationService.getNumberOfReservationsInTimeslot(startTime);

        // create empty list that eventually will hold all the virtual machines that are occupied
        List<VirtualMachineEntity> occupiedVirtualMachines = new ArrayList<>();

        // find all virtual machines
        List<VirtualMachineEntity> allVirtualMachines = virtualMachineService.findAll();

        // declare reservation entity
        ReservationEntity reservationEntity;

        // declare virtual machine entity
        VirtualMachineEntity virtualMachineEntity = null;

        // if there are no reservations in the given timeslot, the first virtual machine can be return to client
        if (allReservationsWithinTimeslot.isEmpty()) {
            virtualMachineEntity = allVirtualMachines.get(0);
        }

        // otherwise we have to map through all the found reservations within the timeslot
        else {
            for (int i = 0; i < allReservationsWithinTimeslot.size(); i++) {

                // set reservation entity equal to the first found reservation
                reservationEntity = allReservationsWithinTimeslot.get(i);

                // extract virtual machine and add it to list of occupied virtual machines
                occupiedVirtualMachines.add(reservationEntity.getVirtualMachineEntity());
            }

            // then loop through all virtual machines
            for(VirtualMachineEntity virtualMachine: allVirtualMachines){

                // if the list of occupied virtual machines does not contain the current virtual machine, update
                // virtual machine entity
                if(!(occupiedVirtualMachines.contains(virtualMachine))){
                    virtualMachineEntity = virtualMachine;
                }
            }
        }

        // return the virtual machine
        return virtualMachineEntity;
    }
}
