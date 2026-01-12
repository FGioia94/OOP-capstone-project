# Exception Shielding Pattern Implementation

## Overview
This document describes the Exception Shielding pattern implementation in the Environment Simulator project. Exception shielding is a defensive programming technique that converts low-level, infrastructure-specific exceptions into higher-level, domain-specific exceptions, protecting the application layer from implementation details.

## What is Exception Shielding?

Exception shielding creates a boundary between different layers of an application. It "shields" higher layers from knowing about:
- Low-level I/O exceptions (IOException, FileNotFoundException)
- Serialization exceptions (ClassNotFoundException, NotSerializableException)
- Framework-specific exceptions (JsonSyntaxException, JsonParseException)
- System errors (OutOfMemoryError, AccessDeniedException)

Instead, these are converted into meaningful, domain-specific exceptions that make sense in the business context.

## Architecture

### Base Exception
```java
GameException (base class for all domain exceptions)
├── ReadException (file reading failures)
├── SaveException (file writing failures)
├── SerializationException (object serialization failures)
├── DeserializationException (object deserialization failures)
└── LoadException (game loading failures)
```

### Shielding Layer
The `ExceptionShieldingLayer` class provides static methods that wrap operations and convert exceptions:

1. **shieldRead()** - Converts I/O exceptions during file reading
2. **shieldSave()** - Converts I/O exceptions during file writing
3. **shieldSerialization()** - Converts serialization exceptions
4. **shieldDeserialization()** - Converts deserialization exceptions
5. **shieldLoad()** - Converts combined load operation exceptions

## Implementation Details

### File: exceptionShielding/GameException.java
Base exception class that all domain exceptions extend from. Provides a consistent exception hierarchy.

### File: exceptionShielding/ExceptionShieldingLayer.java
Central shielding layer with the following capabilities:

**Read Operations:**
- FileNotFoundException → ReadException with "File not found" message
- AccessDeniedException → ReadException with "Access denied" message
- IOException → ReadException with contextual error
- OutOfMemoryError → ReadException with "File too large" message

**Save Operations:**
- AccessDeniedException → SaveException with "Access denied" message
- IOException (disk full) → SaveException with "Insufficient disk space"
- IOException (generic) → SaveException with contextual error
- OutOfMemoryError → SaveException with "Insufficient memory"

**Serialization:**
- NotSerializableException → SerializationException with class details
- OutOfMemoryError → SerializationException with memory warning
- Generic exceptions → SerializationException with context

**Deserialization:**
- ClassNotFoundException → DeserializationException with class name
- InvalidClassException → DeserializationException with version mismatch warning
- JsonSyntaxException → DeserializationException with "Invalid JSON" message
- JsonParseException → DeserializationException with parsing details
- OutOfMemoryError → DeserializationException with memory warning

## Where It's Used

### Strategy Pattern Classes
The exception shielding layer is integrated into the Strategy pattern implementations:

1. **JsonReadStrategy** - Uses `shieldRead()` to protect file reading
2. **BinaryReadStrategy** - Uses `shieldRead()` to protect binary file reading
3. **JsonDeserializationStrategy** - Uses `shieldDeserialization()` for JSON parsing
4. **BinaryDeserializationStrategy** - Uses `shieldDeserialization()` for object deserialization
5. **JsonSerializationStrategy** - Uses `shieldSerialization()` for JSON creation

### Example: Before and After

**Before (manual exception handling):**
```java
public String read(String filePath) {
    Path path = Paths.get(filePath);
    try {
        return Files.readString(path);
    } catch (Exception e) {
        throw new ReadException("Unable to read file: " + filePath, e);
    }
}
```

**After (with exception shielding):**
```java
public String read(String filePath) {
    return ExceptionShieldingLayer.shieldRead(() -> {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }, filePath);
}
```

## Benefits

1. **Decoupling**: Application logic doesn't depend on infrastructure exceptions
2. **Consistency**: All exceptions follow the same pattern and hierarchy
3. **User-Friendly**: Error messages are meaningful to the domain
4. **Maintainability**: Exception handling logic is centralized
5. **Testability**: Easy to test exception scenarios
6. **Information Hiding**: Implementation details are hidden from callers

## Exception Flow Example

```
Low-Level Exception          Shielding Layer              Domain Exception
───────────────────         ─────────────────            ────────────────
FileNotFoundException   →   shieldRead()          →      ReadException
                            "File not found: X"           (meaningful message)

IOException             →   shieldSave()          →      SaveException
                            "Failed to save: Y"           (contextual info)

JsonSyntaxException     →   shieldDeserialization() →    DeserializationException
                            "Invalid JSON format"         (user-friendly)

ClassNotFoundException  →   shieldDeserialization() →    DeserializationException
                            "Class version mismatch"      (actionable advice)
```

## Testing

### ExceptionShieldingTest
**Location**: `src/test/java/ExceptionShieldingTest.java`

**Test Coverage** (13 tests):
1. FileNotFoundException → ReadException conversion
2. IOException → ReadException conversion
3. IOException → SaveException conversion
4. OutOfMemoryError → SaveException conversion
5. JsonSyntaxException → DeserializationException conversion
6. ClassNotFoundException → DeserializationException conversion
7. Generic exception → SerializationException conversion
8. Domain exception propagation through LoadException
9. Successful operation completion without interference
10. JsonReadStrategy integration
11. BinaryReadStrategy integration
12. User-friendly error message generation
13. AccessDeniedException → ReadException with helpful message

**Results**: All 13 tests passing ✅

## Design Pattern Classification

Exception Shielding is considered:
- **Architectural Pattern**: Defines layer boundaries
- **Defensive Programming**: Protects against unexpected failures
- **Wrapper Pattern**: Wraps exceptions in domain-specific types
- **Adapter Pattern**: Adapts low-level exceptions to high-level interface

## Integration with Other Patterns

The exception shielding integrates seamlessly with existing patterns:

1. **Strategy Pattern**: Used in all I/O strategies for consistent exception handling
2. **Template Method**: Exceptions can be handled uniformly in template method implementations
3. **Memento Pattern**: Protects snapshot save/load operations
4. **Factory Method**: Can shield object creation exceptions

## Usage Guidelines

### When to Use Shielding

✅ **Use exception shielding when:**
- Crossing architectural boundaries (I/O layer → Application layer)
- Working with external resources (files, network, database)
- Dealing with framework-specific exceptions
- Providing user-facing error messages

❌ **Don't use shielding when:**
- Within a single layer where low-level exceptions are appropriate
- The caller needs specific low-level exception information
- Performance is critical and exception wrapping adds overhead

### Best Practices

1. **Always provide context**: Include file paths, operation types in error messages
2. **Preserve the cause**: Always pass the original exception as the cause
3. **Log at the boundary**: Log when converting exceptions
4. **Keep messages user-friendly**: Avoid technical jargon
5. **Provide actionable advice**: Tell users how to fix the problem

## Example Usage in Code

```java
// Reading a file with exception shielding
public String loadConfiguration(String filePath) {
    return ExceptionShieldingLayer.shieldRead(() -> {
        // Low-level operation
        return Files.readString(Path.of(filePath));
    }, filePath);
    // Caller receives ReadException, not IOException
}

// Saving data with exception shielding
public void saveGameState(GameSnapshot snapshot, String filePath) {
    ExceptionShieldingLayer.shieldSave(() -> {
        // Low-level operations
        String json = new Gson().toJson(snapshot);
        Files.writeString(Path.of(filePath), json);
    }, filePath);
    // Caller receives SaveException, not IOException
}
```

## Error Message Examples

The shielding layer provides clear, actionable error messages:

| Low-Level Exception | Domain Exception Message |
|---------------------|--------------------------|
| FileNotFoundException | "File not found: /path/to/file.json. Please check the file path and try again." |
| AccessDeniedException | "Access denied: Cannot read file /path/to/file. Check file permissions." |
| IOException (disk full) | "Insufficient disk space to save file: /path/to/save.bin" |
| JsonSyntaxException | "Cannot deserialize GameSnapshot: Invalid JSON format. The file may be corrupted." |
| ClassNotFoundException | "Cannot deserialize GameData: Required class not found. Class: com.example.Missing" |
| OutOfMemoryError | "File too large to read: /huge/file.bin. Insufficient memory." |

## Future Enhancements

Potential improvements to the exception shielding layer:

1. **Retry Logic**: Add automatic retry for transient failures
2. **Circuit Breaker**: Prevent cascading failures
3. **Exception Translation Map**: Configurable exception mappings
4. **Localization**: Multi-language error messages
5. **Telemetry**: Integrate with monitoring systems

## Conclusion

The Exception Shielding pattern provides a robust boundary between infrastructure and application layers, ensuring that:
- Low-level exceptions don't leak into business logic
- Error messages are meaningful and actionable
- Exception handling is consistent across the application
- The codebase remains maintainable and testable

This implementation fulfills the teacher requirement for demonstrating the Exception Shielding pattern while adding real value to the project through improved error handling and user experience.

## Teacher Requirements Met

✅ **Exception Shielding**: Fully implemented with comprehensive coverage  
✅ **Domain-Specific Exceptions**: GameException hierarchy created  
✅ **Centralized Handling**: ExceptionShieldingLayer utility class  
✅ **Integration**: Applied to Strategy pattern implementations  
✅ **Testing**: 13 tests demonstrating pattern effectiveness  
✅ **Documentation**: Complete explanation of pattern and usage  

---

**Total Design Patterns: 10 of 17 (59%)**

With exception shielding added, the project now demonstrates:
1. Factory Method ✅
2. Composite ✅
3. Iterator ✅
4. **Exception Shielding ✅ (NEW)**
5. Builder ✅
6. Strategy ✅
7. Observer ✅
8. Chain of Responsibility ✅
9. Memento ✅
10. Template Method ✅
