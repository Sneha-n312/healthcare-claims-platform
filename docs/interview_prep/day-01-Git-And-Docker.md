# Day 1 - Interview Preparation Notes

## Git & GitHub

### Q: Difference between HTTPS and SSH for Git?

HTTPS requires username/token authentication.

SSH uses public/private key authentication.

Advantages of SSH:

* More secure
* No repeated login prompts
* Preferred for developer workflows

---

### Q: What happens when you run git clone?

Git:

1. Downloads repository objects
2. Creates local repository
3. Creates working directory
4. Creates remote named "origin"

---

### Q: Difference between git fetch and git pull?

git fetch:

* Downloads latest changes
* Does not merge

git pull:

* Downloads latest changes
* Automatically merges into current branch

---

## Docker

### Q: What is Docker?

Docker is a containerization platform that packages:

* Application
* Dependencies
* Runtime
* Configuration

into portable containers.

---

### Q: Difference between a Container and a Virtual Machine?

Virtual Machine:

* Has its own operating system
* Runs on a hypervisor
* Heavyweight

Container:

* Shares host OS kernel
* Lightweight
* Faster startup

---

### Q: Difference between Image and Container?

Image:

* Read-only blueprint/template

Container:

* Running instance of an image

Example:

* postgres:16 → Image
* healthcare-postgres → Container

---

### Q: Why use Docker Compose?

Docker Compose allows infrastructure to be defined declaratively.

Instead of manually starting:

* PostgreSQL
* Kafka
* Zookeeper
* Kafka UI

everything can be started using:

docker compose up -d

---

### Q: What is a Docker Volume?

A Docker Volume stores data outside the container lifecycle.

Without a volume:

* Container deleted
* Data lost

With a volume:

* Data survives container restarts and recreation

---

### Q: Why use Docker for local development?

Benefits:

* Consistent environment
* Isolation
* Easy onboarding
* Infrastructure as code
* Easy recreation of environments

---

## Kafka

### Q: Why introduce Kafka from the beginning?

To support future event-driven workflows such as:

* ClaimSubmitted
* ClaimApproved
* ClaimRejected

---

### Q: What is Kafka?

Kafka is a distributed event streaming platform used for:

* Messaging
* Event processing
* Asynchronous communication

Benefits:

* High throughput
* Durability
* Scalability

---

### Q: What is a Kafka Topic?

A Topic is a logical stream of events.

Example:

claim-events

---

### Q: What is a Partition?

A Topic is divided into partitions.

Benefits:

* Parallel processing
* Horizontal scalability
* Higher throughput

---

## Microservices

### Q: Why choose Microservices instead of a Monolith?

Benefits:

* Independent deployments
* Better scalability
* Service ownership
* Technology flexibility

Example:

* Auth Service
* Claims Service

can evolve independently.

---

### Q: What are the challenges of Microservices?

* Distributed transactions
* Network latency
* Monitoring and observability
* Service-to-service communication
* Data consistency
