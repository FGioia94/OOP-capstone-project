package template.GameLoop;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;

import builder.MapBuilder.Position;

public class GameLoop {
    private boolean carryOn;
    private int resourcesRespawnPerCycle = 6;
    private int tick = 0;
    public final MapBuilder builder;
    public final AnimalRepository animalRepository;

    public GameLoop(MapBuilder builder, AnimalRepository animalRepository) {
        this.carryOn = true;
        this.builder = builder;
        this.animalRepository = animalRepository;

    }

    public final void run() {
        while (this.carryOn) {
            this.tick++;
            String message = "";
            message += autoRespawnResources();
            message += moveAnimals();
            message += attack();
            message += processHunger();
            message += checkLifePoints();
            message += consumeResources();
            message += reproduce();
            message += assignExp();
            showRecapMessage(message);
            boolean changes = handleUserInputs();
            rebuildMap(builder, animalRepository);

        }

    }

    private String autoRespawnResources() {
        int grass = 0;
        int water = 0;
        for (int i = 0; i < resourcesRespawnPerCycle; i++) {
            if ((i % 2 == 0 && this.tick % 2 == 0) || (i % 2 != 0 && this.tick % 2 != 0)) {
                grass++;

            } else {
                water++;
            }
        }
        builder.spawnElements(grass, builder.getGrassPositions());
        builder.spawnElements(water, builder.getWaterPositions());
        return "Respawned " + grass + " grass and " + water + " water.\n";

    }


    private String moveAnimals() {
        Collection<Animal> animals = animalRepository.getAll();
        for (Animal animal : animals) {
            builder.moveAnimal(animal);
        }
        return "Moved " + animals.size() + " animals.\n";
    }

    private String attack() {
        Collection<Animal> animals = animalRepository.getAll();
        Collection<Animal> carnivores = animalRepository.getAllByType("Carnivore");
        for (Animal carn : carnivores) {
            for (Animal target : animals) {
                if (carn.getPosition() == target.getPosition()) {
                    target.hp -= carn.getLevel() * 10;
                    carn.setExp(carn.getExp() + 20);
                    carn.setHp(carn.getHp() + 10);
                }
            }
        }
        return "Carnivores attacked nearby animals.\n";
    }

    private String processHunger() {
        Collection<Animal> carnivores = animalRepository.getAllByType("Carnivore");
        if (this.tick % 5 == 0) {
            for (Animal carn : carnivores) {
                carn.setHp(carn.getHp() - 10);
            }
        }
        return "Carnivores are getting hungry.\n";
    }


    private String checkLifePoints() {
        Collection<Animal> animals = animalRepository.getAll();
        for (Animal animal : animals) {
            if (animal.getHp() <= 0) {
                animalRepository.remove(animal.getId());
            }
        }
        return "Some animals didn't pass the night.\n";
    }


    private String consumeResources() {
        Collection<Animal> animals = animalRepository.getAll();
        List<Position> grassPositions = builder.getGrassPositions();
        List<Position> waterPositions = builder.getWaterPositions();

        for (Animal animal : animals) {
            if (Objects.equals(animal.getAnimalType(), "Herbivore")) {
                for (Position grassPos : grassPositions) {
                    if (animal.getPosition() == grassPos) {
                        grassPositions.remove(grassPos);
                        builder.setGrassPositions(grassPositions);
                        animal.setHp(animal.getHp() + 10 * animal.getLevel());
                        animal.setExp(animal.getExp() + 20);

                    }
                }

            }

            for (Position waterPos : waterPositions) {
                if (animal.getPosition() == waterPos) {
                    waterPositions.remove(waterPos);
                    builder.setWaterPositions(waterPositions);
                    animal.setHp(animal.getHp() + 10);

                }
            }
        }
        return "Animals consumed available resources.\n";
    }

    private String reproduce() {
        AnimalFactory factory;
        boolean message = false;
        for (Animal animal : animalRepository.getAll()) {
            for (Animal partner : animalRepository.getAllByType(animal.getAnimalType())) {
                if (!Objects.equals(animal.getSex(), partner.getSex()) && animal.getPosition() == partner.getPosition()) {

                    factory = Objects.equals(animal.getAnimalType(), "Carnivore") ? new CarnivoreFactory() : new HerbivoreFactory();

                    Animal child = factory.buildAnimal(
                            builder,
                            animalRepository,
                            animal.getPosition(),
                            Math.random() < 0.5 ? "m" : "f", 0, 100, 1);

                    animalRepository.add(child);

                    animal.setExp(animal.getExp() + 50);
                    partner.setExp(animal.getExp() + 50);
                    message = true;
                }
            }
        }
        if (message) {
            return "Some animals reproduced.\n";
        } else {
            return "";
        }
    }

    private String assignExp() {
        for (Animal animal : animalRepository.getAll()) {
            if (animal.getExp() >= 100) {
                animal.setLevel(animal.getLevel() + 1);
                animal.setExp(animal.getExp() - 100);
                animal.setHp(animal.getHp() + 20);
            }
        }
        return "Assigned experience points to animals.\n";
    }

    private void handleUserInputs() {
        boolean keepAsking = true;
        while (keepAsking) {
            System.out.println("Enter command (type 'exit' to quit, 'h' for help): ");
            Scanner scanner = new java.util.Scanner(System.in);
            if (!scanner.hasNext()) {
                this.carryOn = false;
                return;
            }
            if (!scanner.hasNextLine()) return;
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return;
            switch (input.toLowerCase()) {
                case "exit" -> {

                    this.carryOn = false;
                    System.out.println("Are you sure you want to exit? (yes/no)");
                    String confirmation = scanner.nextLine().trim().toLowerCase();
                    if (confirmation.equals("yes") || confirmation.equals("y")) {
                        keepAsking = false;
                        carryOn = false;
                    } else {
                        break;
                    }


                }
                case "h", "help" -> {
                    System.out.println("Available commands:");
                    System.out.println("  exit - Quit the game");
                    System.out.println("  h or help - Show this help message");
                    System.out.println("  create - Create a new animal");
                    System.out.println("  listAnimals - List all animals");
                    System.out.println("  listMap - Show map details");
                    System.out.println("  s or save - Save the game state");
                    System.out.println("  l or load - Load a saved game state");
                    System.out.println("  loadPlugin - Loads the expansion plugin");
                    System.out.println("  unloadPlugin - Unloads the expansion plugin");
                    System.out.println("  set - Set animal attributes");
                    System.out.println("  deleteAnimal - Delete an animal by ID");
                    System.out.println("  clean - Clean the map of all animals");
                    System.out.println("  cleanResources - Clean the map of all resources");
                    System.out.println("  deleteResource - Delete a resource at a position");
                    break;
                }

                case "create" -> {
                    createAnimalPrompt(builder, animalRepository);
                    break;
                }
            }
        }
    }

    public static void requestExit() {
        System.exit(0);
    }
}
