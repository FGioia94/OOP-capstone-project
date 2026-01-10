package template.Game;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public abstract class Game {
    protected final Logger logger = LogManager.getLogger(getClass());
    protected MapBuilder builder;
    protected AnimalRepository repository;
    protected GameLoop gameLoop;

    public void start() {
        logger.info("Starting the Environment Simulation Game...");

        this.builder = createMapBuilder();
        this.repository = createAnimalRepository();
        initializeGame();
        initialSetup();

        logger.info("Running game loop...");
        runGameLoop(this.builder, this.repository);
        shutdownGame();

    }

    public static boolean sendWelcomeMessages() {
        Logger staticLogger = LogManager.getLogger(Game.class);
        staticLogger.info("Welcome to the Environment Simulation Game, created by Francesco Gioia!");
        staticLogger.info("Do you want to start in admin mode? (yes/no)");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();
        boolean adminMode = false;
        if (response.equals("yes") || response.equals("y")) {
            String inputPassword = scanner.nextLine();
            if (inputPassword.equals(System.getenv("ENVIRONMENT_SIM_ADMIN_PASSWORD"))) {
                staticLogger.info("Admin mode activated.");
                adminMode = true;
            } else {
                staticLogger.warn("Incorrect password. Starting in normal mode.");
            }

        } else if (response.equals("no") || response.equals("n")) {
            staticLogger.info("Starting in normal mode.");
        } else {
            staticLogger.warn("Invalid input. Starting in normal mode.");
        }
        return adminMode;
    }

    protected abstract void initializeGame();

    protected abstract void runGameLoop(MapBuilder builder, AnimalRepository repository);

    private void shutdownGame() {
        logger.info("Shutting down the game. Goodbye!");
    }

    protected abstract MapBuilder createMapBuilder();

    protected abstract AnimalRepository createAnimalRepository();

    protected abstract void initialSetup();

}
