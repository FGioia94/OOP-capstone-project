package exceptionShielding;

/**
 * Base exception for all game-related exceptions.
 * This is the root of the exception hierarchy for domain-specific exceptions.
 * All game exceptions should extend this class to provide a consistent
 * exception handling mechanism across the application.
 */
public class GameException extends RuntimeException {

    /**
     * Constructs a new game exception with the specified detail message.
     *
     * @param message the detail message
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructs a new game exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause (which is saved for later retrieval)
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new game exception with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval)
     */
    public GameException(Throwable cause) {
        super(cause);
    }
}
