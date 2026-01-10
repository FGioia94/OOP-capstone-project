package template.Game;

import builder.MapBuilder.MapBuilder;
import chainOfResponsibility.commandHandler.*;
import factoryMethod.AnimalFactory.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import builder.MapBuilder.Position;

public class GameLoop {
    private boolean carryOn;
    private int resourcesRespawnPerCycle = 6;
    private int tick = 0;
    public final MapBuilder builder;
    public final AnimalRepository animalRepository;
    private boolean turnFinished;

    public GameLoop(MapBuilder builder, AnimalRepository animalRepository) {
        this.carryOn = true;
        this.builder = builder;
        this.animalRepository = animalRepository;
        this.turnFinished = false;

    }

    public final void run() {
        while (this.carryOn) {
            if (this.tick == 0) {
                moveAnimals();
                attack();
                checkLifePoints();
                consumeResources();
                processHunger();
                checkLifePoints();
                reproduce();
                assignExp();
                handleUserInputs();
            } else {
                String message = "";
                System.out.println("respawn and move cycle " + this.tick);
                message += autoRespawnResources();
                message += moveAnimals();
                message += attack();
                message += processHunger();
                message += checkLifePoints();
                message += consumeResources();
                message += reproduce();
                message += assignExp();
                handleUserInputs();
            }
            this.tick++;


//            showRecapMessage(message);
//
//            rebuildMap(builder, animalRepository);

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
        builder.setGrassPositions(builder.spawnElements(grass, builder.getGrassPositions()));
        builder.setWaterPositions(builder.spawnElements(water, builder.getWaterPositions()));
        return "Respawned " + grass + " grass and " + water + " water.\n";

    }


    private String moveAnimals() {
        Collection<AnimalComponent> animals = animalRepository.getAll();
        for (AnimalComponent animal : animals) {
            if (animal.getPack() != null) {
                continue;
            }
            builder.moveAnimal(animal);
        }
        return "Moved " + animals.size() + " animals.\n";
    }

    private String attack() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        Collection<AnimalComponent> carnivores = animalRepository.getAllByType("Carnivore");
        for (AnimalComponent carn : carnivores) {
            for (AnimalComponent target : animals) {
                boolean isNotSamePack = carn.getPack() != null && target.getPack() != null &&
                        !carn.getPack().equals(target.getPack());
                if (isNear(carn.getPosition(), target.getPosition()) && isNotSamePack) {
                    System.out.println("Carnivore " + carn.getId() +
                            " found target " + target.getId() +
                            " at position (" + carn.getPosition().x() +
                            ", " + carn.getPosition().y() + ")");
                    target.setHp(target.getHp() - (carn.getLevel() * 20));
                    carn.setExp(carn.getExp() + 40);
                    carn.setHp(carn.getHp() + 40);
                    System.out.println("carnivore " + carn.getId() +
                            " attacked " + target.getId());
                }

            }
        }
        return "Carnivores attacked nearby animals.\n";
    }

    private boolean isNear(Position a, Position b) {
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        return dx <= 1 && dy <= 1; // 8‑direction adjacency
    }

    private String processHunger() {

        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        for (AnimalComponent animal : animals) {
            if (animal.getAnimalType().equals("Herbivore")) {
                animal.setHp(animal.getHp() - 5);
            } else {
                animal.setHp(animal.getHp() - 20);
            }
        }
        return "Animals are getting hungry.\n";
    }


    private String checkLifePoints() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        List<String> toRemove = new ArrayList<>();

        for (AnimalComponent animal : animals) {
            if (animal.getHp() <= 0) {
                toRemove.add(animal.getId());
            }
        }

        for (String id : toRemove) {
            animalRepository.remove(id);
        }

        return "Some animals didn't pass the night.\n";
    }


    private String consumeResources() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        List<Position> grassPositions = builder.getGrassPositions();
        List<Position> waterPositions = builder.getWaterPositions();

        for (AnimalComponent animal : animals) {
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
        for (AnimalComponent animal : animalRepository.getAllExceptPacks()) {
            for (AnimalComponent partner : animalRepository.getAllByType(animal.getAnimalType())) {
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
        for (AnimalComponent animal : animalRepository.getAllExceptPacks()) {
            if (animal.getExp() >= 100) {
                animal.setLevel(animal.getLevel() + 1);
                animal.setExp(animal.getExp() - 100);
                animal.setHp(animal.getHp() + 20);
            }
        }
        return "Assigned experience points to animals.\n";
    }

//    private void askUser() {
//        System.out.println("User turn started.");
//        boolean turnActive = true;
//        while (turnActive) {
//            System.out.println("What do you want to do next? Type 'help' or 'h' for a list of commands.");
//            turnActive = handleUserInputs();
//        }
//        System.out.println("User turn finished.");
//    }

    private void handleUserInputs() {

        Scanner scanner = new Scanner(System.in);
        CommandHandler chain = new CommandChainBuilder()
                .add(new HelpCommandHandler())
                .add(new ExitCommandHandler())
                .add(new ContinueCommandHandler())
                .add(new ListAnimalsCommandHandler())
                .add(new ListMapCommandHandler())
                .add(new SaveCommandHandler())
                .add(new LoadCommandHandler())
                .add(new ClearAnimalsCommandHandler())
                .add(new ClearMapResourcesCommandHandler())
                .add(new DeleteAnimalCommandHandler())
                .add(new PackCommandHandler())
                .add(new ListPacksCommandHandler())
                .add(new SpawnCommandHandler())
                .add(new CreateCommandHandler())
                .add(new InvalidInputCommandHandler())
                .build();

        while (!this.turnFinished) {
            System.out.println("What do you want to do next? Type 'help' or 'h' for a list of commands.");
            String input = scanner.nextLine().trim();
            chain.handleAndMessage(input, scanner, this);
        }
        this.turnFinished = false;
    }

    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    public static void requestExit() {
        System.exit(0);

    }
}
