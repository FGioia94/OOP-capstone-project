package strategy.IO;

/**
 * Exception thrown when loading a game snapshot fails.
 * <p>
 * Wraps underlying I/O or deserialization exceptions that occur during load operations.
 * </p>
 */
public class LoadException extends RuntimeException {

  public LoadException(String message) {
    super(message);
  }

  public LoadException(String message, Throwable cause) {
    super(message, cause);
  }
}