package com.algorithm.pki_ca_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "OperationLogs")
public class OperationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private Integer logId;

    @Column(name = "Operator", length = 50)
    private String operator;

    @Column(name = "Action", length = 100)
    private String action;

    @Column(name = "Target", length = 100)
    private String target;

    @Column(name = "ActionTime")
    private LocalDateTime actionTime;

    @Column(name = "Details", columnDefinition = "nvarchar(max)")
    private String details;
}
