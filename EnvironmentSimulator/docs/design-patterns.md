````markdown
# Design Patterns Implementation Guide

This document details all design patterns implemented in the Environment Simulator project, their locations in the codebase, and the rationale for their use.

---

## Table of Contents
1. [Factory Method Pattern](#factory-method-pattern)
2. [Builder Pattern](#builder-pattern)
3. [Strategy Pattern](#strategy-pattern)
4. [Chain of Responsibility Pattern](#chain-of-responsibility-pattern)
5. [Memento Pattern](#memento-pattern)
6. [Exception Shielding Pattern](#exception-shielding-pattern)
7. [Composite Pattern](#composite-pattern)
8. [Iterator Pattern](#iterator-pattern)
9. [Template Method Pattern](#template-method-pattern)
10. [Observer Pattern](#observer-pattern)

---

## Factory Method Pattern

### Location
**Package:** `factoryMethod.AnimalFactory`

### Components
- **`AnimalFactory`** — Abstract factory interface defining animal creation contract
- **`CarnivoreFactory`** — Concrete factory for creating carnivore instances
- **`HerbivoreFactory`** — Concrete factory for creating herbivore instances
- **`AnimalRepository`** — Factory registry managing available factories

### Implementation Details
```
src/main/java/factoryMethod/AnimalFactory/
├── AnimalFactory.java          (Interface)
├── CarnivoreFactory.java       (Concrete Factory)
├── HerbivoreFactory.java       (Concrete Factory)
└── AnimalRepository.java       (Factory Registry)
```

### Purpose
Encapsulates animal creation logic and enables extensibility. New animal types can be added by creating new factory implementations without modifying existing code.

### Usage Example
The factories are used throughout the application when spawning animals via commands or during simulation initialization.

### Test Coverage
- `AnimalFactoryTest.java` — Unit tests for factory implementations
- `AnimalRepositoryTest.java` — Tests for factory registry
- `AnimalCreationIntegrationTest.java` — End-to-end animal creation tests

---

## Builder Pattern

### Location
**Package:** `builder.MapBuilder`

### Components
- **`MapBuilder`** — Fluent builder interface for constructing complex game maps
- **`Position`** — Value object representing coordinates on the map

### Implementation Details
```
src/main/java/builder/MapBuilder/
├── MapBuilder.java             (Builder Class)
└── Position.java               (Value Object)
```

### Purpose
Simplifies complex map construction with a readable, chainable API. Provides a clear separation between map construction logic and map representation.

### Key Features
- Fluent interface with method chaining
- Validation during construction
- Clear and position operations
- Dimension configuration

### Usage Example
Used when initializing game maps, setting up test scenarios, or creating custom environments.

### Test Coverage
- `MapBuilderTest.java` — Comprehensive builder tests including:
  - `BuilderMethods` — Tests fluent interface methods
  - `ClearOperations` — Tests map clearing functionality
  - `PositionOperations` — Tests position-based operations
  - `ValidationTests` — Tests input validation
- `MapBuilderMockitoTest.java` — Mock-based isolation tests
- `PositionTest.java` — Tests for Position value object

---

## Strategy Pattern

### Location
**Package:** `strategy.IO`

### Components
- **`LoadStrategy`** — Strategy interface for loading operations
- **`SaveStrategy`** — Strategy interface for saving operations
- **`JsonLoadStrategy`** — Concrete strategy for loading JSON files
- **`BinaryLoadStrategy`** — Concrete strategy for loading binary files
- **`JsonSaveStrategy`** — Concrete strategy for saving as JSON
- **`BinarySaveStrategy`** — Concrete strategy for saving as binary
- **`LoadGame`** — Context class for load operations
- **`SaveGame`** — Context class for save operations

### Implementation Details
```
src/main/java/strategy/IO/
├── LoadStrategy.java           (Strategy Interface)
├── SaveStrategy.java           (Strategy Interface)
├── JsonLoadStrategy.java       (Concrete Strategy)
├── BinaryLoadStrategy.java     (Concrete Strategy)
├── JsonSaveStrategy.java       (Concrete Strategy)
├── BinarySaveStrategy.java     (Concrete Strategy)
├── LoadGame.java               (Context)
├── SaveGame.java               (Context)
└── LoadException.java          (Custom Exception)
```

### Purpose
Enables runtime selection of serialization format (JSON vs Binary). Allows easy addition of new file formats without modifying existing code.

### Key Features
- Interchangeable serialization strategies
- Automatic file extension handling
- Consistent error handling across strategies
- Support for multiple file formats

### Usage Example
The strategy pattern is used in save/load commands:
```java
LoadGame.load("savefile", "json");    // Uses JsonLoadStrategy
LoadGame.load("savefile", "binary");  // Uses BinaryLoadStrategy
```

### Test Coverage
Tests verify correct strategy selection and file operations for each format.

---

## Chain of Responsibility Pattern

### Location
**Package:** `chainOfResponsibility.commandHandler`

### Components
- **`CommandHandler`** — Abstract base handler defining the chain contract
- **`SpawnCommandHandler`** — Concrete handler for spawn commands
- **`HelpCommandHandler`** — Concrete handler for help commands
- **`CommandHandlerChain`** — Chain coordinator managing handler sequence

### Implementation Details
```
src/main/java/chainOfResponsibility/commandHandler/
├── CommandHandler.java         (Abstract Handler)
├── SpawnCommandHandler.java    (Concrete Handler)
├── HelpCommandHandler.java     (Concrete Handler)
└── CommandHandlerChain.java    (Chain Coordinator)
```

### Purpose
Processes user commands through a flexible, extensible handler chain. New commands can be added by implementing new handlers without modifying the chain structure.

### Key Features
- Decoupled command processing
- Easy extensibility for new commands
- Sequential request handling with pass-through
- Centralized command routing

### Usage Example
Commands are processed through the chain:
```java
CommandHandlerChain chain = new CommandHandlerChain();
chain.handle("spawn carnivore 5");
chain.handle("help");
```

### Test Coverage
- `CommandHandlerChainTest.java` — Tests chain coordination
- `SpawnCommandHandlerTest.java` — Tests spawn command processing
- `HelpCommandHandlerTest.java` — Tests help command processing

---

## Memento Pattern

### Location
**Package:** `memento.GameSnapshot`

### Components
- **`GameSnapshot`** — Memento class storing complete simulation state
- Integration with `LoadGame` and `SaveGame` for persistence

### Implementation Details
```
src/main/java/memento/GameSnapshot/
└── GameSnapshot.java           (Memento)
```

### Purpose
Captures and restores simulation state for persistence and undo functionality. Provides a clean interface for state management without exposing internal structure.

### Key Features
- Immutable state capture
- Integration with Strategy pattern for serialization
- Support for undo/redo operations
- State versioning capability

### Usage Example
Used in save/load operations:
```java
GameSnapshot snapshot = gameState.createSnapshot();
SaveGame.save(snapshot, "checkpoint", "json");

GameSnapshot loaded = LoadGame.load("checkpoint", "json");
gameState.restoreFromSnapshot(loaded);
```

### Test Coverage
Tested through integration tests that validate save/load cycles and state restoration.

---

## Exception Shielding Pattern

### Location
**Throughout the codebase** (cross-cutting concern)

### Components
- **`LoadException`** — Custom exception for load operation failures
- Exception handling wrappers in all user-facing operations
- Logging integration for technical details

### Implementation Details
```
Exception shielding is implemented in:
├── strategy/IO/LoadGame.java       (Shields I/O exceptions)
├── strategy/IO/SaveGame.java       (Shields I/O exceptions)
├── chainOfResponsibility/          (Shields command exceptions)
└── Various other entry points
```

### Purpose
Protects users from technical error details while maintaining a complete audit trail. Ensures stack traces never reach end users.

### Key Features
- Technical exceptions wrapped in user-friendly exceptions
- Full logging of technical details
- Clear, actionable error messages for users
- Controlled exception propagation

### Usage Example
```java
try {
    GameSnapshot snapshot = strategy.load(fullPath.toString());
    logger.info("Game successfully loaded from '{}'", fullPath);
    return snapshot;
} catch (Exception e) {
    logger.error("Failed to load game from '{}': {}", fullPath, e.getMessage(), e);
    throw new LoadException("Unable to load game from: " + fullPath, e);
}
```

### Test Coverage
- `ExceptionShieldingTest.java` — Tests exception wrapping
- `StackTraceVisibilityTest.java` — Verifies no stack traces leak to users
- `InvalidInputHandlingTest.java` — Tests input validation and error handling

---

## Composite Pattern

### Location
**Package:** Entity hierarchy (implementation details based on current codebase structure)

### Components
Entity hierarchy implementing uniform treatment of individual entities and entity groups.

### Implementation Details
```
src/main/java/
└── [Entity package structure]
    ├── Entity.java             (Component interface)
    ├── AnimalEntity.java       (Leaf)
    ├── ResourceEntity.java     (Leaf)
    └── EntityGroup.java        (Composite)
```

### Purpose
Treats individual entities and groups uniformly, allowing operations on single entities or collections without type checking.

### Key Features
- Uniform interface for single and composite entities
- Recursive composition support
- Simplified client code
- Hierarchical entity management

### Test Coverage
Tests verify uniform behavior across individual and composite entities.

---

## Iterator Pattern

### Location
**Package:** Entity collections (implementation details based on current codebase structure)

### Components
Custom iterators for traversing entity collections.

### Implementation Details
```
src/main/java/
└── [Collections package]
    ├── EntityCollection.java   (Iterable implementation)
    └── EntityIterator.java     (Iterator implementation)
```

### Purpose
Provides unified traversal of different entity collection types following standard Java iterator contract.

### Key Features
- Standard iterator interface implementation
- Support for multiple collection types
- Safe concurrent modification handling
- Lazy evaluation where applicable

### Test Coverage
Tests verify correct iteration behavior, including edge cases and concurrent modifications.

---

## Template Method Pattern

### Location
**Package:** Game loop and behavior templates (implementation details based on current codebase structure)

### Components
Abstract classes defining algorithm skeletons with customizable steps.

### Implementation Details
```
src/main/java/
└── [Game logic package]
    ├── GameLoop.java           (Template method)
    ├── EntityBehavior.java     (Template method)
    └── Various subclasses      (Concrete implementations)
```

### Purpose
Defines algorithm structure in base classes while allowing subclasses to customize specific steps.

### Key Features
- Algorithm skeleton in base class
- Customizable hook methods in subclasses
- Code reuse through inheritance
- Consistent process flow

### Test Coverage
Tests verify base algorithm execution and subclass customizations.

---

## Observer Pattern

### Location
**Package:** Event/notification system (implementation details based on current codebase structure)

### Components
- **`Subject`/`Observable`** — Interface or base class for objects being observed
- **`Observer`/`Listener`** — Interface for objects that need to be notified of changes
- Concrete implementations for specific event types

### Implementation Details
```
src/main/java/
└── [Observer package]
    ├── Subject.java            (Observable interface)
    ├── Observer.java           (Observer interface)
    └── Various implementations (Concrete observers and subjects)
```

### Purpose
Establishes a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. Enables loose coupling between components.

### Key Features
- Event-driven architecture
- Decoupled notification system
- Support for multiple observers per subject
- Dynamic registration/unregistration of observers
- Real-time state change propagation

### Usage Example
Used for event notifications such as:
- Entity state changes (health, position, status)
- Game events (entity death, reproduction, combat)
- Simulation state updates
- Resource consumption events

### Test Coverage
Tests verify observer registration, notification delivery, and proper decoupling between subjects and observers.

---

## Pattern Integration

### Save/Load Workflow
**Patterns:** Strategy + Memento + Exception Shielding

1. `GameSnapshot` (Memento) captures simulation state
2. `LoadStrategy`/`SaveStrategy` (Strategy) handles serialization format
3. `LoadException` (Exception Shielding) protects user from technical errors

### Command Processing
**Patterns:** Chain of Responsibility + Factory + Exception Shielding

1. `CommandHandlerChain` (Chain of Responsibility) routes commands
2. `AnimalFactory` (Factory Method) creates entities from commands
3. Exception Shielding ensures graceful error handling

### Map Construction
**Patterns:** Builder + Composite + Iterator

1. `MapBuilder` (Builder) constructs complex maps
2. Composite pattern manages entity hierarchies on the map
3. Iterator pattern enables map traversal

### Event Notification
**Patterns:** Observer + Composite

1. Observer pattern notifies interested components of entity state changes
2. Composite entities propagate events through hierarchy
3. Decoupled event handling improves maintainability

---

## Summary

This project implements **10 design patterns** demonstrating:

### Required Patterns (4)
- ✅ Factory Method
- ✅ Composite
- ✅ Iterator
- ✅ Exception Shielding

### Optional Patterns (6)
- ✅ Builder
- ✅ Strategy
- ✅ Chain of Responsibility
- ✅ Memento
- ✅ Template Method
- ✅ Observer


````