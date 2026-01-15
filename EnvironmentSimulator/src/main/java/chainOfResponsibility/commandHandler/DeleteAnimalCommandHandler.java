package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Command handler for deleting a specific animal from the game.
 * <p>
 * Handles the "deleteAnimal" command, which prompts the user for an animal ID
 * and removes that animal from the repository after confirmation.
 * </p>
 */
public class DeleteAnimalCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(DeleteAnimalCommandHandler.class);

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {

        // Global cancel support
        if (isCancel(cmd)) {
            System.out.println("Operation cancelled.");
            logger.info("DeleteAnimal operation cancelled by user.");
            return true;
        }

        if (cmd.equalsIgnoreCase("deleteAnimal")) {

            logger.info("DeleteAnimal command received. Requesting animal ID from user.");

            System.out.println("Enter the ID of the animal to delete (or 'cancel' to cancel):");
            String idInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";

            // Cancel inside the handler
            if (checkCancel(idInput)) {
                return true;
            }

            logger.debug("User entered ID: '{}'", idInput);

            if (askAreYouSure(scanner)) {
                try {
                    gameLoop.animalRepository.remove(idInput);

                    logger.info("Animal with ID '{}' successfully deleted.", idInput);
                    System.out.println("Animal with ID " + idInput + " has been deleted.");
                    return true;

                } catch (AnimalNotFoundException e) {
                    logger.error("Failed to delete animal with ID '{}': Animal not found.", idInput, e);
                    System.out.println("Animal with ID " + idInput + " not found.");
                    return true;
                } catch (Exception e) {
                    logger.error("Unexpected error deleting animal with ID '{}': {}", idInput, e.getMessage(), e);
                    System.out.println("Failed to delete animal: " + e.getMessage());
                    return true;
                }
            }
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}