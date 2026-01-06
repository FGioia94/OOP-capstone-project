package strategy.IO;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;

import java.io.Serializable;

public class GameSnapshot implements Serializable {
    private final AnimalRepository animalRepository;
    private final MapBuilder builder;

    public GameSnapshot(AnimalRepository animalRepository, MapBuilder builder) {
        this.animalRepository = animalRepository;
        this.builder = builder;
    }
}
