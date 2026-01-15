package template.Game;

/**
 * Observer interface for receiving game event notifications.
 * <p>
 * Part of the Observer pattern, allowing different components to react
 * to game events (attacks, deaths, resource consumption, etc.) without
 * tight coupling to the GameLoop.
 * </p>
 */
public interface GameObserver {
    void onEvent(GameEvent event);
}
