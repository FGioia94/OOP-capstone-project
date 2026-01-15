package template.Game;

/**
 * Enumeration of all possible game event types.
 * <p>
 * Used to categorize events in the Observer pattern, allowing observers
 * to filter or react differently based on event type.
 * </p>
 */
public enum GameEventType {
    ATTACK,
    DEATH,
    REPRODUCTION,
    LEVEL_UP,
    RESOURCE_CONSUMPTION,
    RESOURCE_RESPAWN,
    HUNGER,
    MOVE,
    USER_ACTION
}

