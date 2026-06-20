CREATE TABLE claims (
    id UUID PRIMARY KEY,
    patient_id VARCHAR(100) NOT NULL,
    provider_id VARCHAR(100) NOT NULL,
    diagnosis_code VARCHAR(50) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    submitted_by_user_id UUID,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL
);