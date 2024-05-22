package com.remotelab.internalApi.repositories;

import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VirtualMachineRepository extends CrudRepository<VirtualMachineEntity, Long> {

    // find and return virtual machine based on the connection address
    Optional<VirtualMachineEntity> findByConnectionAddress(String connectionAddress);
}
