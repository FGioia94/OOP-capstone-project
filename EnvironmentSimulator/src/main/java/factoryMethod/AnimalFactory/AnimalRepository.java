package factoryMethod.AnimalFactory;

import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.AnimalState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class AnimalRepository implements Serializable {

    private static final Logger logger = LogManager.getLogger(AnimalRepository.class);

    private final Map<String, AnimalComponent> animals = new HashMap<>();

    // ---------------------------------------------------------
    // CRUD OPERATIONS
    // ---------------------------------------------------------
    public void add(AnimalComponent animal) {
        animals.put(animal.getId(), animal);
        logger.debug("Added animal ID={} Type={}", animal.getId(), animal.getAnimalType());
    }

    public void clear() {
        logger.warn("Clearing entire AnimalRepository. {} animals removed.", animals.size());
        animals.clear();
    }

    public AnimalComponent get(String id) {
        AnimalComponent result = animals.get(id);

        if (result == null) {
            logger.warn("Lookup failed: no animal found with ID={}", id);
        } else {
            logger.trace("Lookup success: ID={} Type={}", id, result.getAnimalType());
        }

        return result;
    }

    public Collection<AnimalComponent> getAll() {
        logger.trace("Retrieving all animals. Count={}", animals.size());
        return animals.values();
    }

    public void remove(String id) {
        AnimalComponent removed = animals.remove(id);

        if (removed == null) {
            logger.warn("Attempted to remove ID={}, but no such animal exists.", id);
        } else {
            logger.debug("Removed animal ID={} Type={}", id, removed.getAnimalType());
        }
    }

    public Collection<AnimalComponent> getAllByType(String type) {
        List<AnimalComponent> result = new ArrayList<>();

        for (AnimalComponent animal : animals.values()) {
            if (animal.getAnimalType().equals(type)) {
                result.add(animal);
            }
        }

        logger.debug("Retrieved {} animals of type '{}'", result.size(), type);
        return result;
    }

    // ---------------------------------------------------------
    // SNAPSHOT SERIALIZATION
    // ---------------------------------------------------------
    public AnimalRepositoryState toState() {
        logger.info("Serializing AnimalRepository to snapshot. Total animals={}", animals.size());

        List<AnimalState> animalStates = new ArrayList<>();

        for (AnimalComponent animal : this.getAll()) {
            logger.trace("Serializing animal ID={} Type={}", animal.getId(), animal.getAnimalType());
            animalStates.add(new AnimalState(animal));
        }

        return new AnimalRepositoryState(animalStates);
    }

    // ---------------------------------------------------------
    // SNAPSHOT DESERIALIZATION
    // ---------------------------------------------------------
    public void fromState(AnimalRepositoryState state) {

        logger.info("Restoring AnimalRepository from snapshot. Total entries={}", state.animals().size());

        animals.clear();

        Map<String, AnimalPack> packMap = new HashMap<>();

        // First pass: create packs
        for (AnimalState s : state.animals()) {
            if (s.animalType().equals("Pack")) {

                logger.debug("Restoring Pack ID={}", s.id());

                AnimalPack pack = new AnimalPack(s.id());
                packMap.put(s.id(), pack);
                this.add(pack);
            }
        }

        // Second pass: create animals and assign to packs
        for (AnimalState s : state.animals()) {
            if (!s.animalType().equals("Pack")) {

                logger.debug("Restoring Animal ID={} Type={}", s.id(), s.animalType());

                AnimalFactory factory = switch (s.animalType()) {
                    case "Carnivore" -> new CarnivoreFactory();
                    case "Herbivore" -> new HerbivoreFactory();
                    default -> {
                        logger.error("Unknown animal type '{}' in snapshot", s.animalType());
                        throw new IllegalArgumentException("Unknown type: " + s.animalType());
                    }
                };

                AnimalComponent animal = factory.createAnimalFromState(this, s);

                if (s.pack() != null) {
                    AnimalPack pack = packMap.get(s.pack());

                    if (pack == null) {
                        logger.error("Snapshot inconsistency: Pack '{}' not found for animal ID={}", s.pack(), s.id());
                        throw new IllegalStateException("Pack " + s.pack() + " not found during load");
                    }

                    logger.debug("Assigning Animal ID={} to Pack ID={}", s.id(), s.pack());

                    pack.add(animal);
                    animal.setPack(s.pack());
                }
            }
        }

        logger.info("AnimalRepository successfully restored from snapshot.");
    }

    // ---------------------------------------------------------
    // UTILITY METHODS
    // ---------------------------------------------------------
    public List<String> listAll() {
        List<String> listOfIds = new ArrayList<>();

        for (AnimalComponent animal : this.getAll()) {
            listOfIds.add(animal.getId());
        }

        logger.trace("Listing all IDs. Count={}", listOfIds.size());
        return listOfIds;
    }

    public List<AnimalComponent> getAllExceptPacks() {
        List<AnimalComponent> result = new ArrayList<>();

        for (AnimalComponent animal : animals.values()) {
            if (!(animal instanceof AnimalPack)) {
                result.add(animal);
            }
        }

        logger.trace("Retrieved {} animals excluding packs.", result.size());
        return result;
    }

    public AnimalComponent getAnimalById(String id) {
        AnimalComponent animal = animals.get(id);

        if (animal == null) {
            logger.warn("getAnimalById: ID={} not found.", id);
        } else {
            logger.trace("getAnimalById: Found ID={} Type={}", id, animal.getAnimalType());
        }

        return animal;
    }
}