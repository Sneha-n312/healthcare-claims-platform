package com.healthcare.claim_service.dto.response;

import com.healthcare.claim_service.entity.ClaimStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ClaimResponse(

        UUID id,

        String patientId,

        String providerId,

        String diagnosisCode,

        BigDecimal amount,

        ClaimStatus status,

        OffsetDateTime createdAt
) {
}
