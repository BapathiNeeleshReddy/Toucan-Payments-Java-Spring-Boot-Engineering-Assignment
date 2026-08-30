# AI Usage Disclosure

AI assistance was used during development of this coding exercise.

## Tool used

ChatGPT was used as a coding assistant.

## How it was used

AI was used to help plan the package structure, draft parts of the Spring Boot implementation, suggest validation and exception-handling patterns, and generate ideas for automated tests and documentation.

## What I changed or verified

The generated suggestions were reviewed against the supplied Toucan requirements. The final implementation was kept deliberately simple: a controller handles HTTP requests, a service contains business rules, a repository handles persistence, DTOs represent API input/output, and a global exception handler provides consistent error responses.

The status-transition rule was chosen deliberately: new transactions start as PENDING, then may become COMPLETED or FAILED. COMPLETED and FAILED are treated as final states. The validation rules and these assumptions are documented in the README.

## Verification

The application was checked by running the Maven test suite from a clean build:

```bash
./mvnw clean test
```

The automated tests cover successful creation, validation failure, duplicate IDs, missing transactions, status updates, and customer transaction retrieval.

## Important note

I understand the submitted code and can explain the design, validation rules, status transitions, error handling and tests during the technical discussion.
