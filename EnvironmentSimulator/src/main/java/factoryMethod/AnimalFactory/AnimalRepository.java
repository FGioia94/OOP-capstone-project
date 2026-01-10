package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.AnimalState;

import java.io.Serializable;
import java.util.*;

public class AnimalRepository implements Serializable {

    private final Map<String, AnimalComponent> animals = new HashMap<>();

    public void add(AnimalComponent animal) {
        animals.put(animal.getId(), animal);
    }

    public void clear() {
        animals.clear();
    }

    public AnimalComponent get(String id) {
        return animals.get(id);
    }

    public Collection<AnimalComponent> getAll() {
        return animals.values();
    }

    public void remove(String id) {
        animals.remove(id);
    }

    public Collection<AnimalComponent> getAllByType(String type) {
        Collection<AnimalComponent> result = new ArrayList<>();
        for (AnimalComponent animal : animals.values()) {
            if (animal.getAnimalType().equals(type)) {
                result.add(animal);
            }
        }
        return result;
    }

    public AnimalRepositoryState toState() {
        List<AnimalState> animalStates = new ArrayList<>();
        for (AnimalComponent animal : this.getAll()) {
            System.out.println(animal);
            animalStates.add(new AnimalState(animal));
        }
        return new AnimalRepositoryState(animalStates);
    }

    public void fromState(AnimalRepositoryState state) {
        animals.clear();

        Map<String, AnimalPack> packMap = new HashMap<>();

        for (AnimalState s : state.animals()) {
            if (s.animalType().equals("Pack")) {
                AnimalPack pack = new AnimalPack(s.id());
                packMap.put(s.id(), pack);
                this.add(pack);
            }
        }


        for (AnimalState s : state.animals()) {
            if (!s.animalType().equals("Pack")) {

                AnimalFactory factory = switch (s.animalType()) {
                    case "Carnivore" -> new CarnivoreFactory();
                    case "Herbivore" -> new HerbivoreFactory();
                    default -> throw new IllegalArgumentException("Unknown type: " + s.animalType());
                };

                AnimalComponent animal = factory.createAnimalFromState(this, s);


                if (s.pack() != null) {
                    AnimalPack pack = packMap.get(s.pack());

                    if (pack == null) {
                        throw new IllegalStateException("Pack " + s.pack() + " not found during load");
                    }

                    pack.add(animal);
                    animal.setPack(s.pack());
                }
            }
        }
    }

    public List<String> listAll() {
        List<String> listOfIds = new ArrayList<>();
        for (AnimalComponent animal : this.getAll()) {
            listOfIds.add(animal.getId());
        }
        return listOfIds;
    }

    public List<AnimalComponent> getAllExceptPacks() {
        List<AnimalComponent> result = new ArrayList<>();
        for (AnimalComponent animal : animals.values()) {
            if (!(animal instanceof AnimalPack)) {
                result.add(animal);
            }
        }
        return result;
    }

    public AnimalComponent getAnimalById(String id) {
        return animals.get(id);
    }
}
