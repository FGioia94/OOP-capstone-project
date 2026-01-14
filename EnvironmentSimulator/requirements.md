# Environment Simulator — Requirements Specification

## Project Overview

**Goal:** Develop a well-architected, secure, and maintainable Java SE application showcasing object-oriented design, design patterns, and core Java technologies through an interactive ecosystem simulation.

## Introduction and Brief

The project will implement a modular ecosystem simulation where environments, resources, 
and animal actors interact through movement, feeding, reproduction, and combat. 
Users will be able to add resources such as water and grass, introduce herbivores and carnivores 
(individually or in packs), and observe how they behave according to species rules. 
The simulation will support saving and loading the world state in multiple formats (JSON and binary) and will include 
a command-driven interface with extensible handlers.

---

# Functional Requirements

## REQ‑F‑001 — Resource Management

### REQ‑F‑001.1
- The App will allow the user to add different resources such as Water and Grass.

### REQ‑F‑001.2
- Herbivores will consume Grass to survive.  
- Carnivores will consume Herbivores to survive.  
- Both Herbivores and Carnivores will consume Water to survive.
- If they are not eating, both herbivores and carnivores lose life points.
- When life points reach 0, the animal dies.

---

## REQ‑F‑002 — Entity Management

### REQ‑F‑002.1
- The App will allow the user to add members of different classes, specifically Herbivores and Carnivores.

### REQ‑F‑002.2
- Carnivores will be addable in packs or as separate entities.
- If two Carnivores are not in the same pack, they will potentially damage each other.

---

## REQ‑F‑003 — Movement and Interaction

### REQ‑F‑003.1
- Both Carnivores and Herbivores will move every game‑loop cycle by a random amount multiplied by their base speed.

### REQ‑F‑003.2
- If a Carnivore and a Herbivore get close enough, the Carnivore will attack the Herbivore.  
- The damage will be a random amount multiplied by the Carnivore’s base attack.

### REQ‑F‑003.3
- If Carnivores or Herbivores of different genders get close, they will reproduce, generating a random number of newborns.  
- The two parents and the newborns will not move for a certain amount of time after reproduction.

---

## REQ‑F‑004 — Persistence

### REQ‑F‑004.1
- The user will be able to save the game state.  
- The serialized data will be stored in a JSON file.

### REQ‑F‑004.2
- The user will be able to load a previously saved game from a JSON or Binary file.
- Loading will support multiple strategies for different file formats.

### REQ‑F‑004.3
- The system will validate save file integrity before loading.
- Corrupted or incompatible files will trigger appropriate error handling with logging.

---

## REQ‑F‑005 — Command Processing

### REQ‑F‑005.1
- The App will support command-line commands processed through an extensible handler system.
- Commands will include: `spawn`, `help`, `save`, `load`, and others.

### REQ‑F‑005.2
- Invalid commands will be handled gracefully without exposing stack traces.
- A help command will display available commands and usage.

---

## REQ‑F‑006 — Map Building and Validation

### REQ‑F‑006.1
- The App will provide an interface for constructing game maps.
- Map builder will support setting dimensions, adding entities, and positioning elements.

### REQ‑F‑006.2
- Maps will be validated before use to ensure structural integrity.
- Validation will include checking boundaries, entity placement, and resource distribution.

---

## REQ‑F‑007 — State Management

### REQ‑F‑007.1
- The App will support capturing simulation state as snapshots.
- Snapshots will enable undo/redo functionality and save/load operations.

### REQ‑F‑007.2
- Game snapshots will be immutable to prevent state corruption.

---

# Non‑Functional Requirements

## REQ‑NF‑001 — Performance
- The simulation will run at a stable tick rate optimized for responsiveness.
- Entity operations will use efficient collections and algorithms.

## REQ‑NF‑002 — Logging
- The App will use Log4j2 for structured logging at appropriate levels (INFO, DEBUG, ERROR).
- All major events (movement, attacks, reproduction, save/load, errors) will be logged.
- Log output will be configurable via `log4j2.xml`.
- No sensitive information will be logged.

## REQ‑NF‑003 — Security and Exception Handling
- **Input Sanitization:** All user inputs must be validated and sanitized.
- **Exception Shielding:** Low-level exceptions must be wrapped in custom exceptions.
- **No Stack Traces:** Stack traces will never be displayed to end users.
- **No Hardcoded Secrets:** Configuration must be externalized; no credentials in code.
- **Controlled Propagation:** Exceptions must be logged with context and converted to user-friendly messages.

## REQ‑NF‑004 — Modularity and Maintainability
- The codebase must follow SOLID principles.
- Design patterns must be applied appropriately to reduce coupling.
- Clear separation of concerns between layers (command handling, business logic, persistence).

## REQ‑NF‑005 — Testability
- Code must be designed for testability with dependency injection where applicable.
- Test coverage must include:
  - Unit tests for individual components
  - Integration tests for end-to-end scenarios
  - Mock-based tests for isolation
- Test suite must run via build system and produce reports.

## REQ‑NF‑006 — Persistence Integrity
- Save files will use consistent serialization formats (JSON, Binary).
- File format versions will be tracked to maintain backward compatibility.
- Checksums or validation mechanisms will detect corruption.

## REQ‑NF‑007 — Error Recovery
- The App will handle errors gracefully and provide actionable feedback.
- Failed operations will not leave the system in an inconsistent state.
- Users will receive clear error messages without technical jargon.

---

# System Requirements

## REQ‑SYS‑001 — Platform
- The App will run as a Java SE 11+ console application.
- Compatible with Windows, macOS, and Linux.

## REQ‑SYS‑002 — Build System
- Gradle will be used for dependency management and build automation.
- Project structure will follow standard Maven/Gradle conventions.

## REQ‑SYS‑003 — Dependencies
- **Log4j2** — Structured logging framework
- **JUnit 5** — Unit testing framework
- **Mockito** — Mocking framework for isolated testing
- **Gson or Jackson** — JSON serialization/deserialization
- No external UI framework required (console-based)

## REQ‑SYS‑004 — Directory Structure
- `src/main/java/` — Application source code
- `src/main/resources/` — Configuration files
- `src/test/java/` — Test source code
- `src/test/resources/` — Test resources
- `src/data/saved/` — Save file storage directory
- `build/` — Build outputs
- `uml/` — UML diagrams and design documentation

---

# Required Technologies

## REQ-TECH-001 — Collections Framework
- The App must use Java Collections Framework extensively.
- Appropriate collection types (`List`, `Set`, `Map`) must be used based on requirements.
- Collections must be type-safe using generics.

## REQ-TECH-002 — Generics
- The App must implement generic classes, interfaces, and methods.
- Type parameters must ensure compile-time type safety.
- Generics must be used in factory implementations and collections.

## REQ-TECH-003 — Java I/O
- The App must implement file-based persistence using Java I/O.
- Multiple serialization formats must be supported.
- File operations must include proper exception handling.
- Path manipulation must use `java.nio.file` package.

## REQ-TECH-004 — Logging
- The App must implement structured logging using a logging framework.
- All major events and errors must be logged.
- Log levels must be used appropriately (INFO, DEBUG, ERROR).
- Logging configuration must be externalizable.

## REQ-TECH-005 — JUnit Testing
- The App must include comprehensive JUnit test suite.
- Unit tests must validate individual component behavior.
- Integration tests must validate end-to-end scenarios.
- Tests must be automated and runnable via build system.

## REQ-TECH-006 — Mockito (Optional)
- The App may use Mockito for mock-based testing.
- Mocks should isolate components during testing.
- Mock verification should validate interactions.

## REQ-TECH-007 — Custom Annotations (Optional)
- The App may implement custom annotations.
- Annotations should provide metadata-driven configuration.
- Annotation processing should be documented.

## REQ-TECH-008 — Stream API & Lambdas (Optional)
- The App may use Stream API and lambda expressions.
- Functional programming constructs should improve code clarity.
- Streams should be used for data transformations and filtering.

---

# Security Requirements

## REQ-SEC-001 — Input Validation
- All user inputs must be validated before processing.
- Command parameters must be sanitized.
- File paths must be validated to prevent directory traversal.
- Type checking and bounds validation must be performed.

## REQ-SEC-002 — Exception Shielding
- Technical exception details must never be visible to end users.
- All exceptions must be logged with full context.
- User-facing error messages must be clear and actionable.
- Stack traces must only appear in logs, never in user output.

## REQ-SEC-003 — Secure File Operations
- File operations must be restricted to designated directories.
- Path traversal attacks must be prevented.
- File existence and permissions must be checked before operations.

## REQ-SEC-004 — Logging Security
- No sensitive information (credentials, personal data) in logs.
- Log levels must be appropriate for security events.
- Audit trail must be maintained for critical operations.

## REQ-SEC-005 — Configuration Security
- No hardcoded credentials or secrets in source code.
- Configuration must be externalized to files.
- Sensitive configuration must support environment variables.

---

# Testing Requirements

## REQ-TEST-001 — Unit Testing
- Individual methods and classes must have unit tests.
- Unit tests must run in isolation without external dependencies.
- Code coverage target: >80% for core logic.
- Fast execution time (<5 seconds for entire unit test suite).

## REQ-TEST-002 — Integration Testing
- End-to-end scenarios must be validated with integration tests.
- Component interactions must be tested.
- Critical workflows (spawn, save, load) must have integration tests.

## REQ-TEST-003 — Mock-Based Testing
- Components with external dependencies must have mock-based tests.
- Mocks must isolate the component under test.
- Test scenarios must include error conditions.

## REQ-TEST-004 — Test Organization
- Tests must be organized by feature or pattern.
- Test classes must follow naming conventions.
- Test methods must have descriptive names.

## REQ-TEST-005 — Test Reporting
- Build system must generate test reports.
- Test results must be available in both HTML and XML formats.
- Failed tests must provide clear diagnostic information.

---

# Known Scope Limitations

## Current Scope
- Console-based interface only
- File-based persistence only
- Local simulation only

## Out of Scope
- GUI implementation
- Network/multiplayer functionality
- Database persistence
- Plugin system for dynamic species loading

