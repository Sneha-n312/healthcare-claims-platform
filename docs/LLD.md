# Healthcare Claims Processing Platform

# Low Level Design (LLD)

## Package Structure

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

---

# API Design

## Authentication

### Register User

```http
POST /api/v1/auth/register
```

### Login

```http
POST /api/v1/auth/login
```

---

## Claim APIs

### Submit Claim

```http
POST /api/v1/claims
```

### Get Claim

```http
GET /api/v1/claims/{claimId}
```

### Claim History

```http
GET /api/v1/claims/{claimId}/history
```

---

## Manual Review

### List Pending Reviews

```http
GET /api/v1/manual-review
```

### Approve Claim

```http
POST /api/v1/manual-review/{claimId}/approve
```

### Reject Claim

```http
POST /api/v1/manual-review/{claimId}/reject
```

---

# Domain Model

## Claim

| Field         |
| ------------- |
| id            |
| patientId     |
| providerId    |
| diagnosisCode |
| amount        |
| status        |
| createdBy     |
| createdAt     |
| updatedAt     |

---

## ClaimHistory

| Field          |
| -------------- |
| id             |
| claimId        |
| previousStatus |
| newStatus      |
| eventType      |
| createdAt      |

---

## User

| Field     |
| --------- |
| id        |
| username  |
| password  |
| role      |
| createdAt |

---

## OutboxEvent

| Field     |
| --------- |
| id        |
| eventId   |
| eventType |
| payload   |
| published |
| createdAt |

---

## ProcessedEvent

| Field       |
| ----------- |
| id          |
| eventId     |
| processedAt |

---

# Claim Status Enum

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

---

# Database Schema

## claims

```text
id
patient_id
provider_id
diagnosis_code
amount
status
created_by
created_at
updated_at
```

Indexes:

```text
id
status
patient_id
created_by
```

---

## claim_history

```text
id
claim_id
previous_status
new_status
event_type
created_at
```

---

## users

```text
id
username
password
role
created_at
```

---

## outbox_events

```text
id
event_id
event_type
payload
published
created_at
```

---

## processed_events

```text
id
event_id
processed_at
```

---

# Service Layer

## ClaimService

Responsibilities:

* Create claims
* Retrieve claims
* Update status
* Create history records
* Create outbox events

---

## AuthenticationService

Responsibilities:

* Register users
* Authenticate users
* Generate JWT tokens

---

## WorkflowService

Responsibilities:

* State transitions
* Event orchestration

---

# Event Model

## Base Event

```json
{
  "eventId": "",
  "eventType": "",
  "correlationId": "",
  "occurredAt": ""
}
```

---

## ClaimSubmittedEvent

```json
{
  "eventId": "",
  "claimId": "",
  "patientId": "",
  "providerId": "",
  "amount": 0
}
```

---

## ClaimEnrichedEvent

Contains:

* Risk category
* Diagnosis description
* Review level

---

## ValidationCompletedEvent

Contains:

* Validation result
* Failure reason

---

## FraudCompletedEvent

Contains:

* Fraud result
* Fraud score

---

## PolicyCompletedEvent

Contains:

* Policy result
* Coverage information

---

# State Machine

Valid transitions:

```text
SUBMITTED -> ENRICHING

ENRICHING -> ENRICHED

ENRICHED -> VALIDATING

VALIDATING -> VALIDATED

VALIDATED -> FRAUD_CHECKING

FRAUD_CHECKING -> FRAUD_PASSED

FRAUD_PASSED -> POLICY_CHECKING

POLICY_CHECKING -> APPROVED

POLICY_CHECKING -> REQUIRES_MANUAL_REVIEW

REQUIRES_MANUAL_REVIEW -> APPROVED

REQUIRES_MANUAL_REVIEW -> REJECTED
```

Invalid transitions must throw:

```text
InvalidStateTransitionException
```

---

# Security Design

Authentication:

* JWT

Authorization:

* ROLE_PROVIDER
* ROLE_ADJUSTER
* ROLE_ADMIN

---

# Design Patterns

* Repository Pattern
* Builder Pattern
* Factory Pattern
* Strategy Pattern
* State Machine Pattern
* Outbox Pattern

---

# Reliability Design

## Outbox Pattern

Transaction:

```text
Save Claim

Save Outbox Event

Commit
```

Publisher:

```text
Outbox Table

↓

Kafka
```

---

## Idempotent Consumers

Before processing:

```text
Check Processed Event Table
```

If event already exists:

```text
Ignore Message
```

---

## Dead Letter Queue

Failed messages routed to:

```text
claim-dlq
```

---

# Testing Strategy

## Unit Tests

* Services
* Validators
* State machine

## Integration Tests

* Repository layer
* Kafka producer
* Kafka consumer
* REST APIs

Tools:

* JUnit 5
* Mockito
* Testcontainers

Target Coverage:

```text
80%+
```
