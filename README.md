# Toucan Payments - Customer Transaction Service

## Overview

This project implements the four transaction operations requested in the Toucan Engineering Challenge:

1. Create a transaction
2. Get a transaction by Transaction ID
3. Update the status of an existing transaction
4. Get all transactions for a Customer ID

The implementation uses Java 17, Spring Boot, Spring Data JPA, H2 and JUnit, following the supplied starter project.

## Assumptions and validation rules

The challenge asks candidates to define their own validation rules. No candidate-specific variant was provided to me, so the following simple rules are used:

- Transaction ID is required, trimmed, unique, and at most 50 characters.
- Customer ID is required, trimmed, and at most 50 characters.
- Amount is required and must be at least 0.01.
- Supported currencies are INR, USD and EUR.
- Supported transaction types are PAYMENT, REFUND and TRANSFER.
- A newly created transaction must start in PENDING status.
- A transaction can move from PENDING to COMPLETED or FAILED.
- COMPLETED and FAILED are treated as final states and cannot be changed again.

The rules are deliberately small and deterministic so that invalid input is rejected explicitly and the status lifecycle is easy to reason about.

## API endpoints

Base path: `/api`

### 1. Create transaction

`POST /api/transactions`

Example request:

```json
{
  "transactionId": "TX100",
  "customerId": "CUST1",
  "amount": 1250.00,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}
```

Returns `201 Created` when successful, `400 Bad Request` for validation failures, and `409 Conflict` when the Transaction ID already exists.

### 2. Get transaction

`GET /api/transactions/{transactionId}`

Returns `200 OK` when found and `404 Not Found` when the transaction does not exist.

### 3. Update transaction status

`PATCH /api/transactions/{transactionId}/status`

Example request:

```json
{
  "transactionStatus": "COMPLETED"
}
```

Returns `200 OK` for an allowed transition, `400 Bad Request` for an invalid transition, and `404 Not Found` when the transaction does not exist.

### 4. Get customer transactions

`GET /api/customers/{customerId}/transactions`

Returns `200 OK` with a list of matching transactions. If the customer has no transactions, an empty list is returned.

## Project structure

```text
src/main/java/com/example/transactionstarter/
├── controller/   REST API endpoints
├── dto/          Request and response models
├── entity/       JPA entity and enums
├── exception/    Application exceptions and global error handling
├── repository/   Spring Data JPA repository
└── service/      Business logic and status-transition rules
```

## Testing

The test suite uses Spring Boot's test support and MockMvc against the H2 database. It covers:

- Successful transaction creation
- Validation failure
- Duplicate Transaction ID
- Transaction not found
- Successful status update
- Retrieving all transactions for a customer

Run the tests from a clean checkout with:

```bash
./mvnw clean test
```

On Windows:

```bat
mvnw.cmd clean test
```

## Known limitations

- H2 is an embedded in-memory database, so data is not persistent between application restarts.
- The currency and transaction-type lists are intentionally small assumptions because no candidate-specific variant was supplied.
- Authentication, authorization, pagination and production database configuration are outside the scope of this exercise.

## What I would improve with more time

I would add more edge-case tests, database-backed integration testing, API documentation, structured logging, and externalized business rules if the service needed to support more currencies or transaction types.
