package strategy.IO;

/**
 * Exception thrown when reading a file fails.
 * <p>
 * Wraps underlying I/O exceptions that occur during file read operations.
 * </p>
 */
public class ReadException extends RuntimeException {

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }
}