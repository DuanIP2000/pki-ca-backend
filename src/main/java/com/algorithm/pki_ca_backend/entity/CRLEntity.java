package com.algorithm.pki_ca_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CRL")
public class CRLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CRLID")
    private Integer crlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CertID", nullable = false)   // FK -> Certificates(CertID)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CertificateEntity certificate;

    @Column(name = "Reason", length = 200)
    private String reason;

    @Column(name = "RevokeTime")
    private LocalDateTime revokeTime;
}
