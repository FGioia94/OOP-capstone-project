# Testing Suite Documentation

This project includes a comprehensive testing suite with both unit and integration tests.

## Test Structure

```
src/test/java/
├── builder/MapBuilder/          # Unit tests for map builder components
│   ├── PositionTest.java
│   └── MapBuilderTest.java
├── factoryMethod/AnimalFactory/  # Unit tests for animal factory pattern
│   ├── CarnivoreTest.java
│   ├── HerbivoreTest.java
│   └── AnimalFactoryTest.java
├── chainOfResponsibility/        # Unit tests for command handlers
│   └── commandHandler/
│       ├── HelpCommandHandlerTest.java
│       └── CommandHandlerChainTest.java
├── integration/                  # Integration tests
│   ├── AnimalCreationIntegrationTest.java
│   └── MapMovementIntegrationTest.java
└── testutils/                    # Test utilities and helpers
    └── TestDataBuilder.java
```

## Technologies Used

- **JUnit 5** (Jupiter): Modern testing framework with annotations like `@Test`, `@BeforeEach`, `@DisplayName`
- **Mockito 5**: Mocking framework for creating test doubles and verifying interactions
- **AssertJ**: Fluent assertion library for more readable test assertions

## Running Tests

### Run all tests
```bash
./gradlew test
```

### Run specific test class
```bash
./gradlew test --tests PositionTest
```

### Run tests with coverage
```bash
./gradlew test jacocoTestReport
```

### Run tests in a specific package
```bash
./gradlew test --tests "builder.MapBuilder.*"
```

## Test Categories

### Unit Tests
Unit tests focus on testing individual components in isolation:

- **PositionTest**: Tests the Position record class
- **MapBuilderTest**: Tests the builder pattern for map creation
- **CarnivoreTest/HerbivoreTest**: Tests individual animal types
- **AnimalFactoryTest**: Tests factory pattern with mocked dependencies
- **CommandHandlerTests**: Tests command chain pattern

### Integration Tests
Integration tests verify that multiple components work together:

- **AnimalCreationIntegrationTest**: Tests the complete workflow of creating animals with map and repository
- **MapMovementIntegrationTest**: Tests map building and animal movement together

## Writing New Tests

### Unit Test Example
```java
@DisplayName("My Component Unit Tests")
class MyComponentTest {
    
    @BeforeEach
    void setUp() {
        // Initialize test fixtures
    }
    
    @Test
    @DisplayName("Should do something specific")
    void testSpecificBehavior() {
        // Arrange
        // Act
        // Assert
    }
}
```

### Using Mockito
```java
@Mock
private Dependency mockDependency;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
    when(mockDependency.someMethod()).thenReturn(value);
}

@Test
void testWithMock() {
    // Use mockDependency
    verify(mockDependency, times(1)).someMethod();
}
```

## Test Utilities

The `TestDataBuilder` class provides common test fixtures:

```java
// Create a basic map for testing
MapBuilder builder = TestDataBuilder.createBasicMapBuilder();

// Create valid positions
List<Position> positions = TestDataBuilder.createValidPositionsList();
```

## Best Practices

1. **Follow AAA Pattern**: Arrange, Act, Assert
2. **Use descriptive test names**: `@DisplayName` annotations explain what is being tested
3. **Test one thing per test**: Each test should verify a single behavior
4. **Use mocks for external dependencies**: Isolate the unit under test
5. **Clean up resources**: Use `@AfterEach` when needed
6. **Use meaningful assertions**: Prefer specific assertions over generic ones

## Coverage Goals

- **Unit Tests**: Aim for >80% code coverage
- **Integration Tests**: Cover critical user workflows
- **Edge Cases**: Test boundary conditions, null values, and error scenarios

## Continuous Integration

Tests are automatically run on:
- Every commit
- Pull requests
- Before deployment

Failed tests will block the build pipeline.
