import exceptionShielding.ExceptionShieldingLayer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import strategy.IO.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests demonstrating the Exception Shielding pattern.
 * Shows how low-level exceptions are converted to domain-specific exceptions.
 */
public class ExceptionShieldingTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Exception shielding should convert FileNotFoundException to ReadException")
    void testShieldReadFileNotFound() {
        String nonExistentFile = tempDir.resolve("does_not_exist.txt").toString();

        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldRead(() -> Files.readString(Path.of(nonExistentFile)), nonExistentFile)
        )
                .isInstanceOf(ReadException.class)
                .hasMessageContaining("File not found")
                .hasMessageContaining(nonExistentFile)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("Exception shielding should convert IOException to ReadException with meaningful message")
    void testShieldReadIOException() {
        // Create a file and make it inaccessible (platform-dependent behavior)
        String filePath = tempDir.resolve("test.txt").toString();

        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldRead(() -> {
                    throw new IOException("Disk read error");
                }, filePath)
        )
                .isInstanceOf(ReadException.class)
                .hasMessageContaining("Failed to read file")
                .hasMessageContaining(filePath)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("Exception shielding should convert IOException to SaveException")
    void testShieldSaveIOException() {
        String filePath = tempDir.resolve("test_save.txt").toString();

        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldSave(() -> {
                    throw new IOException("Disk write error");
                }, filePath)
        )
                .isInstanceOf(SaveException.class)
                .hasMessageContaining("Failed to save file")
                .hasMessageContaining(filePath)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("Exception shielding should convert OutOfMemoryError to SaveException")
    void testShieldSaveOutOfMemory() {
        String filePath = "/tmp/huge_file.bin";

        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldSave(() -> {
                    throw new OutOfMemoryError("Java heap space");
                }, filePath)
        )
                .isInstanceOf(SaveException.class)
                .hasMessageContaining("Insufficient memory")
                .hasMessageContaining(filePath);
    }

    @Test
    @DisplayName("Exception shielding should convert JSON parse errors to DeserializationException")
    void testShieldDeserializationJsonError() {
        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldDeserialization(() -> {
                    throw new com.google.gson.JsonSyntaxException("Malformed JSON");
                }, "GameSnapshot")
        )
                .isInstanceOf(DeserializationException.class)
                .hasMessageContaining("Invalid JSON format")
                .hasMessageContaining("GameSnapshot")
                .hasCauseInstanceOf(com.google.gson.JsonSyntaxException.class);
    }

    @Test
    @DisplayName("Exception shielding should convert ClassNotFoundException to DeserializationException")
    void testShieldDeserializationClassNotFound() {
        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldDeserialization(() -> {
                    throw new ClassNotFoundException("com.example.MissingClass");
                }, "GameData")
        )
                .isInstanceOf(DeserializationException.class)
                .hasMessageContaining("Required class not found")
                .hasMessageContaining("GameData")
                .hasCauseInstanceOf(ClassNotFoundException.class);
    }

    @Test
    @DisplayName("Exception shielding should convert generic exceptions to SerializationException")
    void testShieldSerializationGenericError() {
        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldSerialization(() -> {
                    throw new RuntimeException("Serialization failed");
                }, "GameSnapshot")
        )
                .isInstanceOf(SerializationException.class)
                .hasMessageContaining("Failed to serialize")
                .hasMessageContaining("GameSnapshot")
                .hasCauseInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Exception shielding should propagate domain exceptions through load operation")
    void testShieldLoadPropagatesDomainExceptions() {
        String fileName = "corrupted.json";

        assertThatThrownBy(() ->
                ExceptionShieldingLayer.shieldLoad(() -> {
                    throw new ReadException("Cannot read file", new IOException());
                }, fileName)
        )
                .isInstanceOf(LoadException.class)
                .hasMessageContaining("Failed to load game")
                .hasMessageContaining(fileName)
                .hasCauseInstanceOf(ReadException.class);
    }

    @Test
    @DisplayName("Exception shielding should allow successful operations to complete")
    void testShieldReadSuccess() throws Exception {
        Path testFile = tempDir.resolve("success.txt");
        Files.writeString(testFile, "Test content");

        String result = ExceptionShieldingLayer.shieldRead(
                () -> Files.readString(testFile),
                testFile.toString()
        );

        assertThat(result).isEqualTo("Test content");
    }

    @Test
    @DisplayName("JsonReadStrategy should use exception shielding")
    void testJsonReadStrategyUsesShielding() {
        JsonReadStrategy strategy = new JsonReadStrategy();
        String nonExistentFile = tempDir.resolve("missing.json").toString();

        assertThatThrownBy(() -> strategy.read(nonExistentFile))
                .isInstanceOf(ReadException.class)
                .hasMessageContaining("File not found");
    }

    @Test
    @DisplayName("BinaryReadStrategy should use exception shielding")
    void testBinaryReadStrategyUsesShielding() {
        BinaryReadStrategy strategy = new BinaryReadStrategy();
        String nonExistentFile = tempDir.resolve("missing.bin").toString();

        assertThatThrownBy(() -> strategy.read(nonExistentFile))
                .isInstanceOf(ReadException.class)
                .hasMessageContaining("File not found");
    }

    @Test
    @DisplayName("Exception shielding provides user-friendly error messages")
    void testUserFriendlyErrorMessages() {
        String filePath = "/restricted/file.txt";

        Throwable exception = catchThrowable(() ->
                ExceptionShieldingLayer.shieldRead(() -> {
                    throw new java.nio.file.AccessDeniedException(filePath);
                }, filePath)
        );

        assertThat(exception)
                .isInstanceOf(ReadException.class)
                .hasMessage("Access denied: Cannot read file " + filePath + ". Check file permissions.");
    }
}
