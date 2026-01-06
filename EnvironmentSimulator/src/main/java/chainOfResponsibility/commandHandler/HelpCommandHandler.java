package chainOfResponsibility.commandHandler;

import template.GameLoop.GameLoop;

import java.util.Scanner;

public class HelpCommandHandler implements CommandHandler {
    private CommandHandler next;
    private final Runnable onExit;

    public HelpCommandHandler(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void setNext(CommandHandler next) {
        this.next = next;
    }

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("h") || cmd.equalsIgnoreCase("help")) {
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
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
