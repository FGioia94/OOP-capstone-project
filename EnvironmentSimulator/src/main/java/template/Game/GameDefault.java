package template.Game;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameDefault extends Game {

    @Override
    protected void initializeGame() {
        Configurator.setLevel(getClass().getName(), Level.INFO);
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
        System.out.println("Building map of size " + values.get("width") + "x" + values.get("height"));
        builder.getObstaclesPositions();
        logger.info("obstacles");
        builder.setObstaclesPositions(builder.spawnElements(values.get("obstacles"), builder.getObstaclesPositions()));
        logger.info("obstaclesSet");

        builder.setGrassPositions(builder.spawnElements(values.get("grass"), builder.getGrassPositions()));
        builder.setWaterPositions(builder.spawnElements(values.get("water"), builder.getWaterPositions()));

        logger.info("Map built with dimensions {}x{}, obstacles: {}, grass: {}, water: {}",
                values.get("width"), values.get("height"),
                values.get("obstacles"), values.get("grass"), values.get("water"));
    }

    private Map<String, Integer> askAnimalAmounts() {
        return new HashMap<>(Map.of(
                "width", 20,
                "height", 20,
                "water", 20,
                "grass", 20,
                "obstacles", 20
        ));
    }
}