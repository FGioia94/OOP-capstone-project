package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command handler for continuing to the next game turn.
 * <p>
 * Handles the "continue" or "c" command, which marks the current turn as finished
 * and allows the game loop to proceed to the next tick.
 * </p>
 */
public class ContinueCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ContinueCommandHandler.class);

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("continue") || cmd.equalsIgnoreCase("c"))  {

            logger.info("Continue command received. Marking turn as finished.");

            System.out.println("Turn finished. Continuing the game...");
            gameLoop.setTurnFinished(true);

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}