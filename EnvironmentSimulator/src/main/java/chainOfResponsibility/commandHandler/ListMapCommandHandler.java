package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.Scanner;

/**
 * Command handler for displaying map statistics.
 * <p>
 * Handles the "listMap" command, which shows the current counts of water, grass,
 * obstacles, and animals on the game map.
 * </p>
 */
public class ListMapCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ListMapCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("listMap")) {

            logger.info("ListMap command received.");

            int water = gameLoop.builder.getWaterPositions().size();
            int grass = gameLoop.builder.getGrassPositions().size();
            int obstacles = gameLoop.builder.getObstaclesPositions().size();
            int animals = gameLoop.animalRepository.getAllExceptPacks().size();

            logger.debug("Map stats -> water={}, grass={}, obstacles={}, animals={}",
                    water, grass, obstacles, animals);

            System.out.println(
                    "Map Details: + " +
                            "Water amount: " + water +
                            ", Grass amount: " + grass +
                            ", Obstacle amount: " + obstacles +
                            ", Animals amount: " + animals
            );

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}