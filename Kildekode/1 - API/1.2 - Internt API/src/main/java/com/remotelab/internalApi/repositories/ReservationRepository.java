package com.remotelab.internalApi.repositories;

import com.remotelab.internalApi.domain.entities.ReservationEntity;
import com.remotelab.internalApi.domain.entities.VirtualMachineEntity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {

    // custom method that returns true or false based on received virtual machine entity
    boolean existsByVirtualMachineEntity(VirtualMachineEntity virtualMachineEntity);
}
