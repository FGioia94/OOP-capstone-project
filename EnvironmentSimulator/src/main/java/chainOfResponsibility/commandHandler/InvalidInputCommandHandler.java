package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvalidInputCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(InvalidInputCommandHandler.class);

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {

        logger.warn("Invalid command received: '{}'", cmd);

        System.out.println("Invalid command: " + cmd + ". Please try again.");

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}