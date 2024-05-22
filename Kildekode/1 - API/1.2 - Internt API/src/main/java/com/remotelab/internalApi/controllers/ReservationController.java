package com.remotelab.internalApi.controllers;

import com.remotelab.internalApi.domain.dto.ReservationDTO;
import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.internalApi.mappers.implementations.ReservationMapper;
import com.remotelab.internalApi.services.ReservationService;
import com.remotelab.internalApi.services.VirtualMachineService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*")
@RestController
public class ReservationController {

    // lines 25-36 facilitates for dependency injection
    private ReservationService reservationService;
    private ReservationMapper reservationMapper;
    private VirtualMachineService virtualMachineService;


    public ReservationController(ReservationService reservationService,
                                 ReservationMapper reservationMapper,
                                 VirtualMachineService virtualMachineService){
        this.reservationService = reservationService;
        this.reservationMapper = reservationMapper;
        this.virtualMachineService = virtualMachineService;
    }

    @PostMapping(path = "/internalReservations")
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO,
                                                            HttpServletRequest request){

        // information printed on the console to verify the received address
        System.out.println("IP-address is: " + request.getRemoteAddr());

        // retrieve virtual machine from cookie
        Optional<VirtualMachineEntity> receivedVirtualMachine = virtualMachineService.findByName(request.getRemoteAddr());

        // information printed on the console to verify the received virtual machine
        System.out.println("\nVirtual Machine retrieved: " + receivedVirtualMachine.toString());

        // check if virtual machines exists
        if(!(receivedVirtualMachine.isPresent())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // return http bad request if VM already have reservation
        if(reservationService.virtualMachineExists(receivedVirtualMachine.get())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // map from DTO to entity
        ReservationEntity receivedReservation = reservationMapper.mapFrom(reservationDTO);

        // information printed on the console to verify the received reservation
        System.out.println("\nReservation: " + receivedReservation.toString());

        // save the reservation to database and obtain the result
        ReservationEntity savedReservation =
                reservationService.saveReservation(receivedReservation, receivedVirtualMachine.get().getVirtualMachineId());

        // return response consisting of the saved reservation DTO and http 201 created
        return new ResponseEntity<>(reservationMapper.mapTo(savedReservation), HttpStatus.CREATED);
    }


    @GetMapping(path = "/internalReservations")
    public List<ReservationDTO> getAllReservations(){

        // find all reservations
        List<ReservationEntity> reservationEntities = reservationService.findAll();

        // map each element from the list to DTO and save to another list, when done: return the list to client
        return reservationEntities.stream().map(reservationMapper::mapTo).collect(Collectors.toList());
    }

    @DeleteMapping(path = "/internalReservations")
    public ResponseEntity deleteReservation(HttpServletRequest request){

        // find the reservation corresponding to senders IP-address
        Optional<VirtualMachineEntity> receivedVirtualMachine = virtualMachineService.findByName(request.getRemoteAddr());

        // delete the reservation with the given virtual machine
        reservationService.deleteReservationByVirtualMachine(receivedVirtualMachine.get());

        // return http 204 no content
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
