package com.remotelab.externalApi.repositories;

import com.remotelab.externalApi.domain.entities.VirtualMachineEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// crud repository gives CRUD-functionality towards database
public interface VirtualMachineRepository extends CrudRepository<VirtualMachineEntity, Long> {}
