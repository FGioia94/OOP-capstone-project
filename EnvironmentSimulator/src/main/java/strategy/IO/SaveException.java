package strategy.IO;

/**
 * Exception thrown when saving a game snapshot fails.
 * <p>
 * Wraps underlying I/O or serialization exceptions that occur during save operations.
 * </p>
 */
public class SaveException extends RuntimeException {

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable cause) {
        super(message, cause);
    }
}