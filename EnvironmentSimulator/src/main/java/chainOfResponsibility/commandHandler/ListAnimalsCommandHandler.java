package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.Scanner;

/**
 * Command handler for listing all animals in the game.
 * <p>
 * Handles the "listAnimals" command, which displays detailed information about
 * each animal currently in the repository, excluding pack entities.
 * </p>
 */
public class ListAnimalsCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ListAnimalsCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("listAnimals")) {

            logger.info("ListAnimals command received.");

            var animals = gameLoop.animalRepository.getAllExceptPacks();

            logger.debug("Listing {} animals (excluding packs).", animals.size());

            if (animals.isEmpty()) {
                logger.warn("No animals found to list.");
                System.out.println("No animals present on the map.");
                return true;
            }

            animals.forEach(animal -> {
                logger.trace("Animal listed: ID={}, Type={}, Sex={}, Pos={}, HP={}, EXP={}, Level={}, Pack={}",
                        animal.getId(),
                        animal.getAnimalType(),
                        animal.getSex(),
                        animal.getPosition(),
                        animal.getHp(),
                        animal.getExp(),
                        animal.getLevel(),
                        animal.getPack() != null ? animal.getPack() : "None"
                );

                System.out.println(
                        "ID: " + animal.getId() +
                                ", Type: " + animal.getAnimalType() +
                                ", Sex: " + animal.getSex() +
                                ", Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                                ", HP: " + animal.getHp() +
                                ", EXP: " + animal.getExp() +
                                ", Level: " + animal.getLevel() +
                                ", Pack: " + (animal.getPack() != null ? animal.getPack() : "None") +
                                "/"
                );
            });

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}