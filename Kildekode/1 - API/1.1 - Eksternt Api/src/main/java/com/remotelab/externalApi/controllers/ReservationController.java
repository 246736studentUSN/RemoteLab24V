package com.remotelab.externalApi.controllers;

import com.remotelab.externalApi.domain.dto.ReservationDTO;
import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.mappers.implementations.ReservationMapper;
import com.remotelab.externalApi.services.AvailabilityService;
import com.remotelab.externalApi.services.CredentialsService;
import com.remotelab.externalApi.services.ReservationService;
import com.remotelab.externalApi.services.VirtualMachineService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class ReservationController {

    // lines 27-43 facilitates for dependency injection
    private ReservationService reservationService;
    private ReservationMapper reservationMapper;
    private VirtualMachineService virtualMachineService;
    private CredentialsService credentialsService;
    private AvailabilityService availabilityService;

    public ReservationController(ReservationService reservationService,
                                 ReservationMapper reservationMapper,
                                 VirtualMachineService virtualMachineService,
                                 AvailabilityService availabilityService,
                                 CredentialsService credentialsService){
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.virtualMachineService = virtualMachineService;
        this.availabilityService = availabilityService;
        this.credentialsService = credentialsService;
    }

    @PostMapping(path = "/reservations")
    public ResponseEntity<ReservationDTO> createReservation(@RequestParam("email") String userEmail,
                                                            @RequestBody ReservationDTO reservationDTO){

        // get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // map from DTO to reservation entity
        ReservationEntity receivedReservationEntity = reservationMapper.mapFrom(reservationDTO);

        // make sure the submitted date is not from the past
        if(currentTime.isAfter(receivedReservationEntity.getStart()) || currentTime.isAfter(receivedReservationEntity.getEnd())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // make sure the submitted date is not from the past
        if(receivedReservationEntity.getStart().isAfter(receivedReservationEntity.getEnd())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // get number of reservations in the desired timeslot
        List<ReservationEntity> numberOfReservationsInDesiredTimeslot =
                reservationService.getNumberOfReservationsInTimeslot(receivedReservationEntity.getStart());

        // if the number of reservations exceeds available virtual machines, request has to be denied
        if(numberOfReservationsInDesiredTimeslot.size() >= virtualMachineService.findAll().size()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // otherwise, call service to find available virtual machine
        VirtualMachineEntity virtualMachine =
                availabilityService.findAvailableVirtualMachine(receivedReservationEntity.getStart());

        // save credentials to database
        CredentialsEntity credentials = credentialsService.saveCredentials(userEmail, virtualMachine.getVirtualMachineId());

        // save reservation to database
        ReservationEntity savedReservationEntity =
                reservationService.saveReservation(receivedReservationEntity, virtualMachine.getVirtualMachineId(), userEmail, credentials);

        // return saved reservation DTO and http 200 ok to client
        return new ResponseEntity<>(reservationMapper.mapTo(savedReservationEntity), HttpStatus.OK);
    }



    @GetMapping(path = "/reservations")
    public ResponseEntity<ReservationDTO> getOneReservationByEmail(@RequestParam("email") String userEmail){

        // check if the reservation exists in the database
        Optional<ReservationEntity> foundReservation = reservationService.findOneByEmail(userEmail);

        // if the reservation exists, return reservation DTO and http 200 ok, otherwise return http 404 not found
        if (foundReservation.isPresent()){
            return new ResponseEntity<>(reservationMapper.mapTo(foundReservation.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/reservations")
    public ResponseEntity deleteReservation(@RequestParam("email") String userEmail){

        // call reservation service and pass in the received email as parameter
        reservationService.deleteReservation(userEmail);

        // return http 204 no content regardless of result from last step
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
