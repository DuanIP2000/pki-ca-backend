package com.algorithm.pki_ca_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CertificateRequests")
public class CertificateRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "UserID")
    private UserEntity user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Column(nullable = false)
    private String status;   // PENDING / ISSUED / REJECTED

    @Column(nullable = false)
    private LocalDateTime requestTime;

    private LocalDateTime approveTime;
}

