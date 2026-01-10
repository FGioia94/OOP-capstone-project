package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.util.List;
import java.util.UUID;

import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalState;

public class CarnivoreFactory extends AnimalFactory {

    @Override
    public Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {

        String id = UUID.randomUUID().toString();

        Animal carnivore = new Carnivore(
                id,
                position,
                sex,
                hp,
                exp,
                level);
        repository.add(carnivore);
        return carnivore;

    }
}
