package com.algorithm.pki_ca_backend.service;

import com.algorithm.pki_ca_backend.entity.OperationLogEntity;
import com.algorithm.pki_ca_backend.repository.OperationLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class OperationLogService {

    private final OperationLogRepository operationLogRepository;

    public OperationLogService(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    public void record(String operator, String action, String target, String details) {
        OperationLogEntity log = new OperationLogEntity();
        log.setOperator(operator);
        log.setAction(action);
        log.setTarget(target);
        log.setDetails(details);
        log.setActionTime(LocalDateTime.now());
        operationLogRepository.save(log);
    }
}
