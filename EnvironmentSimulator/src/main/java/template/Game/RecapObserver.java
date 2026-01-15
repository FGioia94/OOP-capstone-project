package template.Game;

/**
 * Observer that collects game events and provides formatted summaries.
 * <p>
 * Buffers events during a game tick and allows flushing the accumulated
 * messages for display. Part of the Observer pattern implementation.
 * </p>
 */
public class RecapObserver implements GameObserver {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void onEvent(GameEvent event) {
        buffer.append(event.getMessage()).append("\n");
    }

    public String flush() {
        String out = buffer.toString();
        buffer.setLength(0);
        return out;
    }
}