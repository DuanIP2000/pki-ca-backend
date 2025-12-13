package com.algorithm.pki_ca_backend.controller;

import com.algorithm.pki_ca_backend.entity.OperationLogEntity;
import com.algorithm.pki_ca_backend.repository.OperationLogRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class OperationLogController {

    private final OperationLogRepository logRepository;

    public OperationLogController(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping
    public List<OperationLogEntity> getAllLogs() {
        return logRepository.findAll();
    }
}
