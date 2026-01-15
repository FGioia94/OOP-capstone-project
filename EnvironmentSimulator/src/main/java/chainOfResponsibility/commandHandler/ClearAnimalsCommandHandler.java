package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command handler for clearing all animals from the game.
 * <p>
 * Handles the "clearAnimals" command, which removes all animals from the animal repository
 * after user confirmation. This is a destructive operation that cannot be undone.
 * </p>
 */
public class ClearAnimalsCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ClearAnimalsCommandHandler.class);

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("clearAnimals")) {
            if (askAreYouSure(scanner)) {
                int count = gameLoop.animalRepository.getAll().size();
                logger.info("Clearing {} animals from repository", count);

                gameLoop.animalRepository.clear();

                System.out.println("All animals have been removed.");
            }
            return true;

        }

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}