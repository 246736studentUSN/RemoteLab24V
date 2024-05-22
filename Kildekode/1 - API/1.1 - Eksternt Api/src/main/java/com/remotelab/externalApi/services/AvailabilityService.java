package com.remotelab.externalApi.services;

import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;

import java.time.LocalDateTime;

public interface AvailabilityService {

    // method with return type "Virtual Machine Entity" and parameter "LocalDateTime"
    VirtualMachineEntity findAvailableVirtualMachine(LocalDateTime startTime);
}
