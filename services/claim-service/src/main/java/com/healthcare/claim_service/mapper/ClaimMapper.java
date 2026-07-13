package com.healthcare.claim_service.mapper;

import com.healthcare.claim_service.dto.response.ClaimResponse;
import com.healthcare.claim_service.entity.Claim;
import com.healthcare.claim_service.entity.ClaimStatus;

import java.util.UUID;

public final class ClaimMapper {

    private ClaimMapper() {
    }

    public static Claim toEntity(ClaimResponse claimRequest, UUID submittedByUserId) {

        return Claim.builder()
                .id(UUID.randomUUID())
                .patientId(claimRequest.patientId())
                .providerId(claimRequest.providerId())
                .diagnosisCode(claimRequest.diagnosisCode())
                .amount(claimRequest.amount())
                .submittedByUserId(submittedByUserId)
                .status(ClaimStatus.CREATED)
                .build();
    }

    public static ClaimResponse toResponse(Claim claim) {

       return new ClaimResponse(
               claim.getId(),
               claim.getPatientId(),
               claim.getProviderId(),
               claim.getDiagnosisCode(),
               claim.getAmount(),
               claim.getStatus(),
               claim.getCreatedAt()
       );

    }
}
