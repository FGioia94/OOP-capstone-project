package template.Game;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.HashMap;
import java.util.Map;

public class GameDefault extends Game {

    @Override
    protected void initializeGame() {
        Configurator.setRootLevel(Level.OFF);
    }

    @Override
    protected void runGameLoop(MapBuilder builder, AnimalRepository repository) {
        this.gameLoop = new GameLoop(builder, repository);
        gameLoop.run();
    }

    @Override
    protected MapBuilder createMapBuilder() {
        return new MapBuilder();
    }

    @Override
    protected AnimalRepository createAnimalRepository() {
        return new AnimalRepository();
    }

    @Override
    protected void initialSetup() {

        Map<String, Integer> values = askAnimalAmounts();

        builder.setWidth(values.get("width"));
        builder.setHeight(values.get("height"));

        builder.setObstaclesPositions(
                builder.spawnElements(values.get("obstacles"), builder.getObstaclesPositions())
        );

        builder.setGrassPositions(
                builder.spawnElements(values.get("grass"), builder.getGrassPositions())
        );

        builder.setWaterPositions(
                builder.spawnElements(values.get("water"), builder.getWaterPositions())
        );

    }

    private Map<String, Integer> askAnimalAmounts() {
        // Default values for non-admin mode
        return new HashMap<>(Map.of(
                "width", 20,
                "height", 20,
                "water", 20,
                "grass", 20,
                "obstacles", 20
        ));
    }
}