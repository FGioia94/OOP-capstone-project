package template.Game;

/**
 * Represents a game event with type, message, and optional payload.
 * <p>
 * Used in the Observer pattern to notify observers about significant
 * game occurrences. Events can carry structured data in the payload field.
 * </p>
 */
public class GameEvent {
    private final GameEventType type;
    private final String message;
    private final Object payload; // optional structured data

    public GameEvent(GameEventType type, String message) {
        this(type, message, null);
    }

    public GameEvent(GameEventType type, String message, Object payload) {
        this.type = type;
        this.message = message;
        this.payload = payload;
    }

    public GameEventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public Object getPayload() {
        return payload;
    }
}