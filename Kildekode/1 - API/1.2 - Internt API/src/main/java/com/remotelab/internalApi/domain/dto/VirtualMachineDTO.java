package com.remotelab.internalApi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualMachineDTO {
    private Long virtualMachineId;
    private String virtualMachineName;
    private String connectionAddress;
}
