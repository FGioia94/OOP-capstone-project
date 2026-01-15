package template.Game;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.*;

/**
 * Concrete game implementation for admin mode.
 * <p>
 * Extends the Game template to provide full debugging capabilities,
 * verbose logging, and access to admin-only features like reflection-based
 * animal inspection.
 * </p>
 */
public class GameAdmin extends Game {

    @Override
    protected void initializeGame() {
        Configurator.setRootLevel(Level.DEBUG);
        logger.debug("Admin mode: logging set to DEBUG level");
        logger.info("Initializing admin game...");
    }

    @Override
    protected void runGameLoop(MapBuilder builder, AnimalRepository repository) {
        this.gameLoop = new GameLoop(builder, repository, true); // Pass true for admin mode
        logger.debug("Running admin game loop with full verbosity...");
        gameLoop.run();
    }

    @Override
    protected MapBuilder createMapBuilder() {
        logger.debug("Creating MapBuilder...");
        return new MapBuilder();
    }

    @Override
    protected AnimalRepository createAnimalRepository() {
        logger.debug("Creating AnimalRepository...");
        return new AnimalRepository();
    }

    @Override
    protected void initialSetup() {
        logger.debug("Performing initial setup for admin game");

        Map<String, Integer> values = ask();
        logger.debug("User provided setup values: {}", values);

        builder.setWidth(values.get("width"));
        builder.setHeight(values.get("height"));
        builder.setObstaclesPositions(builder.spawnElements(values.get("obstacles"), builder.getObstaclesPositions()));
        builder.setGrassPositions(builder.spawnElements(values.get("grass"), builder.getGrassPositions()));
        builder.setWaterPositions(builder.spawnElements(values.get("water"), builder.getWaterPositions()));

        logger.info("Map built with dimensions {}x{}, obstacles: {}, grass: {}, water: {}",
                values.get("width"), values.get("height"),
                values.get("obstacles"), values.get("grass"), values.get("water"));
    }

    private Map<String, Integer> ask() {

        Scanner scanner = new Scanner(System.in);
        logger.info("Enter your desired setup values:");

        Map<String, String> prompts = new LinkedHashMap<>();
        prompts.put("width", "Map width");
        prompts.put("height", "Map height");
        prompts.put("water", "Number of water tiles");
        prompts.put("grass", "Number of grass tiles");
        prompts.put("obstacles", "Number of obstacles");

        Map<String, Integer> values = new HashMap<>();

        for (Map.Entry<String, String> entry : prompts.entrySet()) {

            String key = entry.getKey();
            String label = entry.getValue();

            while (true) {
                try {
                    System.out.print(label + ": ");
                    String input = scanner.nextLine().trim();

                    int amount = Integer.parseInt(input);

                    if (amount < 0) {
                        logger.warn("Negative number entered for {}. Please enter a non-negative integer.", key);
                        continue;
                    }

                    if (amount >= 100) {
                        logger.warn("{} entered for {}. Please enter a number less than 100 to avoid overpopulation.", amount, key);
                        continue;
                    }

                    values.put(key, amount);
                    break;

                } catch (NumberFormatException e) {
                    logger.warn("Invalid input for {}. Please enter a valid integer.", key);
                }
            }
        }

        return values;
    }
}