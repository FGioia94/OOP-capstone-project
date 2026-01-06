package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.util.List;

import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalState;

public class CarnivoreFactory extends AnimalFactory {

    @Override
    public Animal createAnimal(MapBuilder builder,
                               AnimalRepository repository,
                               Position position,
                               String sex,
                               int hp,
                               int exp,
                               int level) {

        if (builder != null) {
            validateCreation(builder, position, sex, hp, exp, level);
        }

        Animal carnivore = new Carnivore(
                position,
                sex,
                hp,
                exp,
                level);
        repository.add(carnivore);
        return carnivore;

    }
}
