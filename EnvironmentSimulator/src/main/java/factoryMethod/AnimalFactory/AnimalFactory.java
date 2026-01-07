package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalState;

import java.util.List;

// ANIMAL FACTORY IS THE CREATOR OF THE FACTORY METHOD PATTERN
public abstract class AnimalFactory {
    // Declaring the FACTORY METHOD
    public Animal buildAnimal(MapBuilder builder,
                              AnimalRepository repository,
                              Position position,
                              String sex,
                              int hp,
                              int exp,
                              int level) {
        validateCreation(builder, position, sex, hp, exp, level);
        return createAnimal(repository, position, sex, hp, exp, level);
    }

    protected abstract Animal createAnimal(
            AnimalRepository repository,
            Position position,
            String sex,
            int hp,
            int exp,
            int level);

    public Animal createAnimalFromState(AnimalRepository repository,
                                        AnimalState state) {
        {
            return createAnimal(
                    repository,
                    state.position(),
                    state.sex(),
                    state.hp(),
                    state.exp(),
                    state.level());

        }
    }

    private static void validatePosition(MapBuilder builder, Position position) throws IllegalArgumentException {
        if (position == null) {
            throw new IllegalArgumentException("positions invalid");
        }
        int x = position.x();
        if (x < 0 || x >= builder.getWidth()) {
            throw new IllegalArgumentException("Invalid X position: " + x + " (valid 0.." + (builder.getWidth() - 1) + ")");
        }
        int y = position.y();
        if (y < 0 || y >= builder.getHeight()) {
            throw new IllegalArgumentException("Invalid Y position: " + y + " (valid 0.." + (builder.getHeight() - 1) + ")");
        }
    }

    private static void validateSex(String sex) throws IllegalArgumentException {
        if (sex == null) {
            throw new IllegalArgumentException("Sex must be 'M' or 'F'");
        }
        String s = sex.toUpperCase();
        if (!s.equals("M") && !s.equals("F")) {
            throw new IllegalArgumentException("Sex must be 'M' or 'F'");
        }
    }

    private static void validateHp(int hp) throws IllegalArgumentException {
        if (hp < 0) {
            throw new IllegalArgumentException("HP must be non-negative");
        }
    }

    private static void validateExp(int exp) throws IllegalArgumentException {
        if (exp < 0) {
            throw new IllegalArgumentException("EXP must be non-negative");
        }
    }

    private static void validateLevel(int level) throws IllegalArgumentException {
        if (level < 1) {
            throw new IllegalArgumentException("Level must be at least 1");
        }
    }


    public static void validateCreation(MapBuilder builder,
                                        Position position,
                                        String sex,
                                        int hp,
                                        int exp,
                                        int level) {
        validatePosition(builder, position);
        validateSex(sex);
        validateHp(hp);
        validateExp(exp);
        validateLevel(level);
    }

}
