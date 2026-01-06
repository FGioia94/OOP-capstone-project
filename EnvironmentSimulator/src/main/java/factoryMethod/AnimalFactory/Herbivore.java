package factoryMethod.AnimalFactory;

import java.util.List;

import builder.MapBuilder.Position;


public class Herbivore extends Animal {
    public Herbivore(Position position,
                     String sex,
                     int hp,
                     int exp,
                     int level) {
        int range = 3;
        String animalType = "Herbivore";
        super(range, position, sex, hp, exp, level, animalType);
    }


    @Override
    Animal reproduce() {
        return null;
    }
}
