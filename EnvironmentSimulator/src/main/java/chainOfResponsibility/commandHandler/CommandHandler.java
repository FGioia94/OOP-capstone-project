package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.Scanner;

/**
 * Abstract base class for implementing the Chain of Responsibility pattern for command handling.
 * <p>
 * Each concrete handler processes a specific command and passes unhandled commands to the next
 * handler in the chain. This class provides common utility methods for input validation,
 * user confirmations, and chaining behavior.
 * </p>
 *
 * @see CommandChainBuilder
 */
public abstract class CommandHandler {

    private static final Logger logger = LogManager.getLogger(CommandHandler.class);

    protected CommandHandler next;

    /**
     * Sets the next handler in the chain of responsibility.
     *
     * @param next the next {@link CommandHandler} to delegate to if this handler cannot process the command
     */
    public void setNext(CommandHandler next) {
        logger.debug("Setting next handler: {} -> {}",
                this.getClass().getSimpleName(),
                next != null ? next.getClass().getSimpleName() : "null");
        this.next = next;
    }

    /**
     * Handles a command with logging and delegates to the {@link #handle(String, Scanner, GameLoop)} method.
     *
     * @param cmd the command string entered by the user
     * @param scanner the {@link Scanner} for reading additional user input
     * @param gameLoop the {@link GameLoop} instance for accessing game state and operations
     */
    public void handleAndMessage(String cmd, Scanner scanner, GameLoop gameLoop) {
        logger.debug("Handling command '{}' with {}", cmd, this.getClass().getSimpleName());
        handle(cmd, scanner, gameLoop);
    }

    /**
     * Processes the given command. If this handler can process the command, it does so and returns {@code true}.
     * Otherwise, it delegates to the next handler in the chain.
     *
     * @param cmd the command string entered by the user
     * @param scanner the {@link Scanner} for reading additional user input
     * @param gameLoop the {@link GameLoop} instance for accessing game state and operations
     * @return {@code true} if the command was handled, {@code false} otherwise
     */
    public abstract boolean handle(String cmd, Scanner scanner, GameLoop gameLoop);

    /**
     * Prompts the user to enter a file name.
     *
     * @param scanner the {@link Scanner} for reading user input
     * @return the file name entered by the user, or {@code null} if the operation was cancelled
     */
    String askFileName(Scanner scanner) {
        System.out.println("Enter the file name (or 'cancel' to cancel):");
        String input = scanner.nextLine().trim();

        if (checkCancel(input)) {
            return null;
        }

        return input;
    }

    /**
     * Prompts the user to enter a file format (json or binary).
     *
     * @param scanner the {@link Scanner} for reading user input
     * @return the file format entered by the user (lowercase), or {@code null} if the operation was cancelled
     */
    String askFormat(Scanner scanner) {
        System.out.println("Enter the file format (json/binary) or 'cancel' to cancel:");
        String input = scanner.nextLine().trim().toLowerCase();

        if (checkCancel(input)) {
            return null;
        }

        return input;
    }


    /**
     * Prompts the user for confirmation before proceeding with a potentially destructive operation.
     *
     * @param scanner the {@link Scanner} for reading user input
     * @return {@code true} if the user confirmed (yes/y), {@code false} otherwise
     */
    boolean askAreYouSure(Scanner scanner) {
        System.out.println("Are you sure? Type 'y' or 'yes' to confirm, anything else to cancel");

        String confirmation = scanner.hasNextLine()
                ? scanner.nextLine().trim().toLowerCase()
                : "";

        boolean result = confirmation.equals("yes") || confirmation.equals("y");

        logger.debug("User confirmation: '{}' -> {}", confirmation, result ? "CONFIRMED" : "CANCELLED");

        return result;
    }

    /**
     * Checks if the given input is the "cancel" keyword (case-insensitive).
     *
     * @param input the user input to check
     * @return {@code true} if the input is "cancel", {@code false} otherwise
     */
    protected boolean isCancel(String input) {
        return input.equalsIgnoreCase("cancel");
    }

    /**
     * Checks if the given input is "cancel" and, if so, prints a cancellation message.
     *
     * @param input the user input to check
     * @return {@code true} if the operation was cancelled, {@code false} otherwise
     */
    protected boolean checkCancel(String input) {
        if (isCancel(input)) {
            System.out.println("Operation cancelled.");
            logger.info("Operation cancelled by user.");
            return true;
        }
        return false;
    }
}