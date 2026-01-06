package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.AnimalState;

import java.util.*;

public class AnimalRepository {

    private final Map<String, Animal> animals = new HashMap<>();

    public void add(Animal animal) {
        animals.put(animal.getId(), animal);
    }

    public Animal get(String id) {
        return animals.get(id);
    }

    public Collection<Animal> getAll() {
        return animals.values();
    }

    public void remove(String id) {
        animals.remove(id);
    }

    public Collection<Animal> getAllByType(String type) {
        Collection<Animal> result = new ArrayList<>();
        for (Animal animal : animals.values()) {
            if (animal.animalType.equals(type)) {
                result.add(animal);
            }
        }
        return result;
    }

    public AnimalRepositoryState toState() {
        List<AnimalState> animalStates = new ArrayList<>();
        for (Animal animal : this.getAll()) {
            animalStates.add(new AnimalState(animal));
        }
        return new AnimalRepositoryState(animalStates);
    }

    public static AnimalRepository fromState(AnimalRepositoryState state) throws IllegalArgumentException {
        AnimalRepository repository = new AnimalRepository();
        AnimalFactory factory = null;
        for (AnimalState animalState : state.animals()) {
            switch (animalState.animalType()) {
                case "Carnivore" -> {
                    factory = new CarnivoreFactory();
                }
                case "Herbivore" -> {
                    factory = new HerbivoreFactory();
                }
                default -> {
                    throw new IllegalArgumentException("Unknown animal type: "
                            + animalState.animalType());
                }
            }
            repository.add(factory.createAnimalFromState(repository, animalState));
        }

        return repository;
    }
}
