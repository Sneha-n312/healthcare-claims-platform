# Day 2 - Auth Service and Claims Service Bootstrap


## Goal

Bootstrap Auth Service and Claims Service with PostgreSQL, Flyway, Swagger and Actuator.

---

## Auth Service

Generated Spring Boot application using Spring Initializr.

Dependencies:

* Spring Web
* Spring Data JPA
* PostgreSQL Driver
* Flyway
* Spring Security
* Validation
* Actuator
* Lombok
* OpenAPI

Java Version:

```text
Java 21
Spring Boot 3.5.15
```

---

## Initial Flyway Setup

Created migration:

```text
V1__create_users_table.sql
```

---

## Major Issue #1 PostgreSQL Authentication Issue

Application startup failed with:

```text
password authentication failed for user "healthcare_user"
```

### Investigation Performed

Verified Docker container:

```bash
docker ps
```

Connected to PostgreSQL:

```bash
docker exec -it healthcare-postgres psql -U healthcare_user -d healthcare_claims
```

Verified user:

```sql
\du
```

Verified authentication config:

```bash
docker exec -it healthcare-postgres cat /var/lib/postgresql/data/pg_hba.conf
```

```
Created standalone JDBC test to isolate Spring Boot from database connectivity
```

Verified connectivity:

```powershell
Test-NetConnection localhost -Port 5432
```

Checked port conflicts:

```powershell
netstat -ano | findstr :5432
```

While troubleshooting, discovered that two PostgreSQL instances were running on the machine:

Local Windows PostgreSQL service
Docker PostgreSQL container

The local PostgreSQL service was listening on port 5432 alongside Docker.

## Resolution
Stopped the local PostgreSQL service. Verified that only Docker was listening on port 5432. After stopping the local PostgreSQL instance, the original authentication error disappeared.

New Error Discovered
---
## Major Issue #2  Postgres Timezone setting
Once JDBC connected to the correct PostgreSQL instance, a different error appeared:

## Root Cause

Created standalone JDBC test:

```java
DriverManager.getConnection(...)
```

Observed error:

```text
FATAL: invalid value for parameter "TimeZone": "Asia/Calcutta"
```

PostgreSQL 16 rejected timezone value supplied by JVM.

---

## Resolution

Added JVM option:

```text
-Duser.timezone=Asia/Kolkata
```

IntelliJ:

```text
Run Configurations
→ VM Options
→ -Duser.timezone=Asia/Kolkata
```

Verified JDBC connection:

```text
CONNECTED SUCCESSFULLY
```

Auth Service startup successful.

---

## Claims Service

Generated second Spring Boot application.

Dependencies identical to Auth Service.

Created migration:

```text
V1__create_claims_table.sql
```

---

## Flyway Version Conflict

Claim Service startup failed.

Observed:

```text
FlywayValidateException
Migration checksum mismatch
```

### Root Cause

Auth Service and Claims Service were using same database:

```text
healthcare_claims
```

Both services contained migration:

```text
V1
```

Flyway history conflicted.

---

## Architecture Change

Created separate databases.

Auth Service:

```text
healthcare_auth
```

Claims Service:

```text
healthcare_claims
```

Created database:

```sql
CREATE DATABASE healthcare_auth;
```

Updated datasource configurations.

---

## Verification

Auth database:

```sql
\c healthcare_auth
\dt
```

Tables:

* users
* flyway_schema_history

Claims database:

```sql
\c healthcare_claims
\dt
```

Tables:

* claims
* flyway_schema_history

---

##After both services were up:
checked swagger at : http://localhost:<service_port>/swagger-ui.html
checked actuator at:  http://localhost:<service_port>/actuator/health

## End of Day Status

Working:

* Docker Compose
* PostgreSQL
* Auth Service
* Claims Service
* Flyway
* Swagger
* Actuator

##Important Lessons:

1. Never share databases between microservices.
2. Flyway migrations are immutable once executed.
3. Verify JDBC connectivity separately from Spring Boot.
4. Explicitly configure JVM timezone for local development.
