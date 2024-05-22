package com.remotelab.internalApi.services.implementations;

import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.services.ClearReservationsService;
import com.remotelab.internalApi.services.ReservationService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ClearReservationServiceImplementation implements ClearReservationsService {

    // lines 17-21 facilitates for dependency injection
    private ReservationService reservationService;

    public ClearReservationServiceImplementation(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @Override
    @Scheduled(fixedRate = 30000) // method runs every 30 seconds
    public void removeOldReservations() {

        // get current time
        LocalTime currentTime = LocalTime.now();

        // get all reservations
        List<ReservationEntity> allReservations = reservationService.findAll();

        // go over each reservation
        allReservations.forEach((reservation -> {

            // extract reservations end time
            LocalTime currentReservation = reservation.getEnd();

            // if the current reservation is older than the current time, then we can delete it
            if(currentReservation.isBefore(currentTime)){

                // print information to the console
                System.out.println("Deleted reservation: \n" + reservation);

                // call service to delete reservation
                reservationService.deleteById(reservation.getReservationNumber());

            }
        }));
    }
}
