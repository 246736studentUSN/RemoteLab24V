package com.remotelab.externalApi.integrationTests.repository;

import com.remotelab.externalApi.TestData;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.repositories.ReservationRepository;
import com.remotelab.externalApi.services.VirtualMachineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationRepositoryTests {
    private ReservationRepository reservationRepository;
    private VirtualMachineService virtualMachineService;

    @Autowired
    public ReservationRepositoryTests(ReservationRepository reservationRepository,
                                      VirtualMachineService virtualMachineService){
        this.reservationRepository = reservationRepository;
        this.virtualMachineService = virtualMachineService;
    }

    @Test
    public void testThatReservationCanBeCreated(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntity = virtualMachineService.save(virtualMachine);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntity);

        reservationRepository.save(reservation);

        Optional<ReservationEntity> result = reservationRepository.findById(reservation.getReservationNumber());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(reservation);
    }

    @Test
    public void testThatReservationCanBeDeleted(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntity = virtualMachineService.save(virtualMachine);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntity);

        reservationRepository.save(reservation);

        Optional<ReservationEntity> resultBefore = reservationRepository.findById(reservation.getReservationNumber());

        assertThat(resultBefore).isPresent();
        assertThat(resultBefore.get()).isEqualTo(reservation);

        reservationRepository.delete(reservation);

        Optional<ReservationEntity> resultAfter = reservationRepository.findById(reservation.getReservationNumber());

        assertThat(resultAfter).isNotPresent();
    }

    @Test
    public void testThatReservationCanBeUpdated(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntity = virtualMachineService.save(virtualMachine);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntity);

        reservationRepository.save(reservation);

        reservation.setUserEmail("UPDATED@UPDATED.UPDATED");

        reservationRepository.save(reservation);

        Optional<ReservationEntity> result = reservationRepository.findById(reservation.getReservationNumber());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(reservation);
        assertThat(result.get().getReservationNumber()).isEqualTo(1L);
    }

    @Test
    public void testThatCustomFunctionFindEntriesFromStartTimeWorks(){
        VirtualMachineEntity virtualMachineA = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntityA = virtualMachineService.save(virtualMachineA);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntityA);

        reservationRepository.save(reservation);

        VirtualMachineEntity virtualMachineB = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntityB = virtualMachineService.save(virtualMachineB);

        ReservationEntity reservationEntityB = TestData.createReservationEntityC();
        reservationEntityB.setVirtualMachineEntity(virtualMachineEntityB);
        reservationRepository.save(reservationEntityB);

        List<ReservationEntity> result =
                reservationRepository
                        .findNumberOfEntriesFromStartTime(
                                LocalDateTime.of(2025, 1, 2, 10, 00, 00)
                        );

        assertThat(result).hasSize(2).containsExactly(reservation, reservationEntityB);
    }

    @Test
    public void testThatCustomMethodDeleteByUserEmailWorks(){
        VirtualMachineEntity virtualMachineA = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntityA = virtualMachineService.save(virtualMachineA);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntityA);

        reservationRepository.save(reservation);

        Optional<ReservationEntity> resultBefore =
                reservationRepository.findById(reservation.getReservationNumber());

        assertThat(resultBefore).isPresent();
        assertThat(resultBefore.get()).isEqualTo(reservation);

        reservationRepository.deleteByUserEmail(reservation.getUserEmail());

        Optional<ReservationEntity> resultAfter =
                reservationRepository.findById(reservation.getReservationNumber());

        assertThat(resultAfter).isNotPresent();
    }

    @Test
    public void testThatCustomMethodFindReservationByUserEmailWorks(){
        VirtualMachineEntity virtualMachineA = TestData.createVirtualMachineA();
        VirtualMachineEntity virtualMachineEntityA = virtualMachineService.save(virtualMachineA);

        ReservationEntity reservation = TestData.createReservationEntityA();
        reservation.setVirtualMachineEntity(virtualMachineEntityA);

        reservationRepository.save(reservation);

        Optional<ReservationEntity> result =
                reservationRepository.findReservationByUserEmail(reservation.getUserEmail());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(reservation);
    }
}

/*
List of tests:
- Test that reservation can be created
- Test that reservation can be deleted
- Test that reservation can be updated
- Test that custom query works as expected (find reservations from startTime)
- Test that custom query works as expected (delete user by email)
- Test that custom query works as expected (find reservation by user email)
 */
