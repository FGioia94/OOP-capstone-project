package template.Game;

import builder.MapBuilder.MapBuilder;
import factoryMethod.AnimalFactory.AnimalRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Game {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected MapBuilder builder;
    protected AnimalRepository repository;
    protected GameLoop gameLoop;

    public void start() {
        initializeGame();
        logger.info("Starting the Environment Simulation Game...");

        logger.debug("Creating MapBuilder...");
        this.builder = createMapBuilder();

        logger.debug("Creating AnimalRepository...");
        this.repository = createAnimalRepository();


        logger.debug("Running initial setup...");
        initialSetup();

        logger.info("Running game loop...");
        runGameLoop(this.builder, this.repository);

        shutdownGame();
    }

    public static boolean sendWelcomeMessages() {


        Logger staticLogger = LogManager.getLogger(Game.class);
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Welcome to the Environment Simulation Game, created by Francesco Gioia!");
            System.out.println("Do you want to start in admin mode? (yes/no)");

            if (!scanner.hasNextLine()) {
                System.out.println("Input stream closed. Starting in normal mode.");
                return false;
            }

            String response = scanner.nextLine().trim().toLowerCase();

            boolean adminMode = false;

            switch (response) {
                case "yes":
                case "y":
                    staticLogger.info("Enter admin password:");

                    if (!scanner.hasNextLine()) {
                        System.out.println("Input stream closed. Starting in normal mode.");
                        return false;
                    }

                    String inputPassword = scanner.nextLine();
                    String expectedPassword = System.getenv("ENVIRONMENT_SIM_ADMIN_PASSWORD");

                    if (expectedPassword == null) {
                        System.out.println("Admin password environment variable is not set.");
                        return false;
                    }

                    if (inputPassword.equals(expectedPassword)) {
                        System.out.println("Admin mode activated.");
                        adminMode = true;
                    } else {
                        System.out.println("Incorrect password. Starting in normal mode.");
                    }
                    break;

                case "no":
                case "n":
                    System.out.println("Starting in normal mode.");
                    break;

                default:
                    System.out.println("Invalid input. Starting in normal mode.");
                    break;
            }

            return adminMode;

        } catch (NoSuchElementException e) {
            System.out.println("Input stream closed unexpectedly. Starting in normal mode.");
            return false;
        }

    }

    protected abstract void initializeGame();

    protected abstract void runGameLoop(MapBuilder builder, AnimalRepository repository);

    private void shutdownGame() {
        System.out.println("Shutting down the game. Goodbye!");
    }

    protected abstract MapBuilder createMapBuilder();

    protected abstract AnimalRepository createAnimalRepository();

    protected abstract void initialSetup();
}