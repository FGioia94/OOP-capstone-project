package chainOfResponsibility.commandHandler;

import memento.GameSnapshot.GameSnapshot;
import strategy.IO.SaveGame;
import template.GameLoop.GameLoop;

import java.util.Scanner;

public class SaveCommandHandler implements CommandHandler {
    private CommandHandler next;

    @Override
    public void setNext(CommandHandler next) {
        this.next = next;
    }

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("s") || (cmd.equalsIgnoreCase("save") {
            System.out.println("Enter the name of the file to save here");
            String fileName = scanner.nextLine();
            if (fileName.isEmpty() || fileName.isBlank()) {
                System.out.println("Invalid file name. Save command aborted.");
                return true;
            }
            String format = askFormat(scanner);

            try {
                GameSnapshot snapshot = new GameSnapshot(gameLoop.animalRepository, gameLoop.builder);
                SaveGame.save(fileName, snapshot, format);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

    public String askFormat(Scanner scanner) {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter the format to save (json/binary): ");
            String format = scanner.nextLine().trim().toLowerCase();
            if (format.equals("json") || format.equals("binary")) {
                return format;
            } else {
                System.out.println("Invalid format. Please enter 'json' or 'binary'.");
            }
        }
        return null;
    }
}
