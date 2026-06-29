# MoneyLedger

A production-inspired backend service built using Java and Spring Boot that simulates secure money transfers between bank accounts.

The project focuses on backend engineering concepts used in fintech systems rather than simply exposing CRUD APIs.

---

# Features

- Secure account-to-account money transfer
- Double-entry ledger accounting
- ACID database transactions
- Idempotent payment processing
- Redis-based duplicate request protection
- PostgreSQL persistence
- Kafka event publishing
- Docker Compose infrastructure
- Global exception handling
- Input validation
- Layered architecture

---

# Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Language |
| Spring Boot | Backend Framework |
| Spring Data JPA | Database ORM |
| PostgreSQL | Primary Database |
| Redis | Idempotency Cache |
| Apache Kafka | Event Streaming |
| Docker Compose | Infrastructure |
| Maven | Dependency Management |
| Lombok | Boilerplate Reduction |
| Spring Validation | Request Validation |

---

# Architecture

```
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ├──────────────► Redis
   │               (Idempotency)
   │
   ▼
PostgreSQL
   │
   ▼
Ledger Entries
   │
   ▼
Kafka Event
```

---

# Money Transfer Flow

1. Client sends a transfer request.
2. Redis checks whether the idempotency key already exists.
3. If duplicate, the previous result is returned.
4. Sender and receiver accounts are loaded.
5. Balance validation is performed.
6. Money is transferred inside a single database transaction.
7. Transaction record is created.
8. Debit and Credit ledger entries are created.
9. Redis stores the completed idempotency key.
10. Kafka publishes a transaction event.

---

# Project Structure

```
src
 ├── controller
 ├── dto
 ├── entity
 ├── enums
 ├── exceptions
 ├── repository
 ├── service
 ├── config
 └── MoneyledgerApplication
```

---

# Database

Current entities:

- Account
- Transaction
- LedgerEntry
- IdempotencyKey

---

# Running the Project

## Clone

```bash
git clone https://github.com/<your-username>/moneyledger.git
```

---

## Start Infrastructure

```bash
docker compose up -d
```

This starts

- PostgreSQL
- Redis
- Kafka
- ZooKeeper

---

## Run

```bash
mvn spring-boot:run
```

---

# Sample Request

```
POST /api/v1/transactions
```

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 100,
  "idempotencyKey": "tx001"
}
```

---

# Sample Response

```json
Transfer Successful
```

Submitting the same request again with the same idempotency key returns:

```json
Transaction already processed
```

---

# Current Functionality

- Account Management
- Money Transfer
- Double Entry Ledger
- Transaction Persistence
- Redis Idempotency
- Kafka Producer
- Exception Handling
- Validation

---

# Planned Enhancements

- Transaction History API
- Account Statement API
- Kafka Consumer
- JWT Authentication
- Prometheus Metrics
- Grafana Dashboard
- OpenAPI / Swagger
- Unit Testing
- Integration Testing
- Flyway Database Migration
- CI/CD Pipeline
- Kubernetes Deployment

---

# Design Principles

- Layered Architecture
- Separation of Concerns
- Constructor Dependency Injection
- Transactional Consistency
- Event-Driven Design
- Idempotent API Design
- Production-Oriented Development

---

# Author

**Qudsia Siddiqui**

Backend Developer

Java • Spring Boot • PostgreSQL • Redis • Kafka • Docker
