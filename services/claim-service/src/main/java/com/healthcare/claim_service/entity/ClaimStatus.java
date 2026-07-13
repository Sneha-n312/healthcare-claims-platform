package com.healthcare.claim_service.entity;

public enum ClaimStatus {

    CREATED,
    ENRICHING,
    ENRICHED,
    VALIDATING,
    VALIDATED,
    FRAUD_CHECKING,
    FRAUD_PASSED,
    POLICY_CHECKING,
    REQUIRES_MANUAL_REVIEW,
    APPROVED,
    REJECTED
}
