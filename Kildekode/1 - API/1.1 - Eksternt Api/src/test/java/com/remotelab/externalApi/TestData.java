package com.remotelab.externalApi;

import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;

import java.time.LocalDateTime;

/*
    This class uses the builder-pattern to create objects that can be used for testing purposes, this pattern is
    described in the final report

    Methods are static, so they can be accessed without creating instance of the "TestData"-class
 */

public final class TestData {
    private TestData(){}

    public static VirtualMachineEntity createVirtualMachineA(){
        return VirtualMachineEntity
                .builder()
                .virtualMachineName("student-vm-1")
                .connectionAddress("192.168.1.1")
                .build();
    }

    public static VirtualMachineEntity createVirtualMachineB(){
        return VirtualMachineEntity
                .builder()
                .virtualMachineName("student-vm-2")
                .connectionAddress("192.168.1.2")
                .build();
    }

    public static ReservationEntity createReservationEntityA(){
        return ReservationEntity.builder()
                .start(LocalDateTime.of(2025, 1, 2, 10, 00, 00))
                .end(LocalDateTime.of(2025, 1, 2, 11, 00, 00))
                .userEmail("testmail@test.test")
                .build();
    }

    public static ReservationEntity createReservationEntityB(){
        return ReservationEntity.builder()
                .start(LocalDateTime.of(2025, 8, 10, 15, 00, 00))
                .end(LocalDateTime.of(2025, 5, 22, 16, 00, 00))
                .userEmail("newuser@new.user")
                .build();
    }

    public static ReservationEntity createReservationEntityC(){
        return ReservationEntity.builder()
                .start(LocalDateTime.of(2025, 1, 2, 10, 00, 00))
                .end(LocalDateTime.of(2025, 1, 2, 13, 00, 00))
                .userEmail("newuser@new.user")
                .build();
    }

    public static CredentialsEntity createCredentialsA(){
        return CredentialsEntity.builder()
                .userEmail("test@test.test")
                .cookie("cookieA")
                .token("tokenA")
                .build();
    }

    public static CredentialsEntity createCredentialsB(){
        return CredentialsEntity.builder()
                .userEmail("user@user.user")
                .cookie("cookieB")
                .token("tokenB")
                .build();
    }
}
