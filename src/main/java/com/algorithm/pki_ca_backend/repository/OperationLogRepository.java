package com.algorithm.pki_ca_backend.repository;

import com.algorithm.pki_ca_backend.entity.OperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationLogRepository extends JpaRepository<OperationLogEntity, Integer> {
}
