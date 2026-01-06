package factoryMethod.AnimalFactory;

import builder.MapBuilder.Position;

import java.util.List;

public class Carnivore extends Animal {
    public Carnivore(
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {
        int range = 5;
        String animalType = "Carnivore";
        super(range, position, sex, hp, exp, level, animalType);
    }


    @Override
    Animal reproduce() {
        return null;
    }
}
