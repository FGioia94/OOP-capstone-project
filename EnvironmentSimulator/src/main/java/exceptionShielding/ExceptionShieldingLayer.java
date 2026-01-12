package exceptionShielding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strategy.IO.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;

/**
 * Exception Shielding Layer
 * 
 * This class implements the Exception Shielding pattern by catching low-level
 * implementation exceptions and converting them into higher-level, domain-specific
 * exceptions. This shields the application layer from knowing about infrastructure
 * details and provides meaningful error messages to users.
 * 
 * Benefits:
 * - Decouples application logic from infrastructure concerns
 * - Provides consistent exception handling across the application
 * - Makes exceptions more meaningful to the business domain
 * - Prevents leaking implementation details to higher layers
 */
public class ExceptionShieldingLayer {

    private static final Logger logger = LogManager.getLogger(ExceptionShieldingLayer.class);

    /**
     * Shields file read operations by converting low-level I/O exceptions
     * into domain-specific ReadException.
     *
     * @param operation the read operation to execute
     * @param filePath  the file path being read
     * @param <T>       the type of data being read
     * @return the result of the read operation
     * @throws ReadException if the read operation fails
     */
    public static <T> T shieldRead(ThrowingSupplier<T> operation, String filePath) {
        try {
            logger.debug("Executing shielded read operation for file: {}", filePath);
            return operation.get();
            
        } catch (FileNotFoundException | NoSuchFileException e) {
            logger.error("File not found during read: {}", filePath, e);
            throw new ReadException(
                "File not found: " + filePath + ". Please check the file path and try again.",
                e
            );
            
        } catch (AccessDeniedException e) {
            logger.error("Access denied while reading file: {}", filePath, e);
            throw new ReadException(
                "Access denied: Cannot read file " + filePath + ". Check file permissions.",
                e
            );
            
        } catch (IOException e) {
            logger.error("I/O error during read: {}", filePath, e);
            throw new ReadException(
                "Failed to read file " + filePath + ": " + e.getMessage(),
                e
            );
            
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory while reading file: {}", filePath, e);
            throw new ReadException(
                "File too large to read: " + filePath + ". Insufficient memory.",
                e
            );
            
        } catch (Exception e) {
            logger.error("Unexpected error during read: {}", filePath, e);
            throw new ReadException(
                "Unexpected error reading file " + filePath + ": " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Shields file write operations by converting low-level I/O exceptions
     * into domain-specific SaveException.
     *
     * @param operation the save operation to execute
     * @param filePath  the file path being written
     * @throws SaveException if the save operation fails
     */
    public static void shieldSave(ThrowingRunnable operation, String filePath) {
        try {
            logger.debug("Executing shielded save operation for file: {}", filePath);
            operation.run();
            
        } catch (AccessDeniedException e) {
            logger.error("Access denied while saving file: {}", filePath, e);
            throw new SaveException(
                "Access denied: Cannot write to file " + filePath + ". Check file permissions.",
                e
            );
            
        } catch (IOException e) {
            logger.error("I/O error during save: {}", filePath, e);
            
            if (e.getMessage() != null && e.getMessage().contains("No space left")) {
                throw new SaveException(
                    "Insufficient disk space to save file: " + filePath,
                    e
                );
            }
            
            throw new SaveException(
                "Failed to save file " + filePath + ": " + e.getMessage(),
                e
            );
            
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory while saving file: {}", filePath, e);
            throw new SaveException(
                "Insufficient memory to save file: " + filePath,
                e
            );
            
        } catch (Exception e) {
            logger.error("Unexpected error during save: {}", filePath, e);
            throw new SaveException(
                "Unexpected error saving file " + filePath + ": " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Shields serialization operations by converting low-level exceptions
     * into domain-specific SerializationException.
     *
     * @param operation the serialization operation to execute
     * @param dataType  description of the data being serialized
     * @param <T>       the type of serialized data
     * @return the serialized data
     * @throws SerializationException if serialization fails
     */
    public static <T> T shieldSerialization(ThrowingSupplier<T> operation, String dataType) {
        try {
            logger.debug("Executing shielded serialization for: {}", dataType);
            return operation.get();
            
        } catch (java.io.NotSerializableException e) {
            logger.error("Object not serializable: {}", dataType, e);
            throw new SerializationException(
                "Cannot serialize " + dataType + ": Object is not serializable. " +
                "Ensure all classes implement Serializable.",
                e
            );
            
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory during serialization: {}", dataType, e);
            throw new SerializationException(
                "Insufficient memory to serialize " + dataType,
                e
            );
            
        } catch (Exception e) {
            logger.error("Serialization failed for: {}", dataType, e);
            throw new SerializationException(
                "Failed to serialize " + dataType + ": " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Shields deserialization operations by converting low-level exceptions
     * into domain-specific DeserializationException.
     *
     * @param operation the deserialization operation to execute
     * @param dataType  description of the data being deserialized
     * @param <T>       the type of deserialized object
     * @return the deserialized object
     * @throws DeserializationException if deserialization fails
     */
    public static <T> T shieldDeserialization(ThrowingSupplier<T> operation, String dataType) {
        try {
            logger.debug("Executing shielded deserialization for: {}", dataType);
            return operation.get();
            
        } catch (ClassNotFoundException e) {
            logger.error("Class not found during deserialization: {}", dataType, e);
            throw new DeserializationException(
                "Cannot deserialize " + dataType + ": Required class not found. " +
                "Class: " + e.getMessage(),
                e
            );
            
        } catch (java.io.InvalidClassException e) {
            logger.error("Invalid class during deserialization: {}", dataType, e);
            throw new DeserializationException(
                "Cannot deserialize " + dataType + ": Class version mismatch. " +
                "The saved data may be from an incompatible version.",
                e
            );
            
        } catch (com.google.gson.JsonSyntaxException e) {
            logger.error("Invalid JSON syntax: {}", dataType, e);
            throw new DeserializationException(
                "Cannot deserialize " + dataType + ": Invalid JSON format. " +
                "The file may be corrupted.",
                e
            );
            
        } catch (com.google.gson.JsonParseException e) {
            logger.error("JSON parsing failed: {}", dataType, e);
            throw new DeserializationException(
                "Cannot deserialize " + dataType + ": JSON parsing error. " +
                e.getMessage(),
                e
            );
            
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory during deserialization: {}", dataType, e);
            throw new DeserializationException(
                "Insufficient memory to deserialize " + dataType,
                e
            );
            
        } catch (Exception e) {
            logger.error("Deserialization failed for: {}", dataType, e);
            throw new DeserializationException(
                "Failed to deserialize " + dataType + ": " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Shields load operations by converting exceptions into LoadException.
     *
     * @param operation the load operation to execute
     * @param fileName  the file being loaded
     * @param <T>       the type being loaded
     * @return the loaded data
     * @throws LoadException if the load operation fails
     */
    public static <T> T shieldLoad(ThrowingSupplier<T> operation, String fileName) {
        try {
            logger.debug("Executing shielded load operation for: {}", fileName);
            return operation.get();
            
        } catch (ReadException | DeserializationException e) {
            // Already domain exceptions, wrap in LoadException
            logger.error("Load failed due to read/deserialization error: {}", fileName, e);
            throw new LoadException(
                "Failed to load game from " + fileName + ": " + e.getMessage(),
                e
            );
            
        } catch (Exception e) {
            logger.error("Unexpected error during load: {}", fileName, e);
            throw new LoadException(
                "Unexpected error loading game from " + fileName + ": " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Functional interface for operations that may throw exceptions.
     *
     * @param <T> the type of result
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    /**
     * Functional interface for operations that may throw exceptions and return void.
     */
    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
