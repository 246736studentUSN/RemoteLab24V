package com.remotelab.internalApi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private LocalTime start;
    private LocalTime end;
    private VirtualMachineDTO virtualMachineDTO;
}
