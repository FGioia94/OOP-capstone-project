package chainOfResponsibility.commandHandler;

import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CreateCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(CreateCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        // Global cancel support
        if (isCancel(cmd)) {
            System.out.println("Operation cancelled.");
            logger.info("Create operation cancelled by user.");
            return true;
        }

        if (cmd.equalsIgnoreCase("create")) {

            logger.info("Create command received. Asking user for animal specifications.");

            Map<String, String> animalSpecs = askAnimalSpecs(scanner);
            if (animalSpecs == null) { // cancelled inside askAnimalSpecs
                return true;
            }

            String type = animalSpecs.get("typeOfAnimal");
            String sex = animalSpecs.get("sex");

            logger.debug("User selected type='{}', sex='{}'", type, sex);

            AnimalFactory factory =
                    type.equals("carnivore") ? new CarnivoreFactory() : new HerbivoreFactory();

            Position spawnPos = gameLoop.builder.getRandomValidPosition();

            if (spawnPos == null) {
                logger.error("No valid spawn position available. Cannot create animal.");
                System.out.println("No valid position available to spawn a new animal.");
                return true;
            }

            logger.debug("Spawning new {} at {}", type, spawnPos);

            Animal animal = factory.buildAnimal(
                    gameLoop.builder,
                    gameLoop.animalRepository,
                    spawnPos,
                    sex,
                    100,
                    0,
                    1
            );

            gameLoop.animalRepository.add(animal);

            logger.info("Created new {} with ID={} at {}", type, animal.getId(), spawnPos);

            System.out.println("New " + type
                    + " created with ID: "
                    + animal.getId()
                    + " at position "
                    + spawnPos);

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }

    private Map<String, String> askAnimalSpecs(Scanner scanner) {

        logger.debug("Prompting user for animal type and sex.");

        boolean validInput = false;
        String typeOfAnimal = "";

        while (!validInput) {
            System.out.println("Enter the type of animal to create (carnivore/herbivore) or 'cancel' to cancel:");
            typeOfAnimal = scanner.nextLine().trim().toLowerCase();

            if (checkCancel(typeOfAnimal)) {
                return null;
            }

            if (typeOfAnimal.equals("carnivore") || typeOfAnimal.equals("herbivore")) {
                logger.debug("User selected animal type: {}", typeOfAnimal);
                validInput = true;
            } else {
                logger.warn("Invalid animal type entered: {}", typeOfAnimal);
                System.out.println("Invalid type. Please enter 'carnivore' or 'herbivore'.");
            }
        }

        validInput = false;
        String sex = "";

        while (!validInput) {
            System.out.println("Enter the sex of the animal to create (m/f) or 'cancel' to cancel:");
            sex = scanner.nextLine().trim().toLowerCase();

            if (checkCancel(sex)) {
                return null;
            }

            if (sex.equals("m") || sex.equals("f")) {
                logger.debug("User selected sex: {}", sex);
                validInput = true;
            } else {
                logger.warn("Invalid sex entered: {}", sex);
                System.out.println("Invalid sex. Please enter 'm' or 'f'.");
            }
        }

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("typeOfAnimal", typeOfAnimal);
        infoMap.put("sex", sex);

        logger.debug("Animal specs collected: {}", infoMap);

        return infoMap;
    }
}