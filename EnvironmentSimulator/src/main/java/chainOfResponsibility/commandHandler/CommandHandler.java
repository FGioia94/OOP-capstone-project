package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.Scanner;

public abstract class CommandHandler {

    private static final Logger logger = LogManager.getLogger(CommandHandler.class);

    protected CommandHandler next;

    public void setNext(CommandHandler next) {
        logger.debug("Setting next handler: {} -> {}",
                this.getClass().getSimpleName(),
                next != null ? next.getClass().getSimpleName() : "null");
        this.next = next;
    }

    public void handleAndMessage(String cmd, Scanner scanner, GameLoop gameLoop) {
        logger.debug("Handling command '{}' with {}", cmd, this.getClass().getSimpleName());
        handle(cmd, scanner, gameLoop);
    }

    public abstract boolean handle(String cmd, Scanner scanner, GameLoop gameLoop);

    String askFileName(Scanner scanner) {
        System.out.println("Enter the file name (or 'cancel' to cancel):");
        String input = scanner.nextLine().trim();

        if (checkCancel(input)) {
            return null;
        }

        return input;
    }

    String askFormat(Scanner scanner) {
        System.out.println("Enter the file format (json/binary) or 'cancel' to cancel:");
        String input = scanner.nextLine().trim().toLowerCase();

        if (checkCancel(input)) {
            return null;
        }

        return input;
    }


    boolean askAreYouSure(Scanner scanner) {
        System.out.println("Are you sure? Type 'y' or 'yes' to confirm, anything else to cancel");

        String confirmation = scanner.hasNextLine()
                ? scanner.nextLine().trim().toLowerCase()
                : "";

        boolean result = confirmation.equals("yes") || confirmation.equals("y");

        logger.debug("User confirmation: '{}' -> {}", confirmation, result ? "CONFIRMED" : "CANCELLED");

        return result;
    }

    protected boolean isCancel(String input) {
        return input.equalsIgnoreCase("cancel");
    }

    protected boolean checkCancel(String input) {
        if (isCancel(input)) {
            System.out.println("Operation cancelled.");
            logger.info("Operation cancelled by user.");
            return true;
        }
        return false;
    }
}