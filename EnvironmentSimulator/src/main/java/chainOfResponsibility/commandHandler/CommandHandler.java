package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public abstract class CommandHandler {
    protected CommandHandler next;

    public void setNext(CommandHandler next) {
        this.next = next;
    }

    public boolean handleAndMessage(String cmd, Scanner scanner, GameLoop gameLoop) {
        return handle(cmd, scanner, gameLoop);
    }

    public abstract boolean handle(String cmd, Scanner scanner, GameLoop gameLoop);

    String askFileName(Scanner scanner) {
        boolean validInput = false;
        String fileName = "";
        while (!validInput) {
            System.out.println("Enter the name of the file to load here: ");
            fileName = scanner.nextLine().trim();
            if (!fileName.isEmpty()) {
                validInput = true;
            } else {
                System.out.println("File name cannot be empty. Please try again.");
            }
        }
        return fileName;

    }

    String askFormat(Scanner scanner) {
        boolean validInput = false;
        String format = "";
        while (!validInput) {
            System.out.println("Enter the format to load (json/binary): ");
            format = scanner.nextLine().trim().toLowerCase();
            if (format.equals("json") || format.equals("binary")) {
                validInput = true;
            } else {
                System.out.println("Invalid format. Please enter 'json' or 'binary'.");
            }
        }
        return format;
    }

    boolean askAreYouSure(Scanner scanner) {
        System.out.println("Are you sure? Type 'y' or 'yes' to confirm, anything else to cancel");
        String confirmation = scanner.hasNextLine() ? scanner.nextLine().trim().toLowerCase() : "";
        return confirmation.equals("yes") || confirmation.equals("y");
    }
}