package com.remotelab.externalApi.controllers;

import com.remotelab.externalApi.domain.dto.CredentialsDTO;
import com.remotelab.externalApi.domain.entities.CredentialsEntity;
import com.remotelab.externalApi.mappers.implementations.CredentialsMapper;
import com.remotelab.externalApi.services.CredentialsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
public class CredentialsController {

    // lines 21-29 facilitates for dependency injection
    private CredentialsService credentialsService;
    private CredentialsMapper credentialsMapper;

    public CredentialsController(CredentialsService credentialsService,
                                 CredentialsMapper credentialsMapper){
        this.credentialsService = credentialsService;
        this.credentialsMapper = credentialsMapper;
    }

    @GetMapping("/credentials")
    public ResponseEntity<CredentialsDTO> getCredentials(@RequestParam("email") String userEmail){

        // extracts request parameter and calls service to retrieve possible credentials entity
        Optional<CredentialsEntity> receivedEntity = credentialsService.findCredentials(userEmail);

        // return http 404 bad request if the provided email does not exist
        if(!(receivedEntity.isPresent())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // otherwise, return http 200 ok with the credentials DTO
        return new ResponseEntity<>(credentialsMapper.mapTo(receivedEntity.get()), HttpStatus.OK);
    }
}
