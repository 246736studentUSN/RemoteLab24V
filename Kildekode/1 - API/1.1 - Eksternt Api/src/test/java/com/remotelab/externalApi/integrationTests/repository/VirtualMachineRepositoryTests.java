package com.remotelab.externalApi.integrationTests.repository;

import com.remotelab.externalApi.TestData;
import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import com.remotelab.externalApi.repositories.VirtualMachineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VirtualMachineRepositoryTests {
    private VirtualMachineRepository virtualMachineRepository;

    @Autowired
    public VirtualMachineRepositoryTests(VirtualMachineRepository virtualMachineRepository){
        this.virtualMachineRepository = virtualMachineRepository;
    }

    @Test
    public void testThatVirtualMachineCanBeRetrieved(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();

        virtualMachineRepository.save(virtualMachine);

        Optional<VirtualMachineEntity> result = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        assertThat(result).isPresent();
    }

    @Test
    public void testThatVirtualMachineCanBeCreated(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();

        virtualMachineRepository.save(virtualMachine);

        Optional<VirtualMachineEntity> result = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(virtualMachine);
    }

    @Test
    public void testThatVirtualMachineCanBeDeleted(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();

        virtualMachineRepository.save(virtualMachine);

        Optional<VirtualMachineEntity> resultBefore = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        assertThat(resultBefore).isPresent();
        assertThat(resultBefore.get()).isEqualTo(virtualMachine);

        virtualMachineRepository.delete(virtualMachine);

        Optional<VirtualMachineEntity> resultAfter = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        assertThat(resultAfter).isNotPresent();
    }

    @Test
    public void testThatVirtualMachineCanBeUpdated(){
        VirtualMachineEntity virtualMachine = TestData.createVirtualMachineA();

        virtualMachineRepository.save(virtualMachine);

        Optional<VirtualMachineEntity> resultBefore = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        virtualMachine.setVirtualMachineName("UPDATED");

        virtualMachineRepository.save(virtualMachine);

        Optional<VirtualMachineEntity> resultAfter = virtualMachineRepository.findById(virtualMachine.getVirtualMachineId());

        assertThat(resultBefore).isPresent();
        assertThat(resultAfter).isPresent();
        assertThat(resultBefore.get()).isNotEqualTo(resultAfter.get());
    }

    @Test
    public void testThatMultipleVirtualMachinesCanBeSaved(){
        VirtualMachineEntity virtualMachineEntityA = TestData.createVirtualMachineA();
        virtualMachineRepository.save(virtualMachineEntityA);

        VirtualMachineEntity virtualMachineEntityB = TestData.createVirtualMachineB();
        virtualMachineRepository.save(virtualMachineEntityB);

        Iterable<VirtualMachineEntity> result = virtualMachineRepository.findAll();

        assertThat(result)
                .hasSize(2)
                .containsExactly(virtualMachineEntityA, virtualMachineEntityB);
    }
}


/*
List of tests:
- Test that virtual machine can be retrieved from db
- Test that virtual machine can be created in the DB
- Test that virtual machine can be deleted from db
- Test that virtual machine can be updated
- Test that multiple virtual machines can be saved and retrieved
 */