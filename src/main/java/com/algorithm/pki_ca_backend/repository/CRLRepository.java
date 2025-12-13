package com.algorithm.pki_ca_backend.repository;

import com.algorithm.pki_ca_backend.entity.CRLEntity;
import com.algorithm.pki_ca_backend.entity.CertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CRLRepository extends JpaRepository<CRLEntity, Integer> {
    // 判断某个证书是否已经被吊销（是否存在于 CRL 中）
    boolean existsByCertificate(CertificateEntity certificate);
}
