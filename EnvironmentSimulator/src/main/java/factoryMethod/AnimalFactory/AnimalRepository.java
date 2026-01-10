package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.AnimalState;

import java.io.Serializable;
import java.util.*;

public class AnimalRepository implements Serializable {

    private final Map<String, Animal> animals = new HashMap<>();

    public void add(Animal animal) {
        animals.put(animal.getId(), animal);
    }

    public void clear() {
        animals.clear();
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
            System.out.println(animal);
            animalStates.add(new AnimalState(animal));
        }
        return new AnimalRepositoryState(animalStates);
    }

    public void fromState(AnimalRepositoryState state) throws IllegalArgumentException {
        this.animals.clear();
        AnimalFactory factory = null;
        System.out.println("creating animals from state");
        System.out.println(this.getAll());

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
            factory.createAnimalFromState(this, animalState);
            System.out.println(this.getAll());

        }
        System.out.println(this.getAll());

    }

    public List<String> listAll() {
        List<String> listOfIds = new ArrayList<>();
        for (Animal animal : this.getAll()) {
            listOfIds.add(animal.getId());
        }
        return listOfIds;
    }
}
