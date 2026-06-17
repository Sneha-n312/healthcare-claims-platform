# Development Conventions

## Purpose

This document defines development standards for the Healthcare Claims Processing Platform.

The goal is to ensure consistency across all microservices and simplify maintenance as the platform evolves.

---

# Repository Structure

```text
healthcare-claims-platform

├── docs
├── docker
├── services
│
├── auth-service
├── claim-service
├── workflow-service
├── enrichment-service
├── validation-service
├── fraud-service
└── policy-service
```

---

# Package Structure

Every microservice must follow the same package layout.

```text
controller
service
repository
entity
dto
mapper
event
config
exception
security
```

Optional packages:

```text
scheduler
client
validator
util
```

---

# Naming Conventions

## Classes

### Controllers

```java
ClaimController
AuthController
ManualReviewController
```

### Services

```java
ClaimService
WorkflowService
AuthenticationService
```

### Repositories

```java
ClaimRepository
UserRepository
ClaimHistoryRepository
```

### DTOs

```java
SubmitClaimRequest
ClaimResponse
LoginRequest
LoginResponse
```

### Events

```java
ClaimSubmittedEvent
ClaimEnrichedEvent
ValidationCompletedEvent
FraudCompletedEvent
PolicyCompletedEvent
```

---

# API Standards

## Base Path

```text
/api/v1
```

Example:

```http
/api/v1/claims

/api/v1/auth/login

/api/v1/manual-review
```

---

## REST Naming

Use nouns.

Good:

```http
GET /claims

POST /claims

GET /claims/{id}
```

Avoid:

```http
GET /getClaims

POST /createClaim
```

---

# Response Standards

Success Response Example

```json
{
  "claimId": "123",
  "status": "SUBMITTED"
}
```

---

Error Response Example

```json
{
  "errorCode": "CLAIM_NOT_FOUND",
  "message": "Claim does not exist",
  "timestamp": "2026-06-17T10:00:00Z"
}
```

---

# Database Standards

## Naming Style

Use:

```text
snake_case
```

Examples:

```text
claim_history
outbox_events
processed_events
```

Avoid:

```text
ClaimHistory
claimHistory
```

---

## Primary Keys

All primary keys should use:

```java
UUID
```

Example:

```java
UUID id;
```

---

## Audit Fields

Every table should contain:

```text
created_at
updated_at
```

Where applicable:

```text
created_by
updated_by
```

---

# Claim Status Standards

Allowed statuses:

```text
SUBMITTED

ENRICHING

ENRICHED

VALIDATING

VALIDATED

FRAUD_CHECKING

FRAUD_PASSED

POLICY_CHECKING

REQUIRES_MANUAL_REVIEW

APPROVED

REJECTED
```

Never use free-text status values.

Always use enums.

---

# Kafka Standards

## Topic Naming

Use lowercase with hyphens.

Examples:

```text
claim-submitted

claim-enriched

validation-completed

fraud-completed

policy-completed

claim-approved

claim-rejected

claim-manual-review
```

---

## Retry Topics

Convention:

```text
<topic>-retry
```

Example:

```text
claim-submitted-retry
```

---

## Dead Letter Queue

Convention:

```text
<topic>-dlq
```

Example:

```text
claim-submitted-dlq
```

---

# Event Standards

Every event must contain:

```json
{
  "eventId": "",
  "eventType": "",
  "correlationId": "",
  "occurredAt": ""
}
```

---

## Event Naming

Past tense.

Examples:

```text
ClaimSubmittedEvent

ClaimEnrichedEvent

ValidationCompletedEvent

FraudCompletedEvent

PolicyCompletedEvent
```

Avoid:

```text
SubmitClaimEvent

ValidateClaimEvent
```

---

# Correlation ID Rules

Every request must generate a correlation ID.

The same correlation ID should be propagated across:

* Kafka events
* Logs
* Downstream service calls

Purpose:

* Traceability
* Debugging
* Distributed tracing

---

# Logging Standards

Use structured logging.

Example:

```text
Claim submitted successfully

claimId=123
correlationId=abc-123
status=SUBMITTED
```

Do not log:

* Passwords
* JWT tokens
* Sensitive user information

---

# Security Standards

## Authentication

JWT-based authentication.

## Authorization

Supported roles:

```text
ROLE_PROVIDER

ROLE_ADJUSTER

ROLE_ADMIN
```

---

## Password Storage

Passwords must be stored using:

```text
BCrypt
```

Never store plaintext passwords.

---

# Testing Standards

## Unit Tests

Naming:

```text
ClaimServiceTest

WorkflowServiceTest
```

---

## Integration Tests

Naming:

```text
ClaimControllerIT

KafkaConsumerIT
```

---

## Coverage Target

Minimum:

```text
80%
```

Focus on:

* Service layer
* State machine
* Validators
* Kafka consumers

---

# Git Conventions

## Branch Naming

Feature:

```text
feature/claim-submission
```

Bugfix:

```text
bugfix/kafka-retry
```

Refactor:

```text
refactor/workflow-state-machine
```

---

## Commit Messages

Examples:

```text
feat: add claim submission endpoint

feat: implement kafka producer

fix: resolve duplicate event processing

refactor: introduce state machine validator

docs: update architecture diagrams
```

---

# Architecture Principles

* Event-driven communication
* Loose coupling
* High cohesion
* Idempotent processing
* Auditability
* Reliability first
* API-first design

---

# Reliability Principles

Always design for:

* Duplicate messages
* Consumer failures
* Service restarts
* Network failures
* Eventual consistency

Never assume:

* Exactly-once delivery
* Perfect network conditions
* Ordered message processing across services
