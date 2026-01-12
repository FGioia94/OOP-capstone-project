package chainOfResponsibility.commandHandler;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strategy.IO.LoadGame;
import template.Game.GameLoop;

import java.util.Scanner;

public class LoadCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(LoadCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {


        if (cmd.equalsIgnoreCase("l") || cmd.equalsIgnoreCase("load")) {

            logger.info("Load command received. Requesting file name and format.");

            String fileName = this.askFileName(scanner);
            if (fileName == null) {
                logger.info("Load operation cancelled during file name input.");
                System.out.println("Load cancelled.");
                return true;
            }

            String format = this.askFormat(scanner);
            if (format == null) {
                logger.info("Load operation cancelled during format input.");
                System.out.println("Load cancelled.");
                return true;
            }

            logger.debug("User selected file='{}', format='{}'", fileName, format);

            if (this.askAreYouSure(scanner)) {
                logger.warn("User cancelled the load operation.");
                System.out.println("Load cancelled.");
                return true;
            }

            logger.info("User confirmed load operation. Attempting to load snapshot.");

            try {
                GameSnapshot snapshot = LoadGame.load(fileName, format);

                logger.debug("Snapshot loaded successfully. Applying state to game.");

                gameLoop.animalRepository.fromState(snapshot.getAnimalState());
                gameLoop.builder.fromState(snapshot.getMapState());

                logger.info("Game state successfully restored from '{}.{}'", fileName, format);

                System.out.println("Game loaded successfully.");
                return true;

            } catch (Exception e) {

                logger.error("Failed to load game from '{}.{}': {}", fileName, format, e.getMessage());

                System.out.println("Failed to load the game. Check logs for details.");
                throw new RuntimeException(e);
            }
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }


}