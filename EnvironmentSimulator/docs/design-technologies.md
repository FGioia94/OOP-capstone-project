````markdown
# Advanced Technologies — Usage Guide

This document explains how and where the following advanced technologies are used in the project:

- Multithreading
- Stream API & Lambdas
- Reflection
- Inversion of Control (IoC) / Dependency Injection
- Custom Annotations
- Mockito (mock-based testing)

Each section lists the purpose, concrete locations (files/packages), and short notes about usage.

---

## Multithreading

- Purpose: Improve throughput for CPU-bound or batch operations and keep observer notifications single-threaded.
- Where used:
  - `template/Game/GameLoop.java`
    - `processHunger()` uses `parallelStream()` and a `ConcurrentLinkedQueue` to parallelize HP updates and collect events.
    - `checkLifePoints()` uses `parallelStream()` to detect dead animals in parallel, then sequentially removes them and notifies observers.
  - Other places: Parallel operations are used only on safely shared data (read-mostly or with synchronized/atomic actions).
- Notes:
  - Observers are notified sequentially to avoid race conditions.
  - Thread-safe collections (`ConcurrentLinkedQueue`, synchronized lists) are used where needed.

### Other multithreading approaches considered

- **Full multi-threaded game loop (worker pool):** partition entities across worker threads and run movement/interaction/AI in parallel each tick. This can increase throughput but requires careful synchronization of shared state (map, repository) and deterministic tick ordering.
- **Per-entity threads:** give each entity its own thread for behavior. This is simple conceptually but scales poorly (thread overhead), complicates lifecycle management, and makes testing and determinism difficult.
- **Dedicated I/O threads / async persistence:** offload save/load and heavy I/O to separate threads or executor services to avoid blocking the main loop.

### Why those alternatives were not chosen

- **Complexity vs benefit**
- **Determinism & testability**
- **Shared mutable state:** Core data structures (`AnimalRepository`, `MapBuilder`) are heavily shared; converting them to fully concurrent structures would require major refactors and introduce potential contention.
- **Resource constraints:** Per-entity threads are resource-intensive and not appropriate for scalable simulations.
- **Project scope & time:** Given time and scope constraints, selective parallelism (parallel streams for independent batch ops) delivers meaningful performance improvements while minimizing risk.

Selected approach: apply limited, controlled parallelism (parallel streams + thread-safe queues) for independent batch tasks, and keep the main simulation loop and observer notifications single-threaded to preserve determinism and simplify testing.

---

## Stream API & Lambdas

- Purpose: Concise processing of collections, parallelism, and functional-style transformations.
- Where used:
  - `template/Game/GameLoop.java` — `parallelStream()` for performance in `processHunger()` and `checkLifePoints()`.
  - Various test and utility code use streams and lambdas for filtering and mapping collections (e.g., building lists of IDs, filtering animals by type).
- Notes:
  - Streams are chosen when a pipeline of operations improves readability and when parallelism is desirable.
  - Care is taken to avoid shared mutable state inside lambda bodies.

---

## Reflection

- Purpose: Runtime introspection, dynamic loading of factories, and annotation processing.
- Where used:
  - `factoryMethod/AnimalFactory/AnimalInspector.java`
    - Uses `Class.forName()`, `getDeclaredMethods()`, `getDeclaredFields()`, and `Method.invoke()` to inspect and interact with `Animal`/`AnimalFactory` classes.
    - Dynamically loads factory classes by name (`loadFactoryByName`).
  - `annotations/AdminAnnotationChecker.java`
    - Uses reflection to check for `@AdminOnly` on command handlers before adding them to the chain.
  - `chainOfResponsibility/commandHandler/InspectCommandHandler.java`
    - Demonstrates dynamic method invocation via `AnimalInspector`.
- Notes:
  - Reflection usage is limited to admin/inspection features and factory discovery; it's isolated and logged.

---

## Inversion of Control (IoC) / Dependency Injection

- Purpose: Decouple components, make code testable, and allow swapping implementations (constructor injection pattern).
- Where used:
  - `template/Game/GameLoop.java`
    - `GameLoop` is constructed with `MapBuilder` and `AnimalRepository` instances via constructor injection.
  - `template/Game/GameAdmin.java` / `template/Game/GameDefault.java`
    - Create and inject concrete dependencies when starting the game.
  - `chainOfResponsibility/commandHandler/CommandChainBuilder.java`
    - Handlers can be added dynamically to the chain; the builder composes the chain (loose runtime coupling).
- Notes:
  - Constructor injection is favored for main components; the design makes unit testing straightforward by passing mocks.

---

## Custom Annotations

- Purpose: Declarative metadata for validation and access control; small annotation-driven utilities.
- Where used:
  - `annotations/AdminOnly.java`
    - Marks command handlers that should only be enabled in admin mode.
  - `annotations/ValidPosition.java` and `annotations/PositionValidator.java`
    - Used to mark and validate `Position` parameters (e.g., `Animal.setPosition(@ValidPosition Position)`).
  - `annotations/AdminAnnotationChecker.java`
    - Inspects `@AdminOnly` and decides which handlers to register.
- Notes:
  - Annotation processing is runtime-based (small utility validators/checkers), not compile-time processing.
  - Validators centralize input checks and keep method code cleaner.

---

## Mockito (Mock-Based Testing)

- Purpose: Unit-test components in isolation by mocking collaborators and verifying interactions.
- Where used:
  - `src/test/java/factoryMethod/AnimalFactory/AnimalRepositoryTest.java` — uses Mockito to mock animals and verify repository interactions.
  - `src/test/java/builder/MapBuilder/MapBuilderMockitoTest.java` — demonstrations of mocking for builder tests.
  - Other unit tests make use of Mockito for isolating dependencies when testing logic that interacts with external I/O or complex objects.
- Notes:
  - Mockito keeps unit tests fast and deterministic by avoiding heavy I/O and concrete object creation.

---

## Additional Notes on Safety and Testing

- Concurrency hazards are mitigated by limiting parallel operations to independent tasks and collecting events into thread-safe queues before sequential processing.
- Reflection use is audited and restricted to admin/diagnostic paths to minimize security and maintenance risks.
- IoC via constructor injection improves testability and follows SOLID principles.

---

````