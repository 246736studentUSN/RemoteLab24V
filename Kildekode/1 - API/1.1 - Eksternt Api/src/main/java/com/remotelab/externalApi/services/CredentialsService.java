package com.remotelab.externalApi.services;
// add row with email when reservation is created
// delete row when reservation gets deleted

import com.remotelab.externalApi.domain.entities.CredentialsEntity;

import java.util.Optional;

public interface CredentialsService {

    // method for finding credentials based on email, returns an optional (can be empty or present)
    Optional<CredentialsEntity> findCredentials(String userEmail);

    // method for saving credentials, only attributes email and virtual machine id are passed as arguments
    CredentialsEntity saveCredentials(String userEmail, Long virtualMachineId);

    // void-method for deleting credentials based on email
    void deleteCredentials(String userEmail);
}
