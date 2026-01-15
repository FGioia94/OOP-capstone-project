package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalComponent;
import factoryMethod.AnimalFactory.AnimalPack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.*;

/**
 * Command handler for grouping animals into packs.
 * <p>
 * Handles the "pack" command, which prompts the user for animal IDs and creates
 * a pack composite containing those animals. This demonstrates the Composite pattern.
 * </p>
 */
public class PackCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(PackCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        // Global cancel support at command level
        if (isCancel(cmd)) {
            System.out.println("Operation cancelled.");
            logger.info("Pack operation cancelled by user.");
            return true;
        }

        if (cmd.equalsIgnoreCase("pack")) {

            logger.info("Pack command received. Requesting animal IDs.");

            List<AnimalComponent> animalList = new ArrayList<>();
            boolean validInput = false;


            while (!validInput) {

                System.out.println("Please enter the IDs of the animals to pack separated by commas (or 'cancel' to cancel):");
                String ids = scanner.nextLine().trim();

                // Cancel inside loop
                if (checkCancel(ids)) {
                    return true;
                }

                logger.debug("User entered IDs: '{}'", ids);

                if (ids.matches("^[^,]+(,[^,]+)*$")) {

                    String[] idList = ids.split(",");

                    for (String rawId : idList) {
                        String id = rawId.trim();

                        AnimalComponent animal = gameLoop.animalRepository.getAnimalById(id);

                        if (animal == null) {
                            logger.warn("No animal found with ID '{}'. Skipping.", id);
                            System.out.println("No animal found with ID: " + id + " — Skipping.");
                        } else {
                            logger.debug("Animal found: ID={}, Type={}", animal.getId(), animal.getAnimalType());
                            animalList.add(animal);
                        }
                    }

                    if (animalList.isEmpty()) {
                        logger.warn("No valid animals found in user input.");
                        System.out.println("No valid animals found. Try again.");
                    } else {
                        validInput = true;
                    }

                } else {
                    logger.warn("Invalid ID input format: '{}'", ids);
                    System.out.println("Invalid input. Please enter comma-separated IDs.");
                }
            }

            validInput = false;
            AnimalPack newPack = null;

            logger.info("Requesting destination pack ID.");


            while (!validInput) {

                System.out.println("Type the ID of the destination pack (or 0 to create a new pack, or 'cancel' to cancel):");
                String packId = scanner.nextLine().trim();

                // Cancel inside loop
                if (checkCancel(packId)) {
                    return true;
                }

                logger.debug("User entered pack ID: '{}'", packId);

                if (packId.equals("0")) {

                    String newPackId = UUID.randomUUID().toString();
                    newPack = new AnimalPack(newPackId);

                    logger.info("Creating new pack with ID={}", newPackId);

                    for (AnimalComponent animal : animalList) {
                        newPack.add(animal);
                        animal.setPack(newPack.getId());

                        logger.debug("Animal ID {} added to NEW Pack {}", animal.getId(), newPackId);

                        System.out.println("Animal ID " + animal.getId() +
                                " added to NEW Pack " + newPackId);
                    }

                    validInput = true;

                } else {

                    AnimalComponent packComponent = gameLoop.animalRepository.getAnimalById(packId);

                    if (packComponent instanceof AnimalPack pack) {

                        logger.info("Adding animals to existing pack ID={}", packId);

                        for (AnimalComponent animal : animalList) {
                            pack.add(animal);
                            animal.setPack(pack.getId());

                            logger.debug("Animal ID {} added to Pack {}", animal.getId(), packId);

                            System.out.println("Animal ID " + animal.getId() +
                                    " added to Pack ID " + packId);
                        }

                        validInput = true;

                    } else {
                        logger.warn("Pack ID '{}' not found or does not refer to a pack.", packId);
                        System.out.println("Pack not found or ID does not refer to a pack.");
                    }
                }
            }

            if (newPack != null) {
                logger.info("Registering new pack ID={} into repository.", newPack.getId());
                gameLoop.animalRepository.add(newPack);
            }

            logger.info("Pack operation completed successfully.");
            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}