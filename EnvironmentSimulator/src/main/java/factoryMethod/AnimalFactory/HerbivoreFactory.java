package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import java.util.List;

public class HerbivoreFactory extends AnimalFactory {
    @Override
    public Animal createAnimal(MapBuilder builder,
                               AnimalRepository repository,
                               Position position,
                               String sex,
                               int hp,
                               int exp,
                               int level) {

        validateCreation(builder, position, sex, hp, exp, level);
        Animal herbivore = new Herbivore(
                position,
                sex,
                hp,
                exp,
                level);
        repository.add(herbivore);
        return herbivore;
    }


}
