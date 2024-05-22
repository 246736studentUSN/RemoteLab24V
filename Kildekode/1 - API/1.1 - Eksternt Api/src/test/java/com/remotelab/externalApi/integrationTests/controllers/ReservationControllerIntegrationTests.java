package com.remotelab.externalApi.integrationTests.controllers;

import com.remotelab.externalApi.TestData;
import com.remotelab.externalApi.domain.dto.ReservationDTO;
import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.domain.entities.ReservationEntity;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.mappers.implementations.ReservationMapper;
import com.remotelab.externalApi.services.CredentialsService;
import com.remotelab.externalApi.services.ReservationService;
import com.remotelab.externalApi.services.VirtualMachineService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ReservationService reservationService;
    private VirtualMachineService virtualMachineService;
    private CredentialsService credentialsService;
    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationControllerIntegrationTests(MockMvc mockMvc,
                                                 ObjectMapper objectMapper,
                                                 ReservationService reservationService,
                                                 VirtualMachineService virtualMachineService,
                                                 ReservationMapper reservationMapper,
                                                 CredentialsService credentialsService){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.reservationService = reservationService;
        this.virtualMachineService = virtualMachineService;
        this.reservationMapper = reservationMapper;
        this.credentialsService = credentialsService;
    }
}
