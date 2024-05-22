package com.remotelab.externalApi.integrationTests.repository;

import com.remotelab.externalApi.TestData;
import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.repositories.CredentialsRepository;
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
public class CredentialsRepositoryTests {
    private CredentialsRepository credentialsRepository;

    @Autowired
    public CredentialsRepositoryTests(CredentialsRepository credentialsRepository){
        this.credentialsRepository = credentialsRepository;
    }

    @Test
    public void testThatCredentialsCanBeCreated(){
        CredentialsEntity credentials = TestData.createCredentialsA();
        credentialsRepository.save(credentials);

        Optional<CredentialsEntity> result = credentialsRepository.findByUserEmail(credentials.getUserEmail());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(credentials);
    }

    @Test
    public void testThatCredentialsCanBeUpdated(){
        CredentialsEntity credentials = TestData.createCredentialsA();
        credentialsRepository.save(credentials);

        credentials.setUserEmail("UPDATED@UPDATED.UPDATED");

        credentialsRepository.save(credentials);

        Optional<CredentialsEntity> result = credentialsRepository.findByUserEmail("UPDATED@UPDATED.UPDATED");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(credentials);
    }

    @Test
    public void testThatCredentialsCanBeDeleted(){
        CredentialsEntity credentials = TestData.createCredentialsA();
        credentialsRepository.save(credentials);

        Optional<CredentialsEntity> resultBefore = credentialsRepository.findByUserEmail("test@test.test");

        assertThat(resultBefore).isPresent();

        credentialsRepository.delete(credentials);

        Optional<CredentialsEntity> resultAfter = credentialsRepository.findByUserEmail("UPDATED@UPDATED.UPDATED");
        assertThat(resultAfter).isNotPresent();
    }

    @Test
    public void testThatMultipleCredentialsCanBeStoredInDatabase(){
        CredentialsEntity credentialsA = TestData.createCredentialsA();
        credentialsRepository.save(credentialsA);

        CredentialsEntity credentialsB = TestData.createCredentialsB();
        credentialsRepository.save(credentialsB);

        Iterable<CredentialsEntity> result = credentialsRepository.findAll();

        assertThat(result).hasSize(2)
                .containsExactly(credentialsA, credentialsB);
    }

    @Test
    public void testThatCustomMethodGetCredentialsByUserEmailWorks(){
        CredentialsEntity credentialsA = TestData.createCredentialsA();
        credentialsRepository.save(credentialsA);

        Optional<CredentialsEntity> result = credentialsRepository.findByUserEmail(credentialsA.getUserEmail());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(credentialsA);
    }
}

/*
List of tests:
- Test that credentials can be created
- Test that credentials can be updated
- Test that credentials can be deleted
- Test that multiple credentials can be saved to DB
- Test that custom method works (get credentials by user email)
 */
