package com.remotelab.externalApi.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "credentials")
public class CredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // primary key, database manages and controls how the key is created
    private Long credentialsNumber;

    @Column(name = "cookie", length = 800)
    private String cookie;

    @Column(name = "csrf_token", length = 800)
    private String token;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "virtual_machine_id")
    private Long virtualMachineId;
}
