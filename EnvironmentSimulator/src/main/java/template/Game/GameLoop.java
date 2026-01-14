package template.Game;

import annotations.AdminAnnotationChecker;
import builder.MapBuilder.MapBuilder;
import builder.MapBuilder.Position;
import chainOfResponsibility.commandHandler.*;
import factoryMethod.AnimalFactory.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameLoop {

    private static final Logger logger = LogManager.getLogger(GameLoop.class);

    private boolean carryOn;
    private boolean turnFinished;
    private int tick;
    private int resourcesRespawnPerCycle = 6;
    private final boolean adminMode;

    private final List<GameObserver> observers = new ArrayList<>();
    private RecapObserver recapObserver;

    public final MapBuilder builder;
    public final AnimalRepository animalRepository;

    public GameLoop(MapBuilder builder, AnimalRepository animalRepository) {
        this(builder, animalRepository, false);
    }

    public GameLoop(MapBuilder builder, AnimalRepository animalRepository, boolean adminMode) {
        this.carryOn = true;
        this.turnFinished = false;
        this.tick = 0;
        this.builder = builder;
        this.animalRepository = animalRepository;
        this.adminMode = adminMode;
        logger.debug("GameLoop initialized with adminMode={}", adminMode);

        // Register a RecapObserver by default so that events are collected and
        // printed in the tick recap even if no external observer was added.
        RecapObserver defaultRecap = new RecapObserver();
        addObserver(defaultRecap);
        logger.debug("RecapObserver registered by default.");
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
        if (observer instanceof RecapObserver ro) {
            this.recapObserver = ro;
        }
    }

    private void notifyObservers(GameEvent event) {
        for (GameObserver obs : observers) {
            obs.onEvent(event);
        }
    }

    public final void run() {
        logger.info("Game loop started.");

        while (carryOn) {

            if (tick == 0) {
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
                autoRespawnResources();
                moveAnimals();
                attack();
                processHunger();
                checkLifePoints();
                consumeResources();
                reproduce();
                assignExp();
                handleUserInputs();
            }

            printTickRecap();
            tick++;
        }

        logger.info("Game loop terminated.");
    }

    private void printTickRecap() {
        String summary = recapObserver != null ? recapObserver.flush() : "";

        System.out.println("=== Tick " + tick + " Summary ===");
        if (summary.isBlank()) {
            System.out.println("Nothing significant happened this turn.");
        } else {
            System.out.print(summary);
        }
        System.out.println("===============================");
    }

    private void autoRespawnResources() {
        int grass = 0;
        int water = 0;

        for (int i = 0; i < resourcesRespawnPerCycle; i++) {
            if ((i % 2 == 0 && tick % 2 == 0) || (i % 2 != 0 && tick % 2 != 0)) {
                grass++;
            } else {
                water++;
            }
        }

        builder.setGrassPositions(builder.spawnElements(grass, builder.getGrassPositions()));
        builder.setWaterPositions(builder.spawnElements(water, builder.getWaterPositions()));

        notifyObservers(new GameEvent(
                GameEventType.RESOURCE_RESPAWN,
                String.format("Respawned %d grass and %d water.", grass, water)));
    }

    private void moveAnimals() {
        Collection<AnimalComponent> animals = animalRepository.getAll();

        for (AnimalComponent animal : animals) {
            if (animal.getPack() != null)
                continue;
            builder.moveAnimal(animal);

            notifyObservers(new GameEvent(
                    GameEventType.MOVE,
                    String.format("Animal %s moved to (%d,%d)",
                            animal.getId(),
                            animal.getPosition().x(),
                            animal.getPosition().y())));
        }
    }

    private void attack() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        Collection<AnimalComponent> carnivores = animalRepository.getAllByType("Carnivore");

        for (AnimalComponent carn : carnivores) {
            for (AnimalComponent target : animals) {

                boolean differentPack = carn.getPack() == null ||
                        target.getPack() == null ||
                        !carn.getPack().equals(target.getPack());

                if (isNear(carn.getPosition(), target.getPosition(), 1)
                        && differentPack
                        && !carn.getId().equals(target.getId())) {

                    notifyObservers(new GameEvent(
                            GameEventType.ATTACK,
                            String.format("Carnivore %s attacked %s at (%d,%d)",
                                    carn.getId(),
                                    target.getId(),
                                    carn.getPosition().x(),
                                    carn.getPosition().y())));

                    target.setHp(target.getHp() - (carn.getLevel() * 20));
                    carn.setExp(carn.getExp() + 40);
                    carn.setHp(carn.getHp() + 40);
                }
            }
        }
    }

    private boolean isNear(Position a, Position b, int proximity) {
        proximity = Math.max(1, proximity);
        int dx = Math.abs(a.x() - b.x());
        int dy = Math.abs(a.y() - b.y());
        return dx <= proximity && dy <= proximity;
    }

    private void processHunger() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();

        // Thread-safe queue for events generated in parallel
        Queue<GameEvent> eventQueue = new ConcurrentLinkedQueue<>();

        // Parallelize only the HP update + event creation
        animals.parallelStream().forEach(animal -> {
            int loss = animal.getAnimalType().equals("Herbivore") ? 5 : 20;
            animal.setHp(animal.getHp() - loss);

            eventQueue.add(new GameEvent(
                    GameEventType.HUNGER,
                    String.format("Animal %s lost %d HP due to hunger.", animal.getId(), loss)));
        });

        // Notify observers on a single thread to avoid race conditions
        GameEvent event;
        while ((event = eventQueue.poll()) != null) {
            notifyObservers(event);
        }
    }

    private void checkLifePoints() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();

        // Thread-safe list for IDs to remove
        List<String> toRemove = Collections.synchronizedList(new ArrayList<>());

        // Parallelize only the HP check
        animals.parallelStream().forEach(animal -> {
            if (animal.getHp() <= 0) {
                toRemove.add(animal.getId());
            }
        });

        // Process removals and notifications sequentially
        for (String id : toRemove) {
            animalRepository.remove(id);

            notifyObservers(new GameEvent(
                    GameEventType.DEATH,
                    String.format("Animal %s died.", id)));
        }
    }

    private boolean consumeNearbyResource(List<Position> resourceList, Position animalPos) {
        Iterator<Position> it = resourceList.iterator();

        while (it.hasNext()) {
            Position pos = it.next();

            if (isNear(animalPos, pos, 3)) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    private void consumeResources() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        List<Position> grassPositions = builder.getGrassPositions();
        List<Position> waterPositions = builder.getWaterPositions();

        for (AnimalComponent animal : animals) {

            boolean ateGrass = false;
            boolean drankWater = false;

            if (animal.getAnimalType().equals("Herbivore")) {
                ateGrass = consumeNearbyResource(grassPositions, animal.getPosition());
            }

            drankWater = consumeNearbyResource(waterPositions, animal.getPosition());

            if (ateGrass) {
                animal.setHp(animal.getHp() + 10 * animal.getLevel());
                animal.setExp(animal.getExp() + 20);

                notifyObservers(new GameEvent(
                        GameEventType.RESOURCE_CONSUMPTION,
                        String.format("Herbivore %s ate grass.", animal.getId())));
            }

            if (drankWater) {
                animal.setHp(animal.getHp() + 10);

                notifyObservers(new GameEvent(
                        GameEventType.RESOURCE_CONSUMPTION,
                        String.format("Animal %s drank water.", animal.getId())));
            }
        }

        builder.setGrassPositions(grassPositions);
        builder.setWaterPositions(waterPositions);
    }

    private void reproduce() {
        Collection<AnimalComponent> animals = animalRepository.getAllExceptPacks();
        Set<String> processedPairs = new HashSet<>();

        for (AnimalComponent a : animals) {
            for (AnimalComponent b : animals) {

                if (a.getId().equals(b.getId()))
                    continue;
                if (!a.getAnimalType().equals(b.getAnimalType()))
                    continue;
                if (a.getSex().equals(b.getSex()))
                    continue;
                if (!isNear(a.getPosition(), b.getPosition(), 3))
                    continue;

                String pairKey = a.getId() + "-" + b.getId();
                String reverseKey = b.getId() + "-" + a.getId();

                if (processedPairs.contains(pairKey) || processedPairs.contains(reverseKey)) {
                    continue;
                }

                processedPairs.add(pairKey);

                AnimalFactory factory = a.getAnimalType().equals("Carnivore")
                        ? new CarnivoreFactory()
                        : new HerbivoreFactory();

                int children = (int) (Math.random() * 5) + 1;

                notifyObservers(new GameEvent(
                        GameEventType.REPRODUCTION,
                        String.format("%s and %s reproduced and created %d children.",
                                a.getId(), b.getId(), children)));

                for (int i = 0; i < children; i++) {
                    Animal child = factory.buildAnimal(
                            builder,
                            animalRepository,
                            a.getPosition(),
                            Math.random() < 0.5 ? "m" : "f",
                            0,
                            100,
                            1);
                    animalRepository.add(child);
                }

                a.setExp(a.getExp() + 50);
                b.setExp(b.getExp() + 50);
            }
        }
    }

    private void assignExp() {

        Queue<GameEvent> eventQueue = new ConcurrentLinkedQueue<>();
        animalRepository.getAllExceptPacks().parallelStream().forEach(animal -> {

            while (animal.getExp() >= 100) {
                animal.setLevel(animal.getLevel() + 1);
                animal.setExp(animal.getExp() - 100);
                animal.setHp(animal.getHp() + 20);

                eventQueue.add(new GameEvent(
                        GameEventType.LEVEL_UP,
                        String.format("Animal %s leveled up to %d.",
                                animal.getId(), animal.getLevel())));
            }
        });

        GameEvent event;
        while ((event = eventQueue.poll()) != null) {
            notifyObservers(event);
        }

    }

    private void handleUserInputs() {

        Scanner scanner = new Scanner(System.in);

        CommandChainBuilder chainBuilder = new CommandChainBuilder()
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
                .add(new CreateCommandHandler());

        // Add admin-only handlers based on @AdminOnly annotation
        InspectCommandHandler inspectHandler = new InspectCommandHandler();
        if (adminMode && AdminAnnotationChecker.requiresAdminMode(inspectHandler)) {
            logger.info("Admin mode: Adding InspectCommandHandler (verified via @AdminOnly annotation)");
            chainBuilder.add(inspectHandler);
        } else if (!adminMode && AdminAnnotationChecker.requiresAdminMode(inspectHandler)) {
            logger.debug("Skipping InspectCommandHandler - requires admin mode (@AdminOnly)");
        }

        CommandHandler chain = chainBuilder
                .add(new InvalidInputCommandHandler())
                .build();
        try {
            while (!turnFinished) {
                System.out.println("Awaiting command (type 'help' or 'h' for list)");
                String input = scanner.nextLine().trim();
                try {
                    chain.handleAndMessage(input, scanner, this);
                } catch (Exception e) {
                    // Exception shielding: catch any unhandled exceptions to prevent stack trace
                    // exposure
                    logger.error("Unexpected error handling command '{}': {}", input, e.getMessage(), e);
                    System.out.println("An error occurred: " + e.getMessage());
                    System.out.println("The game will continue. Type 'help' for available commands.");
                }
            }
        } catch (NoSuchElementException e) {
            // IntelliJ or the terminal closed the input stream
            System.out.println("Input stream closed. Exiting game...");
            requestExit();
        }

        turnFinished = false;
    }

    public void setTurnFinished(boolean finished) {
        this.turnFinished = finished;
    }

    public void requestExit() {
        this.carryOn = false;
        this.turnFinished = true;

    }
}