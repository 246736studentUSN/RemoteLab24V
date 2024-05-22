package com.remotelab.externalApi.services.implementations;

import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.repositories.CredentialsRepository;
import com.remotelab.externalApi.services.CredentialsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsServiceImplementation implements CredentialsService {

    // lines 14-18 facilitates for dependency injection
    private CredentialsRepository credentialsRepository;

    public CredentialsServiceImplementation(CredentialsRepository credentialsRepository){
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public Optional<CredentialsEntity> findCredentials(String userEmail) {

        // call custom method from repository interface sending user email as parameter
        return credentialsRepository.findByUserEmail(userEmail);
    }

    @Override
    public CredentialsEntity saveCredentials(String userEmail, Long virtualMachineId) {

        // use builder-pattern to create credential entity with only two fields initialized
        CredentialsEntity credentialsEntity = CredentialsEntity
                .builder()
                .userEmail(userEmail)
                .virtualMachineId(virtualMachineId)
                .build();

        // save to database and return the saved credential entity
        return credentialsRepository.save(credentialsEntity);
    }

    @Override
    public void deleteCredentials(String userEmail) {

        // find credentials by looking for email
        Optional<CredentialsEntity> credentials = findCredentials(userEmail);

        // extract credential-object and delete it
        credentialsRepository.delete(credentials.get());
    }
}
