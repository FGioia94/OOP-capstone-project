package template.Game;

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