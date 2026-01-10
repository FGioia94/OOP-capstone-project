package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;

import java.util.List;
import java.util.UUID;

public class HerbivoreFactory extends AnimalFactory {
    @Override
    public Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level) {

        String id = UUID.randomUUID().toString();

        Animal herbivore = new Herbivore(
                id,
                position,
                sex,
                hp,
                exp,
                level);
        repository.add(herbivore);
        return herbivore;
    }


}
