package com.remotelab.externalApi.integrationTests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remotelab.externalApi.TestData;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.services.VirtualMachineService;
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
public class VirtualMachineControllerIntegrationTests {


    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private VirtualMachineService virtualMachineService;

    @Autowired
    public VirtualMachineControllerIntegrationTests(MockMvc mockMvc,
                                                    ObjectMapper objectMapper,
                                                    VirtualMachineService virtualMachineService){

        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.virtualMachineService = virtualMachineService;
    }


    // Create - Post - /virtualmachines
    @Test
    public void testThatVirtualMachineCanBeCreatedAndReturnsHttpStatus201Created() throws Exception {
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();

        String virtualMachineEntityToJson = objectMapper.writeValueAsString(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/virtualmachines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineEntityToJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    // Create - Post - /virtualmachines
    @Test
    public void testThatSavedVirtualMachineCanBeRetrievedAndSentToClient() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();

        String virtualMachineEntityToJson = objectMapper.writeValueAsString(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/virtualmachines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineEntityToJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.virtualMachineName").value(virtualMachineEntity.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.connectionAddress").value(virtualMachineEntity.getConnectionAddress())
        );
    }

    // Read - Get - /virtualmachines
    @Test
    public void testThatGetAllVirtualMachinesReturnsHttpStatus200Ok() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    // Read - Get - /virtualmachines
    @Test
    public void testThatVirtualMachinesGetsSentToClient() throws Exception{
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB = TestData.createVirtualMachineB();
        virtualMachineService.save(virtualMachineEntityB);

        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].virtualMachineName").value(virtualMachineEntityA.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].connectionAddress").value(virtualMachineEntityA.getConnectionAddress())
        ).andExpect(MockMvcResultMatchers.jsonPath("$[1].virtualMachineName").value(virtualMachineEntityB.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$[1].connectionAddress").value(virtualMachineEntityB.getConnectionAddress())
        );
    }


    // Read - Get - /virtualmachines/vmId
    @Test
    public void testThatSpecificVMCanBeRetrievedAndGivesHttpStatus200Ok() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines/" + virtualMachineEntity.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Read - Get - /virtualmachines/vmId
    @Test
    public void testThatSpecificVMCannotBeRetrievedAndGivesHttpStatus404NotFound() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines/100")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    // Read - Get - /virtualmachines/vmId
    @Test
    public void testThatSpecificVMCanBeRetrievedAndSentToClient() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines/" + virtualMachineEntity.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.virtualMachineName").value(virtualMachineEntity.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.connectionAddress").value(virtualMachineEntity.getConnectionAddress())
        );
    }

    // Update - Put - /virtualmachines/vmid
    @Test
    public void testThatNonExistingUserReturnsHttpStatus404NotFound() throws Exception{
        VirtualMachineEntity virtualMachineEntity =
                VirtualMachineEntity.builder().virtualMachineName("TestMachine").build();

        String virtualMachineAsString = objectMapper.writeValueAsString(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.put("/virtualmachines/900")
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineAsString)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // Update - Put - /virtualmachines/vmid
    @Test
    public void testThatUpdateOnExistingVirtualMachineReturns200Ok() throws Exception{
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB =
                VirtualMachineEntity.builder().virtualMachineName("UPDATED").build();

        String virtualMachineAsJson = objectMapper.writeValueAsString(virtualMachineEntityB);

        mockMvc.perform(MockMvcRequestBuilders.put("/virtualmachines/" + virtualMachineEntityA.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineAsJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatUpdatedVirtualMachineIsSentBackToClientFullUpdate() throws Exception{
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB = TestData.createVirtualMachineB();

        String virtualMachineAsJson = objectMapper.writeValueAsString(virtualMachineEntityB);

        mockMvc.perform(MockMvcRequestBuilders.put("/virtualmachines/" + virtualMachineEntityA.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineAsJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.virtualMachineName").value(virtualMachineEntityB.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.connectionAddress").value(virtualMachineEntityB.getConnectionAddress())
        );
    }

    // Update - Patch - /virtualmachines/vmId (partial update)
    @Test
    public void testThatNonExistingUserReturns404NotFoundPartialUpdate() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();

        String virtualMachineEntityAsJson = objectMapper.writeValueAsString(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.patch("/virtualmachines/900")
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineEntityAsJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    // Update - Patch - /virtualmachines/vmId (partial update)
    @Test
    public void testThatExistingUserReturns200OkPartialUpdate() throws Exception{
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB =
                VirtualMachineEntity.builder().virtualMachineName("UPDATED").build();

        String virtualMachineEntityAsJson = objectMapper.writeValueAsString(virtualMachineEntityB);

        mockMvc.perform(MockMvcRequestBuilders.patch("/virtualmachines/" + virtualMachineEntityA.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineEntityAsJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }



    // Update - Patch - /virtualmachines/vmId (partial update)
    @Test
    public void testThatUpdatedVirtualMachineIsSentBackToClient() throws Exception{
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB =
                VirtualMachineEntity.builder().virtualMachineName("UPDATED").build();

        String virtualMachineAsJson = objectMapper.writeValueAsString(virtualMachineEntityB);

        mockMvc.perform(MockMvcRequestBuilders.patch("/virtualmachines/" + virtualMachineEntityA.getVirtualMachineId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(virtualMachineAsJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.virtualMachineName").value(virtualMachineEntityB.getVirtualMachineName())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.connectionAddress").value(virtualMachineEntityA.getConnectionAddress())
        );
    }


    // Delete
    @Test
    public void testThatDeleteVirtualMachineReturnsHttpStatus204NoContent() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.delete("/virtualmachines/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    // Delete
    @Test
    public void testThatVirtualMachineIsDeletedFromDatabase() throws Exception{
        VirtualMachineEntity virtualMachineEntity = TestData.createVirtualMachineA();
        virtualMachineService.save(virtualMachineEntity);

        mockMvc.perform(MockMvcRequestBuilders.delete("/virtualmachines/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/virtualmachines/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
