package com.remotelab.internalApi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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
    private LocalTime start;
    @Column(name="end_time")
    private LocalTime end;

    @ManyToOne // many reservations can consist of the same virtual machine
    @JoinColumn(name = "virtual_machine_id")
    private VirtualMachineEntity virtualMachineEntity;
}
