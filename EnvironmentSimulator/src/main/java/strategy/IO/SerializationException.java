package strategy.IO;

/**
 * Exception thrown when serialization of a game snapshot fails.
 * <p>
 * Wraps underlying exceptions that occur during the serialization process.
 * </p>
 */
public class SerializationException extends RuntimeException {
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}