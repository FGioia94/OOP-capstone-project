package chainOfResponsibility.commandHandler;

import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SpawnCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(SpawnCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("spawn")) {

            logger.info("Spawn command received. Requesting spawn specifications.");

            Map<String, String> specs = askSpecs(scanner);

            String type = specs.get("type");
            int amount;
            
            try {
                amount = Integer.parseInt(specs.get("amount"));
            } catch (NumberFormatException e) {
                logger.error("Invalid amount format: {}", specs.get("amount"), e);
                System.out.println("Invalid amount. Operation cancelled.");
                return true;
            }

            logger.debug("User selected type='{}', amount={}", type, amount);

            switch (type) {

                case "water": {
                    logger.info("Spawning {} water tiles.", amount);

                    List<Position> newWater =
                            gameLoop.builder.spawnElements(amount, gameLoop.builder.getWaterPositions());

                    logger.debug("Water spawn completed. New total={}", newWater.size());

                    gameLoop.builder.setWaterPositions(newWater);
                    break;
                }

                case "grass": {
                    logger.info("Spawning {} grass tiles.", amount);

                    List<Position> newGrass =
                            gameLoop.builder.spawnElements(amount, gameLoop.builder.getGrassPositions());

                    logger.debug("Grass spawn completed. New total={}", newGrass.size());

                    gameLoop.builder.setGrassPositions(newGrass);
                    break;
                }

                case "herbivore": {
                    logger.info("Spawning {} herbivores.", amount);

                    for (int i = 0; i < amount; i++) {
                        AnimalFactory factory = new HerbivoreFactory();
                        Position pos = gameLoop.builder.getRandomValidPosition();

                        if (pos == null) {
                            logger.error("No valid position available for herbivore spawn.");
                            continue;
                        }

                        factory.buildAnimal(
                                gameLoop.builder,
                                gameLoop.animalRepository,
                                pos,
                                Math.random() < 0.5 ? "m" : "f",
                                100,
                                0,
                                1
                        );

                        logger.debug("Herbivore spawned at {}", pos);
                    }
                    break;
                }

                case "carnivore": {
                    logger.info("Spawning {} carnivores.", amount);

                    for (int i = 0; i < amount; i++) {
                        AnimalFactory factory = new CarnivoreFactory();
                        Position pos = gameLoop.builder.getRandomValidPosition();

                        if (pos == null) {
                            logger.error("No valid position available for carnivore spawn.");
                            continue;
                        }

                        factory.buildAnimal(
                                gameLoop.builder,
                                gameLoop.animalRepository,
                                pos,
                                Math.random() < 0.5 ? "m" : "f",
                                100,
                                0,
                                1
                        );

                        logger.debug("Carnivore spawned at {}", pos);
                    }
                    break;
                }

                default: {
                    logger.warn("Invalid spawn type '{}'", type);
                    System.out.println("Invalid type specified.");
                    return true;
                }
            }

            logger.info("Spawn operation completed successfully.");
            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }

    private Map<String, String> askSpecs(Scanner scanner) {

        logger.debug("Prompting user for spawn type and amount.");

        boolean validInput = false;
        String typeOfObject = "";

        while (!validInput) {
            System.out.println("What do you want to spawn? (carnivore, herbivore, water, grass): ");
            typeOfObject = scanner.nextLine().trim().toLowerCase();

            if (typeOfObject.equals("carnivore") ||
                    typeOfObject.equals("herbivore") ||
                    typeOfObject.equals("water") ||
                    typeOfObject.equals("grass")) {

                logger.debug("User selected spawn type: {}", typeOfObject);
                validInput = true;

            } else {
                logger.warn("Invalid spawn type entered: {}", typeOfObject);
                System.out.println("Invalid type. Please enter 'carnivore', 'herbivore', 'grass' or 'water'.");
            }
        }

        validInput = false;
        String amount = "0";

        while (!validInput) {
            System.out.println("How many do you want to spawn? ");
            String amountInput = scanner.nextLine().trim();

            try {
                int amountInt = Integer.parseInt(amountInput);
                
                if (amountInt <= 0) {
                    logger.warn("User entered invalid amount: {}", amountInput);
                    System.out.println("Invalid amount. Please enter a number > 0.");
                } else {
                    amount = amountInput;
                    validInput = true;
                    logger.debug("User selected amount: {}", amount);
                }

            } catch (NumberFormatException e) {
                logger.error("Invalid number format for amount: {}", amountInput, e);
                System.out.println("Invalid amount. Please enter a number > 0.");
            }
        }

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("type", typeOfObject);
        infoMap.put("amount", amount);

        logger.debug("Spawn specs collected: {}", infoMap);

        return infoMap;
    }
}