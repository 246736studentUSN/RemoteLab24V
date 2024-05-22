package com.remotelab.externalApi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationNumber;
    @Column(name="start_time")
    private LocalDateTime start;
    @Column(name="end_time")
    private LocalDateTime end;
    @Column(name = "user_email", unique = true)
    private String userEmail;

    @ManyToOne // many reservations can consist of the same virtual machine
    @JoinColumn(name = "virtual_machine_id")
    private VirtualMachineEntity virtualMachineEntity;

    // one reservation can be linked to one credential and vice versa
    // "CascadeType.All" makes sure the credentials are deleted when related reservation is deleted
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials")
    private CredentialsEntity credentialsEntity;
}
