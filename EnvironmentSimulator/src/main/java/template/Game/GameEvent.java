package template.Game;

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