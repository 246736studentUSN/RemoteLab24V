package com.remotelab.externalApi.repositories;

import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// crud repository gives CRUD-functionality towards database
public interface CredentialsRepository extends CrudRepository<CredentialsEntity, String> {

    // defining a custom method to be performed towards database
    Optional<CredentialsEntity> findByUserEmail(String userEmail);
}
