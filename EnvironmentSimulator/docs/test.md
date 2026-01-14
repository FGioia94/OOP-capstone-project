````markdown
# Testing Suite — Overview

This document summarizes the project's testing strategy, how to run tests, and where reports are located.

## Test Types

- Unit Tests (JUnit 5)
  - Fast, isolated tests for individual classes and methods.
  - Located under `src/test/java` by feature/package (e.g., `factoryMethod`, `builder`, `chainOfResponsibility`).
  - Examples: `AnimalFactoryTest`, `MapBuilderTest`, `ExceptionShieldingTest`.

- Integration Tests
  - End-to-end validation of critical workflows (spawn, save, load, map movement).
  - Located under `src/test/java/integration` (or similarly named packages).
  - Examples: `AnimalCreationIntegrationTest`, `MapMovementIntegrationTest`.

- Mock-Based Tests (Mockito)
  - Used to isolate components and verify interactions without performing real I/O.
  - Examples: `AnimalRepositoryTest`, `MapBuilderMockitoTest`.

## Coverage & Goals

- Target: >80% coverage for core logic (business rules, factories, repository behavior).
- Tests emphasize determinism and avoid flaky concurrency by isolating parallel parts where necessary.

## How to Run Tests

From project root using Gradle:

Windows (PowerShell / CMD):

```powershell
.\gradlew.bat test
```

macOS / Linux:

```bash
./gradlew test
```

To run a single test class from Gradle, use:

```bash
./gradlew test --tests "fully.qualified.TestClassName"
```

## Test Reports

- HTML reports: `build/reports/tests/test/index.html`
- XML results: `build/test-results/test/`

Open the HTML report in a browser to review failures and stack traces (stack traces are logged but the application shields them from end users).

## Best Practices Followed

- Dependency injection and constructor injection to enable mocking.
- Avoid shared mutable state in unit tests; use test fixtures and mocks for complex dependencies.
- Use `@DisplayName` and descriptive test method names for clarity in reports.
- Keep unit tests fast (<5s total where practical) and integration tests separated.

## How Tests Relate to Requirements

- Tests validate required design patterns (Factory, Composite, Iterator, Exception Shielding) via behavioral tests and repository tests.
- Persistence tests cover save/load strategies (JSON/Binary) and snapshot integrity.
- Security-related tests ensure invalid inputs are handled and stack traces are not shown to users.

````