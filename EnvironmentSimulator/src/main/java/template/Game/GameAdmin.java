package template.Game;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.*;

public class GameAdmin extends Game {

    @Override
    protected void initializeGame() {
        Configurator.setLevel(getClass().getName(), Level.DEBUG);

        logger.debug("Admin mode: logging set to DEBUG level");
        logger.info("Initializing admin game...");
    }

    @Override
    protected void runGameLoop(MapBuilder builder, AnimalRepository repository) {
        this.gameLoop = new GameLoop(builder, repository);
        gameLoop.run();
        logger.debug("Running admin game loop with full verbosity...");
    }

    @Override
    protected MapBuilder createMapBuilder() {
        logger.debug("Creating MapBuilder...");
        return new MapBuilder();
    }

    @Override
    protected AnimalRepository createAnimalRepository() {
        return new AnimalRepository();
    }

    @Override
    protected void initialSetup() {
        logger.debug("Performing initial setup for admin game");
        Map<String, Integer> values = askAnimalAmounts();
        logger.debug("User provided setup values: {}", values.toString());
        builder.setWidth(values.get("width"));
        builder.setHeight(values.get("height"));
        builder.setObstaclesPositions(builder.spawnElements(values.get("obstacles"), builder.getObstaclesPositions()));
        builder.setGrassPositions(builder.spawnElements(values.get("grass"), builder.getGrassPositions()));
        builder.setWaterPositions(builder.spawnElements(values.get("water"), builder.getWaterPositions()));
        logger.info("Map built with dimensions {}x{}, obstacles: {}, grass: {}, water: {}",
                values.get("width"), values.get("height"),
                values.get("obstacles"), values.get("grass"), values.get("water"));
    }

    private Map<String, Integer> askAnimalAmounts() {
        Scanner scanner = new Scanner(System.in);
        logger.info("Enter your desired setup values:");
        int carnivores = -1;
        Map<String, Integer> values = new HashMap<>(Map.of(
                "width", -1,
                "height", -1,
                "water", -1,
                "grass", -1,
                "obstacles", -1
        ));
        for (String key : values.keySet()) {
            while (values.get(key) == -1) {
                try {
                    System.out.print(key + ": ");
                    String input = scanner.nextLine();
                    int amount = Integer.parseInt(input);
                    if (amount < 0) {
                        logger.warn("Negative number entered. Please enter a non-negative integer.");
                    } else if (amount >= 100) {
                        logger.warn(" entered. Please enter a number less than 100 to avoid overpopulation.");
                    } else {
                        values.put(key, amount);
                    }
                } catch (NumberFormatException e) {
                    logger.warn("Invalid input. Please enter a valid integer.");
                }
            }
        }

        return values;
    }
}