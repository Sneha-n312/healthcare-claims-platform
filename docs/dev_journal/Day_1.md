# Day 1 - Project Foundation Setup

## Objective

1. Set up local development environment and create the initial project structure for the Healthcare Claims Platform.
2. Set up Docker services

---

## GitHub Setup

Created a new GitHub repository.

Connected local machine to GitHub using Git Bash.

Verified Git installation:

```bash
git --version
```

Configured Git user details:

```bash
git config --global user.name "<github-username>"
git config --global user.email "<github-email>"
```

Generated SSH key:

```bash
ssh-keygen -t ed25519 -C "<github-email>"
```

Started SSH agent:

```bash
eval "$(ssh-agent -s)"
```

Added SSH key:

```bash
ssh-add ~/.ssh/id_ed25519
```

Copied public key:

```bash
cat ~/.ssh/id_ed25519.pub
```

Added public key to GitHub.

Verified GitHub connectivity:

```bash
ssh -T git@github.com
```

Expected output:

```text
Hi <username>! You've successfully authenticated.
```

---

## Repository Setup

Cloned repository:

```bash
git clone <repository-url>
```

Created initial project structure:

```text
healthcare-claims-platform/
│
├── infrastructure/
├── services/
│   ├── auth-service/
│   └── claims-service/
│
├── docs/
│   ├── journal/
│   └── adr/
│
└── docker-compose.yml
```

Initial commit:

```bash
git add .
git commit -m "chore: initial project structure"
git push
```


## Docker Setup

Installed Docker Desktop for Windows.

Verified Docker installation:

```bash
docker --version
```

Verified Docker Compose:

```bash
docker compose version
```

Started Docker Desktop and ensured Docker Engine was running.

Verified Docker functionality:

```bash
docker run hello-world
```

Expected output:

```text
Hello from Docker!
```

---

## Local Infrastructure Setup

Created:

```text
docker-compose.yml
```

Services configured:

* PostgreSQL 16
* Kafka
* Zookeeper
* Kafka UI


## Infrastructure Verification

Started infrastructure:

```bash
docker compose up -d
```

Verified containers:

```bash
docker ps
```

Expected containers:

```text
healthcare-postgres
healthcare-zookeeper
healthcare-kafka
healthcare-kafka-ui
```

Verified PostgreSQL:

```bash
docker exec -it healthcare-postgres psql -U healthcare_user -d healthcare_claims
```

Verified Kafka UI:

```text
http://localhost:8090
```

Stopped infrastructure:

```bash
docker compose down
```

Restarted infrastructure:

```bash
docker compose up -d
```

---

## Learning

Docker Compose allows the entire local development environment to be started using a single command.

Benefits observed:

* Consistent local environment
* No manual PostgreSQL installation required
* No manual Kafka installation required
* Easy environment reset and recreation
* Infrastructure version controlled alongside application code

