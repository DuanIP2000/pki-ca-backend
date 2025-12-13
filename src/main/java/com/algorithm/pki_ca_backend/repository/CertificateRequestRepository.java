package com.algorithm.pki_ca_backend.repository;

import com.algorithm.pki_ca_backend.entity.CertificateRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificateRequestRepository
        extends JpaRepository<CertificateRequestEntity, Long> {

    List<CertificateRequestEntity> findByStatus(String status);

}

