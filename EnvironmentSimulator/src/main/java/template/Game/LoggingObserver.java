package template.Game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Observer that logs game events to the logging system.
 * <p>
 * Forwards all game events to the Log4j logger for persistent recording.
 * Part of the Observer pattern implementation.
 * </p>
 */
public class LoggingObserver implements GameObserver {

    private static final Logger logger = LogManager.getLogger(LoggingObserver.class);

    @Override
    public void onEvent(GameEvent event) {
        logger.info("[{}] {}", event.getType(), event.getMessage());
    }
}