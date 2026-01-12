import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import strategy.IO.ReadException;
import strategy.IO.SaveException;
import strategy.IO.DeserializationException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests to verify that stack traces are NOT visible to users.
 * This ensures proper exception shielding is in place.
 */
public class StackTraceVisibilityTest {

    private final ByteArrayOutputStream systemOutContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream systemErrContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(systemOutContent));
        System.setErr(new PrintStream(systemErrContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("ReadException should not expose stack trace in message")
    void testReadExceptionNoStackTrace() {
        Exception lowLevel = new java.io.FileNotFoundException("file.txt");
        ReadException readEx = new ReadException("File not found: file.txt. Please check the file path.", lowLevel);

        String message = readEx.getMessage();

        // Message should be user-friendly, not contain stack trace elements
        assertThat(message)
                .contains("File not found")
                .contains("file.txt")
                .doesNotContain("java.io.FileNotFoundException")
                .doesNotContain("at java.base")
                .doesNotContain("at sun.nio");
    }

    @Test
    @DisplayName("SaveException should not expose implementation details")
    void testSaveExceptionNoStackTrace() {
        Exception lowLevel = new java.io.IOException("No space left on device");
        SaveException saveEx = new SaveException("Insufficient disk space to save file: save.json", lowLevel);

        String message = saveEx.getMessage();

        assertThat(message)
                .contains("Insufficient disk space")
                .contains("save.json")
                .doesNotContain("java.io.IOException")
                .doesNotContain("at java.nio")
                .doesNotContain("No space left on device"); // Original low-level message hidden
    }

    @Test
    @DisplayName("DeserializationException should provide user-friendly message")
    void testDeserializationExceptionUserFriendly() {
        Exception lowLevel = new com.google.gson.JsonSyntaxException("Expected BEGIN_OBJECT but was STRING");
        DeserializationException deserEx = new DeserializationException(
                "Cannot deserialize GameSnapshot: Invalid JSON format. The file may be corrupted.",
                lowLevel
        );

        String message = deserEx.getMessage();

        assertThat(message)
                .contains("Invalid JSON format")
                .contains("corrupted")
                .doesNotContain("JsonSyntaxException")
                .doesNotContain("BEGIN_OBJECT")
                .doesNotContain("com.google.gson");
    }

    @Test
    @DisplayName("Exception getMessage should not contain class names or stack trace elements")
    void testExceptionMessageClean() {
        ReadException ex = new ReadException(
                "Access denied: Cannot read file /restricted/file.txt. Check file permissions.",
                new java.nio.file.AccessDeniedException("/restricted/file.txt")
        );

        String message = ex.getMessage();

        // Should NOT contain technical details
        assertThat(message)
                .doesNotContain("java.nio.file")
                .doesNotContain("AccessDeniedException")
                .doesNotContain("at ")
                .doesNotContain("Exception in thread");

        // Should contain user-friendly information
        assertThat(message)
                .contains("Access denied")
                .contains("Check file permissions");
    }

    @Test
    @DisplayName("Exception cause should be available for logging but not in message")
    void testExceptionCauseHiddenFromMessage() {
        java.io.IOException cause = new java.io.IOException("Low-level I/O error");
        ReadException readEx = new ReadException("Failed to read file data.txt", cause);

        // Message should be user-friendly
        assertThat(readEx.getMessage())
                .isEqualTo("Failed to read file data.txt")
                .doesNotContain("IOException")
                .doesNotContain("Low-level");

        // But cause should be available for logging/debugging
        assertThat(readEx.getCause())
                .isNotNull()
                .isInstanceOf(java.io.IOException.class)
                .hasMessage("Low-level I/O error");
    }

    @Test
    @DisplayName("Stack trace should not be printed to System.out or System.err without explicit printStackTrace call")
    void testNoAutomaticStackTracePrinting() {
        // Create an exception
        ReadException ex = new ReadException(
                "File not found: missing.json",
                new java.io.FileNotFoundException("missing.json")
        );

        // Just creating the exception shouldn't print anything
        assertThat(systemOutContent.toString()).isEmpty();
        assertThat(systemErrContent.toString()).isEmpty();

        // Even accessing the message shouldn't print stack traces
        String message = ex.getMessage();
        assertThat(systemOutContent.toString()).isEmpty();
        assertThat(systemErrContent.toString()).isEmpty();
    }

    @Test
    @DisplayName("Domain exceptions should be specific types, not generic RuntimeException")
    void testDomainSpecificExceptionTypes() {
        // Verify that we're using domain-specific exceptions
        ReadException readEx = new ReadException("Read error", new java.io.IOException());
        SaveException saveEx = new SaveException("Save error", new java.io.IOException());
        DeserializationException deserEx = new DeserializationException("Parse error", new Exception());

        // These should be our custom types (they can extend RuntimeException, that's fine)
        assertThat(readEx).isExactlyInstanceOf(ReadException.class);
        assertThat(saveEx).isExactlyInstanceOf(SaveException.class);
        assertThat(deserEx).isExactlyInstanceOf(DeserializationException.class);

        // They should be RuntimeExceptions for unchecked behavior
        assertThat(readEx).isInstanceOf(RuntimeException.class);
        assertThat(saveEx).isInstanceOf(RuntimeException.class);
        assertThat(deserEx).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Exception messages should be actionable for users")
    void testExceptionMessagesAreActionable() {
        // Good: tells user what to do
        ReadException goodEx = new ReadException(
                "File not found: game.json. Please check the file path and try again.",
                new java.io.FileNotFoundException()
        );

        assertThat(goodEx.getMessage())
                .contains("Please check")
                .contains("try again");

        // Messages should guide the user, not just state the problem
        SaveException guidingEx = new SaveException(
                "Access denied: Cannot write to file save.json. Check file permissions.",
                new java.nio.file.AccessDeniedException("save.json")
        );

        assertThat(guidingEx.getMessage())
                .contains("Check file permissions");
    }
}
