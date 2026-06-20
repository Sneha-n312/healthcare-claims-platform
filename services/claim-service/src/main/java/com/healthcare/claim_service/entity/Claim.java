package com.healthcare.claim_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim extends AuditableEntity{

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "diagnosis_code", nullable = false )
    private String diagnosisCode;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ClaimStatus status;

    @Column(name = "submitted_by_user_id")
    private UUID submittedByUserId;

    @Version
    @Column(name = "version")
    private Long version;

}
