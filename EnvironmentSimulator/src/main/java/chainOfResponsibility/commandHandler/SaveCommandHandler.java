package chainOfResponsibility.commandHandler;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import strategy.IO.SaveGame;
import template.Game.GameLoop;

import java.util.Scanner;

/**
 * Command handler for saving the current game state.
 * <p>
 * Handles the "save" or "s" command, which prompts the user for a file name and format,
 * then saves a snapshot of the current game state to disk.
 * </p>
 */
public class SaveCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(SaveCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("s") || cmd.equalsIgnoreCase("save")) {


            logger.info("Save command received. Requesting file name and format.");

            String fileName = this.askFileName(scanner);
            if (fileName == null) {
                logger.info("Save operation cancelled during file name input.");
                System.out.println("Save cancelled.");
                return true;
            }

            String format = this.askFormat(scanner);
            if (format == null) {
                logger.info("Save cancelled during format input.");
                System.out.println("Save cancelled.");
                return true;
            }

            logger.debug("User selected file='{}', format='{}'", fileName, format);

            if (!this.askAreYouSure(scanner)) {
                logger.warn("User cancelled the Save operation.");
                System.out.println("Save cancelled.");
                return true;
            }

            logger.info("User confirmed save operation. Attempting to save snapshot.");


            try {
                logger.info("Creating game snapshot for saving.");

                GameSnapshot snapshot = new GameSnapshot(
                        gameLoop.animalRepository,
                        gameLoop.builder
                );

                logger.debug("Snapshot created. Saving to '{}.{}'", fileName, format);

                SaveGame.save(fileName, snapshot, format);

                logger.info("Game successfully saved to '{}.{}'", fileName, format);
                System.out.println("Game saved successfully.");

                return true;

            } catch (Exception e) {

                logger.error("Failed to save game to '{}.{}': {}", fileName, format, e.getMessage(), e);
                System.out.println("Failed to save the game: " + e.getMessage());
                return true;
            }
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}