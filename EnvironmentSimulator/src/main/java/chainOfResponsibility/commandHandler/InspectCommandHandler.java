package chainOfResponsibility.commandHandler;

import annotations.AdminOnly;
import factoryMethod.AnimalFactory.AnimalComponent;
import factoryMethod.AnimalFactory.AnimalInspector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Command handler for inspecting animals using Java Reflection API.
 * This handler is only available in admin mode.
 * 
 * Demonstrates:
 * - Runtime introspection of animal objects
 * - Dynamic method invocation
 * - Field inspection
 */
@AdminOnly(reason = "Animal inspection requires admin privileges to access reflection capabilities")
public class InspectCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(InspectCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("inspect")) {

            logger.info("Inspect command received (admin-only feature)");

            System.out.println("\n=== ANIMAL INSPECTOR (REFLECTION API) ===");
            System.out.println("1. Inspect specific animal by ID");
            System.out.println("2. Discover available factories");
            System.out.println("3. Compare two animals");
            System.out.println("Enter choice (or 'cancel' to cancel):");

            String choice = scanner.nextLine().trim();

            if (checkCancel(choice)) {
                return true;
            }

            switch (choice) {
                case "1":
                    inspectAnimal(scanner, gameLoop);
                    break;
                case "2":
                    discoverFactories();
                    break;
                case "3":
                    compareAnimals(scanner, gameLoop);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }

    private void inspectAnimal(Scanner scanner, GameLoop gameLoop) {
        System.out.println("Enter animal ID to inspect:");
        String id = scanner.nextLine().trim();

        if (checkCancel(id)) {
            return;
        }

        AnimalComponent animal = gameLoop.animalRepository.get(id);

        if (animal == null) {
            System.out.println("Animal not found with ID: " + id);
            logger.warn("Attempted to inspect non-existent animal: {}", id);
            return;
        }

        logger.info("Using reflection to inspect animal: {}", id);

        // Use reflection to inspect the animal
        AnimalInspector.printInspectionReport(animal);

        // Demonstrate dynamic method invocation
        System.out.println("=== DYNAMIC METHOD INVOCATION ===");
        Object hp = AnimalInspector.invokeGetter(animal, "getHp");
        Object level = AnimalInspector.invokeGetter(animal, "getLevel");
        Object exp = AnimalInspector.invokeGetter(animal, "getExp");
        Object range = AnimalInspector.invokeGetter(animal, "getRange");

        System.out.println("HP (via reflection): " + hp);
        System.out.println("Level (via reflection): " + level);
        System.out.println("Experience (via reflection): " + exp);
        System.out.println("Range (via reflection): " + range);
        System.out.println("=================================\n");
    }

    private void discoverFactories() {
        logger.info("Using reflection to discover available factories");

        System.out.println("\n=== FACTORY DISCOVERY (REFLECTION) ===");
        List<String> factories = AnimalInspector.discoverFactories();

        System.out.println("Discovered " + factories.size() + " factory implementations:");
        for (String factory : factories) {
            System.out.println("  - " + factory);
        }

        System.out.println("\nDemonstrating dynamic factory loading...");
        for (String factoryName : factories) {
            var factory = AnimalInspector.loadFactoryByName(factoryName);
            if (factory != null) {
                System.out.println("✓ Successfully loaded: " + factoryName);
            }
        }
        System.out.println("=====================================\n");
    }

    private void compareAnimals(Scanner scanner, GameLoop gameLoop) {
        System.out.println("Enter first animal ID:");
        String id1 = scanner.nextLine().trim();

        if (checkCancel(id1)) {
            return;
        }

        System.out.println("Enter second animal ID:");
        String id2 = scanner.nextLine().trim();

        if (checkCancel(id2)) {
            return;
        }

        AnimalComponent animal1 = gameLoop.animalRepository.get(id1);
        AnimalComponent animal2 = gameLoop.animalRepository.get(id2);

        if (animal1 == null || animal2 == null) {
            System.out.println("One or both animals not found.");
            return;
        }

        logger.info("Using reflection to compare animals: {} vs {}", id1, id2);

        System.out.println("\n=== ANIMAL COMPARISON (REFLECTION) ===");
        Map<String, String> differences = AnimalInspector.compareAnimals(animal1, animal2);

        if (differences.isEmpty()) {
            System.out.println("Animals are identical in all comparable fields.");
        } else {
            System.out.println("Differences found:");
            for (Map.Entry<String, String> entry : differences.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("======================================\n");
    }
}
