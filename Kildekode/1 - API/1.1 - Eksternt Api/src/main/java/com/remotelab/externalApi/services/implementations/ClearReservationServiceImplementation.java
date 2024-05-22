package com.remotelab.externalApi.services.implementations;

import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.services.ClearReservationsService;
import com.remotelab.externalApi.services.CredentialsService;
import com.remotelab.externalApi.services.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClearReservationServiceImplementation implements ClearReservationsService {

    // lines 17-21 facilitates for dependency injection
    private ReservationService reservationService;

    public ClearReservationServiceImplementation(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @Override
    // method runs every minute
    @Scheduled(fixedRate = 60000)
    public void removeOldReservations() {

        // get current date and time
        LocalDateTime currentTime = LocalDateTime.now();

        // get all reservations
        List<ReservationEntity> allReservations = reservationService.findAll();

        // go over each reservation
        allReservations.forEach((reservation -> {

            // extract reservations end time
            LocalDateTime currentReservation = reservation.getEnd();

            // if the current reservation is older than the current time, then we can delete it
            if(currentReservation.isBefore(currentTime)){

                // call service to delete reservation
                reservationService.deleteReservation(reservation.getUserEmail());

                // print information to the console
                System.out.println("Deleted reservation: \n" + reservation);
            }
        }));
    }
}
