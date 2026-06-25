# Task 2 - PasswordEncoder

## What We Built

Created a Spring-managed PasswordEncoder bean using BCrypt.

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Purpose:

* Hash passwords before storing them
* Verify passwords during login

---

## Interview Questions

### Q: Why should passwords never be stored in plain text?

If the database is compromised, attackers immediately gain access to all user credentials.

Passwords should always be stored as hashes.

---

### Q: Difference between Hashing and Encryption?

Hashing:

* One-way operation
* Cannot be reversed
* Used for passwords

Encryption:

* Two-way operation
* Can be decrypted using a key
* Used for sensitive business data

---

### Q: What is BCrypt?

BCrypt is a password hashing algorithm specifically designed for password storage.

Features:

* Built-in salting
* Resistant to rainbow table attacks
* Computationally expensive
* Adaptive work factor

---

### Q: What is Salt?

Salt is random data added to a password before hashing.

Benefits:

* Same password generates different hashes
* Prevents rainbow table attacks

BCrypt automatically handles salting.

---

### Q: Why use Spring's PasswordEncoder abstraction?

Benefits:

* Decouples application code from BCrypt
* Allows future algorithm changes
* Easier testing and maintenance

Example:

```java
PasswordEncoder passwordEncoder;
```

instead of:

```java
new BCryptPasswordEncoder();
```

throughout the application.

---

## Project Usage

Used in:

```text
POST /api/v1/auth/register
```

Flow:

1. Receive password from RegisterRequest
2. Hash using BCrypt
3. Store hash in users table
4. Compare hashes during login

```
```
