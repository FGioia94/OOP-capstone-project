package strategy.IO;

/**
 * Exception thrown when deserialization of data into a game snapshot fails.
 * <p>
 * Wraps underlying exceptions that occur during the deserialization process.
 * </p>
 */
public class DeserializationException extends RuntimeException {
    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }
}