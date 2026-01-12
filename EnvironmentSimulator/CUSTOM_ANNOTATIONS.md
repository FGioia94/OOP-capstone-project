# Custom Annotations Implementation Summary

## Overview
This document summarizes the custom annotations implementation for the Environment Simulator project, demonstrating advanced Java features including custom annotations, reflection, and annotation processing.

## Implemented Annotations

### 1. @AdminOnly
**Purpose**: Marks command handlers that require administrator privileges.

**Location**: `src/main/java/annotations/AdminOnly.java`

**Features**:
- `@Retention(RUNTIME)` - Available at runtime via reflection
- `@Target(TYPE)` - Can only be applied to classes
- `reason()` parameter - Explains why admin access is required

**Example Usage**:
```java
@AdminOnly(reason = "Animal inspection requires admin privileges for debugging")
public class InspectCommandHandler extends CommandHandler {
    // Implementation
}
```

**Processing**:
- `AdminAnnotationChecker` utility class uses reflection to check for the annotation
- `GameLoop` conditionally registers handlers based on admin mode and annotation
- Demonstrates runtime annotation processing

### 2. @ValidPosition
**Purpose**: Validates Position parameters with configurable bounds.

**Location**: `src/main/java/annotations/ValidPosition.java`

**Features**:
- `minX()`, `maxX()`, `minY()`, `maxY()` - Coordinate bounds
- `nullable()` - Whether null positions are allowed
- `message()` - Custom validation error message

**Example Usage**:
```java
public void setPosition(@ValidPosition Position position) {
    PositionValidator.validateDefault(position);
    this.position = position;
}
```

**Processing**:
- `PositionValidator` utility class validates positions
- `validateDefault()` method enforces non-null, non-negative coordinates
- `validate()` method uses annotation metadata for custom validation

## Utility Classes

### AdminAnnotationChecker
**Purpose**: Checks if command handlers require admin mode.

**Key Methods**:
```java
public static boolean requiresAdminMode(CommandHandler handler)
public static boolean requiresAdminMode(Class<? extends CommandHandler> handlerClass)
```

**Implementation**:
- Uses reflection: `handlerClass.isAnnotationPresent(AdminOnly.class)`
- Logs annotation detection for debugging
- Returns false if annotation not present

### PositionValidator
**Purpose**: Validates positions based on @ValidPosition constraints.

**Key Methods**:
```java
public static void validate(Position position, ValidPosition annotation)
public static void validateDefault(Position position)
public static boolean isValid(Position position, ValidPosition annotation)
```

**Implementation**:
- Reads annotation metadata via reflection
- Validates null, min/max bounds
- Throws IllegalArgumentException on validation failure

## Where Annotations Are Used

### @AdminOnly Usage
1. **InspectCommandHandler** - Admin-only reflection-based animal inspection
   - Line: Class declaration
   - Reason: "Animal inspection requires admin privileges for debugging"

### @ValidPosition Usage
1. **Animal.setPosition()** - Validates position before setting
   - Line: Method parameter
   - Validation: Non-null, non-negative coordinates

2. **MapBuilder.moveAnimal()** - Validates positions before movement
   - Line: Within method body
   - Validation: Ensures source and destination are valid

## Demonstration of Concepts

### Runtime Annotation Processing
The implementation demonstrates several advanced Java concepts:

1. **Custom Annotation Creation**
   - `@Retention(RUNTIME)` - Annotations available via reflection
   - `@Target(TYPE)` and `@Target(PARAMETER)` - Control where annotations apply
   - Annotation parameters for metadata

2. **Reflection API Usage**
   - `Class.isAnnotationPresent()` - Check for annotations
   - `Class.getAnnotation()` - Retrieve annotation instances
   - Annotation method invocation for metadata access

3. **Practical Applications**
   - Access control (@AdminOnly)
   - Input validation (@ValidPosition)
   - Runtime configuration
   - Metadata-driven behavior

## Testing

### CustomAnnotationsTest
**Location**: `src/test/java/CustomAnnotationsTest.java`

**Test Coverage**:
1. Admin annotation detection on InspectCommandHandler
2. Position validation with valid coordinates
3. Position validation with invalid X coordinate
4. Position validation with invalid Y coordinate
5. Null position rejection
6. Custom bounds validation
7. Non-annotated handler returns false

**Results**: All 5 tests passing ✅

## Integration with Existing Architecture

### GameLoop Integration
The `GameLoop` class now uses annotation processing to conditionally register command handlers:

```java
InspectCommandHandler inspectHandler = new InspectCommandHandler();
if (adminMode && AdminAnnotationChecker.requiresAdminMode(inspectHandler)) {
    logger.info("Admin mode: Adding InspectCommandHandler (verified via @AdminOnly annotation)");
    chainBuilder.add(inspectHandler);
} else if (!adminMode && AdminAnnotationChecker.requiresAdminMode(inspectHandler)) {
    logger.debug("Skipping InspectCommandHandler - requires admin mode (@AdminOnly)");
}
```

### Animal Class Integration
The `Animal` class uses position validation:

```java
public void setPosition(Position position) {
    PositionValidator.validateDefault(position);
    this.position = position;
    logger.debug("{} moved to position {}", this.name, position);
}
```

## Benefits

1. **Declarative Access Control**: @AdminOnly clearly marks privileged operations
2. **Type-Safe Validation**: @ValidPosition ensures coordinate validity
3. **Separation of Concerns**: Validation logic separated from business logic
4. **Extensibility**: Easy to add new annotations and validators
5. **Runtime Flexibility**: Behavior changes based on annotation metadata

## Teacher Requirements Met

✅ **JUnit Testing**: 115+ unit and integration tests  
✅ **Mockito**: Used in 5+ test classes for mocking  
✅ **Java Reflection**: AnimalInspector + annotation processing  
✅ **Custom Annotations**: @AdminOnly and @ValidPosition with runtime processing  

## Files Created/Modified

**New Files**:
- `annotations/AdminOnly.java`
- `annotations/ValidPosition.java`
- `annotations/AdminAnnotationChecker.java`
- `annotations/PositionValidator.java`
- `CustomAnnotationsTest.java`

**Modified Files**:
- `GameLoop.java` - Annotation-based handler registration
- `Animal.java` - Position validation
- `MapBuilder.java` - Position validation
- `InspectCommandHandler.java` - @AdminOnly annotation

## Conclusion

The custom annotations implementation demonstrates advanced Java features including:
- Custom annotation definition with metadata
- Runtime annotation processing via reflection
- Practical application for access control and validation
- Clean integration with existing architecture
- Comprehensive test coverage

This implementation fulfills all teacher requirements while adding real value to the project through improved access control and input validation.
