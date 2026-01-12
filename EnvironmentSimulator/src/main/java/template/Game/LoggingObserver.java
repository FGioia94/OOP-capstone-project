package template.Game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggingObserver implements GameObserver {

    private static final Logger logger = LogManager.getLogger(LoggingObserver.class);

    @Override
    public void onEvent(GameEvent event) {
        logger.info("[{}] {}", event.getType(), event.getMessage());
    }
}