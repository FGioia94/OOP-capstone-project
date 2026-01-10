package memento.GameSnapshot;

import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.Animal;
import factoryMethod.AnimalFactory.AnimalComponent;

import java.io.Serializable;

public record AnimalState(
        String id,
        int range,
        Position position,
        String sex,
        int hp,
        int exp,
        int level,
        String animalType,
        String pack
) implements Serializable {

    public AnimalState(AnimalComponent animal) {
        this(
                animal.getId(),
                animal.getRange(),
                animal.getPosition(),
                animal.getSex(),
                animal.getHp(),
                animal.getExp(),
                animal.getLevel(),
                animal.getAnimalType(),
                animal.getPack() != null ? animal.getPack() : null
        );
    }
}