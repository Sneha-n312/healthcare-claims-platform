# Day 3 - JPA Fundamentals Interview Notes

## JPA & Hibernate

### Q: What is JPA?

JPA (Jakarta Persistence API) is a specification for Object Relational Mapping (ORM) in Java.

It defines how Java objects are mapped to database tables.

JPA itself is only a specification.

Common implementations:

- Hibernate
- EclipseLink

In Spring Boot, Hibernate is the default JPA implementation.

---

### Q: What is Hibernate?

Hibernate is an ORM framework and the most popular implementation of JPA.

Responsibilities:

- SQL generation
- Entity lifecycle management
- Dirty checking
- Caching
- Transaction integration

---

### Q: Difference between JPA and Hibernate?

JPA:
- Specification
- Defines contracts/interfaces

Hibernate:
- Implementation
- Executes actual database operations

Example:

```java
@Entity
public class User {
}
```

JPA defines @Entity.

Hibernate implements its behavior.

---

### Q: What is Spring Data JPA?

Spring Data JPA reduces boilerplate code by generating repository implementations automatically.

Instead of:

```java
public class UserRepositoryImpl {
    // JDBC code
}
```

We simply write:

```java
public interface UserRepository extends JpaRepository<User, UUID> {
}
```

---

## JpaRepository

### Q: What is JpaRepository?

JpaRepository provides CRUD operations and JPA-specific functionality for entities.

Example:

```java
public interface UserRepository
        extends JpaRepository<User, UUID> {
}
```

---

### Q: Who implements JpaRepository?

Spring Data JPA generates an implementation at runtime.

Actual implementation:

```text
SimpleJpaRepository
```

You never write the implementation manually.

---

### Q: JpaRepository hierarchy?

```text
Repository
    ↓
CrudRepository
    ↓
PagingAndSortingRepository
    ↓
JpaRepository
```

---

### Q: What methods are available in JpaRepository?

Common methods:

```java
save()
findById()
findAll()
delete()
deleteById()
count()
existsById()
```

---

### Q: What happens when save() is called?

Spring delegates to:

```java
EntityManager
```

Internally:

```java
persist()
or
merge()
```

depending on entity state.

---

## Query Derivation

### Q: What is Query Method Derivation?

Spring parses repository method names and generates queries automatically.

Example:

```java
findByUsername(String username)
```

Generated SQL:

```sql
select *
from users
where username = ?
```

---

### Q: What query does existsByUsername generate?

Method:

```java
existsByUsername(String username)
```

Equivalent SQL:

```sql
select count(*)
from users
where username = ?
```

Returns:

```java
true
```

or

```java
false
```

---

### Q: Why use Optional?

Instead of:

```java
User findByUsername(...)
```

use:

```java
Optional<User>
```

Reason:

The user may not exist.

Example:

```java
userRepository
        .findByUsername(username)
        .orElseThrow(...)
```

---

## EntityManager

### Q: What is EntityManager?

EntityManager is the core JPA interface responsible for:

- Persisting entities
- Updating entities
- Removing entities
- Querying entities

JpaRepository internally uses EntityManager.

---

### Q: What is persist()?

Used for new entities.

Example:

```java
entityManager.persist(user);
```

Results in:

```sql
INSERT
```

---

### Q: What is merge()?

Used for detached entities.

Example:

```java
entityManager.merge(user);
```

Results in:

```sql
UPDATE
```

---

### Q: Difference between persist() and merge()?

persist():

- New entity
- INSERT
- Entity becomes managed

merge():

- Existing detached entity
- UPDATE
- Returns managed instance

---

## Entity Lifecycle

### Q: What are JPA Entity States?

Four states:

### Transient

Object exists only in memory.

```java
User user = new User();
```

Not managed.

---

### Managed

Entity is tracked by Hibernate.

Changes are automatically synchronized.

---

### Detached

Previously managed but no longer attached to persistence context.

---

### Removed

Marked for deletion.

```java
entityManager.remove(user);
```

---

## Persistence Context

### Q: What is Persistence Context?

A cache of managed entities maintained by EntityManager.

Benefits:

- Prevents duplicate database queries
- Tracks changes automatically

---

### Q: What is Dirty Checking?

Hibernate automatically detects changes to managed entities.

Example:

```java
user.setUsername("newName");
```

Hibernate generates UPDATE during transaction commit.

No explicit save required.

---

## Transactions

### Q: Why use @Transactional?

Ensures operations execute as a single unit of work.

Either:

```text
All succeed
```

or

```text
All rollback
```

---

### Q: What happens without @Transactional?

Multiple database operations may partially succeed and leave inconsistent data.

---

### Q: What is ACID?

A - Atomicity

All operations succeed or rollback.

C - Consistency

Database remains valid.

I - Isolation

Concurrent transactions don't interfere.

D - Durability

Committed data survives failures.

---

## Fetching

### Q: Difference between Eager and Lazy Loading?

Eager:

```java
FetchType.EAGER
```

Loads related entities immediately.

---

Lazy:

```java
FetchType.LAZY
```

Loads only when accessed.

Preferred in most cases.

---

### Q: What is N+1 Query Problem?

Example:

```java
findAllClaims()
```

returns 100 claims.

Then accessing:

```java
claim.getUser()
```

fires 100 additional queries.

Result:

```text
1 query + 100 queries
```

Performance issue.

---

## UUID

### Q: Why use UUID instead of Long?

Benefits:

- Globally unique
- Safer in distributed systems
- Harder to guess
- Better for microservices

---

### Q: Drawback of UUID?

- Larger storage
- Larger indexes
- Slightly slower than numeric IDs

---

## Optimistic Locking

### Q: What does @Version do?

Enables optimistic locking.

Example:

```java
@Version
private Long version;
```

---

### Q: Why is @Version useful for Claims?

Scenario:

```text
Adjuster A loads claim
Adjuster B loads claim

A updates
B updates
```

Without versioning:

```text
Last update wins
```

Data loss possible.

With versioning:

```text
OptimisticLockException
```

Prevents lost updates.

---

## Senior-Level Questions

### Q: Why use Spring Data JPA instead of JDBC?

Advantages:

- Less boilerplate
- Entity mapping
- Transaction management
- Repository abstraction
- Faster development

---

### Q: When would you choose JDBC over JPA?

For:

- Extremely high-performance workloads
- Complex SQL-heavy applications
- Bulk processing

where full ORM becomes inefficient.

---

### Q: Why should findAll() be avoided on large tables?

Example:

```java
userRepository.findAll();
```

Loads entire table into memory.

For millions of rows:

- High memory usage
- Slow response times

Use:

```java
Page<User>
Slice<User>
```

instead.

---

## Project Connection

Current Project Usage:

```java
UserRepository
```

Methods:

```java
findByUsername()
existsByUsername()
save()
```

Used for:

- User Registration
- User Login
- Username Validation

These are the first repository methods that will power the Auth Service APIs.

---

### Senior Interview Answer

Q: How does Spring implement UserRepository when you never wrote an implementation?

Answer:

Spring Data JPA creates a proxy implementation at runtime. The generated implementation is based on SimpleJpaRepository, which internally delegates persistence operations to the JPA EntityManager. Query methods such as findByUsername are generated through method-name parsing and converted into JPQL/SQL automatically.