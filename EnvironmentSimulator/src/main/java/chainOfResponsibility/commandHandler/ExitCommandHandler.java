package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.Scanner;

/**
 * Command handler for exiting the game.
 * <p>
 * Handles the "exit" command, which prompts the user for confirmation
 * and then terminates the game loop if confirmed.
 * </p>
 */
public class ExitCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ExitCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("exit")) {

            logger.info("Exit command received. Asking user for confirmation.");

            System.out.println("Are you sure you want to exit? (yes/no)");
            String confirmation = scanner.hasNextLine()
                    ? scanner.nextLine().trim().toLowerCase()
                    : "";

            logger.debug("User exit confirmation input: '{}'", confirmation);

            if (confirmation.equals("yes") || confirmation.equals("y")) {

                logger.info("Exit confirmed by user. Requesting game shutdown.");
                gameLoop.requestExit();
                return true;

            } else {

                logger.warn("Exit cancelled by user.");
                return true;
            }
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}