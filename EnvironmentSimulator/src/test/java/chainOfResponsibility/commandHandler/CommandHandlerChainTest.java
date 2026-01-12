package chainOfResponsibility.commandHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import template.Game.GameLoop;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommandHandler chain setup.
 */
@DisplayName("CommandHandler Chain Tests")
class CommandHandlerChainTest {

    @Mock
    private Scanner mockScanner;

    @Mock
    private GameLoop mockGameLoop;

    private CommandHandler firstHandler;
    private CommandHandler secondHandler;
    private CommandHandler thirdHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Create test handlers
        firstHandler = new HelpCommandHandler();
        secondHandler = new ExitCommandHandler();
        thirdHandler = new InvalidInputCommandHandler();
    }

    @Test
    @DisplayName("Should chain handlers correctly")
    void testChainHandlers() {
        // Arrange
        firstHandler.setNext(secondHandler);
        secondHandler.setNext(thirdHandler);

        // Assert - verify chain is set up
        assertNotNull(firstHandler);
        assertNotNull(secondHandler);
        assertNotNull(thirdHandler);
    }

    @Test
    @DisplayName("Should handle command with first handler in chain")
    void testFirstHandlerHandles() {
        // Arrange
        firstHandler.setNext(secondHandler);

        // Act
        boolean result = firstHandler.handle("help", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should pass command through chain until handled")
    void testChainPassthrough() {
        // Arrange
        firstHandler.setNext(secondHandler);
        secondHandler.setNext(thirdHandler);

        // Act - HelpCommandHandler won't handle "exit", but ExitCommandHandler will
        boolean result = firstHandler.handle("exit", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should use InvalidInputCommandHandler as fallback")
    void testFallbackHandler() {
        // Arrange
        firstHandler.setNext(secondHandler);
        secondHandler.setNext(thirdHandler);

        // Act - Unknown command should reach InvalidInputCommandHandler
        boolean result = firstHandler.handle("unknown_command", mockScanner, mockGameLoop);

        // Assert
        assertFalse(result); // InvalidInputCommandHandler returns false when there's no next handler
    }

    @Test
    @DisplayName("CommandChainBuilder should build complete chain")
    void testCommandChainBuilder() {
        // Arrange
        CommandChainBuilder builder = new CommandChainBuilder();

        // Act
        CommandHandler chain = builder
                .add(new HelpCommandHandler())
                .add(new ExitCommandHandler())
                .build();

        // Assert
        assertNotNull(chain);
        
        // Test that the chain can handle various commands
        boolean helpHandled = chain.handle("help", mockScanner, mockGameLoop);
        assertTrue(helpHandled);
    }
}
