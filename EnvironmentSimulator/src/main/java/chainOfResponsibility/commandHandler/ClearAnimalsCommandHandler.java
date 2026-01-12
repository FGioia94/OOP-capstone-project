package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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