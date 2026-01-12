package chainOfResponsibility.commandHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import template.Game.GameLoop;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HelpCommandHandler using Mockito.
 */
@DisplayName("HelpCommandHandler Unit Tests")
class HelpCommandHandlerTest {

    private HelpCommandHandler handler;

    @Mock
    private Scanner mockScanner;

    @Mock
    private GameLoop mockGameLoop;

    @Mock
    private CommandHandler mockNextHandler;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new HelpCommandHandler();
        
        // Capture System.out
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("Should handle 'help' command")
    void testHandleHelpCommand() {
        // Act
        boolean result = handler.handle("help", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result);
        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("continue"));
        assertTrue(output.contains("exit"));
        assertTrue(output.contains("save"));
        assertTrue(output.contains("load"));
    }

    @Test
    @DisplayName("Should handle 'h' command")
    void testHandleShortHelpCommand() {
        // Act
        boolean result = handler.handle("h", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result);
        String output = outputStream.toString();
        assertTrue(output.contains("Available commands:"));
    }

    @Test
    @DisplayName("Should be case-insensitive")
    void testCaseInsensitive() {
        // Act
        boolean result1 = handler.handle("HELP", mockScanner, mockGameLoop);
        outputStream.reset();
        boolean result2 = handler.handle("HeLp", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    @DisplayName("Should pass to next handler for non-help commands")
    void testPassToNextHandler() {
        // Arrange
        handler.setNext(mockNextHandler);
        when(mockNextHandler.handle(anyString(), any(), any())).thenReturn(true);

        // Act
        boolean result = handler.handle("continue", mockScanner, mockGameLoop);

        // Assert
        assertTrue(result);
        verify(mockNextHandler, times(1)).handle("continue", mockScanner, mockGameLoop);
    }

    @Test
    @DisplayName("Should return false when command not handled and no next handler")
    void testNoNextHandler() {
        // Act
        boolean result = handler.handle("unknown", mockScanner, mockGameLoop);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should display all expected commands in help text")
    void testHelpTextContainsAllCommands() {
        // Act
        handler.handle("help", mockScanner, mockGameLoop);
        String output = outputStream.toString();

        // Assert - Check for all major commands
        assertTrue(output.contains("continue"));
        assertTrue(output.contains("clearAnimals"));
        assertTrue(output.contains("clearResources"));
        assertTrue(output.contains("create"));
        assertTrue(output.contains("deleteAnimal"));
        assertTrue(output.contains("exit"));
        assertTrue(output.contains("help"));
        assertTrue(output.contains("load"));
        assertTrue(output.contains("listAnimals"));
        assertTrue(output.contains("listMap"));
        assertTrue(output.contains("listPacks"));
        assertTrue(output.contains("save"));
        assertTrue(output.contains("spawn"));
        assertTrue(output.contains("pack"));
    }
}
