package com.healthcare.claim_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ClaimRequest(
        @NotBlank(message = "Patient ID is required")
        String patientId,

        @NotBlank(message = "Provider ID is required")
        String providerId,

        @NotBlank(message = "Diagnosis Code is required")
        String diagnosisCode,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
