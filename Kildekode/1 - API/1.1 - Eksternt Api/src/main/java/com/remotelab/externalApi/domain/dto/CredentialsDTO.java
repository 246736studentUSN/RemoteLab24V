package com.remotelab.externalApi.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsDTO {
    private String cookie;
    private String token;
    private Long virtualMachineId;
}
