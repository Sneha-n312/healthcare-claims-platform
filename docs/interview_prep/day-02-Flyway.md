# Day 2 - Interview Preparation Notes

## Flyway

### Q: What problem does Flyway solve?

Flyway provides database schema versioning.

It ensures every environment has the same schema.

---

### Q: How does Flyway discover migrations?

Flyway scans:

src/main/resources/db/migration

Example:

* V1__create_users_table.sql
* V2__add_new_column.sql

and executes them in version order.

---

### Q: What is flyway_schema_history?

A Flyway metadata table that stores:

* Version
* Description
* Checksum
* Execution timestamp
* Status

---

### Q: Why are Flyway migrations immutable?

When Flyway executes a migration:

1. Reads the file
2. Calculates checksum
3. Stores checksum in flyway_schema_history

If the file changes later:

* New checksum is generated
* Stored checksum no longer matches
* Flyway startup fails

---

### Q: How should schema changes be made?

Never modify:

V1__create_users_table.sql

Instead create:

V2__add_new_column.sql

This preserves database history.

---

### Q: Flyway vs ddl-auto=update?

ddl-auto=update:

* Automatic
* Uncontrolled
* Not production-friendly

Flyway:

* Versioned
* Auditable
* Repeatable
* Production-ready

---

## PostgreSQL Debugging

### Q: Describe the PostgreSQL issue you faced.

Observed:

password authentication failed for user healthcare_user

Investigation:

1. Verified credentials
2. Verified datasource configuration
3. Verified Docker container
4. Created standalone JDBC test
5. Checked port conflicts

Discovered:

* Local PostgreSQL service running
* Docker PostgreSQL running

Stopped local PostgreSQL service.

Then discovered actual root cause:

FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"

Fixed using:

-Duser.timezone=Asia/Kolkata

---

### Q: How did you identify the port conflict?

Used:

netstat -ano | findstr :5432

and

Get-Process

Found multiple PostgreSQL instances listening on port 5432.

---

### Q: Why create a standalone JDBC test?

To isolate:

* Spring Boot
* Flyway
* Hibernate

from database connectivity.

This narrowed the troubleshooting scope.

---

### Q: What was the actual root cause?

Not credentials.

The JVM was sending:

Asia/Calcutta

during PostgreSQL startup negotiation.

PostgreSQL 16 rejected this value.

---

### Q: What lesson did you learn?

The first error message is not always the root cause.

Always isolate layers and verify assumptions.

---

## Spring Boot

### Q: What are Spring Profiles?

Profiles allow environment-specific configuration.

Examples:

* local
* dev
* prod

---

### Q: Why use application-local.yml?

To separate local development configuration from production configuration.

---

## Database Design

### Q: Why separate databases per microservice?

Benefits:

* Service ownership
* Independent schema evolution
* No schema coupling
* Independent deployments

---

### Q: What issue occurred when Auth Service and Claim Service shared a database?

Flyway checksum conflict.

Both services had:

V1

but with different schemas.

Flyway validation failed.

---

### Q: Why does Claim use @Version?

Claims are business records that may be updated concurrently.

@Version enables optimistic locking and prevents lost updates.

---

### Q: Why does User not currently use @Version?

User updates are infrequent.

There is currently no business requirement requiring optimistic locking on User.

---

## Behavioral Question

### Q: Tell me about a challenging issue you solved recently.

Answer Framework:

1. Observed authentication failures.
2. Verified configuration and credentials.
3. Created standalone JDBC test.
4. Discovered multiple PostgreSQL instances.
5. Eliminated port conflict.
6. Found timezone incompatibility.
7. Implemented JVM timezone fix.
8. Successfully restored connectivity.

This demonstrates:

* Structured debugging
* Root cause analysis
* Systematic troubleshooting
* Production-style investigation
