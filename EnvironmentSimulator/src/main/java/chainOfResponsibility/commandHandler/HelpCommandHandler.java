package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public class HelpCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("h") || cmd.equalsIgnoreCase("help")) {
            System.out.println("Available commands:");
            System.out.println("  c or continue - Finish the current turn");
            System.out.println("  clearAnimals - Clean the map of all animals");
            System.out.println("  clearResources - Clean the map of all resources");
            System.out.println("  create - Create a new animal");
            System.out.println("  deleteAnimal - Delete an animal by ID");
            System.out.println("  exit - Quit the game");
            System.out.println("  h or help - Show this help message");
            System.out.println("  l or load - Load a saved game state");
            System.out.println("  listAnimals - List all animals");
            System.out.println("  listMap - Show map details");
            System.out.println("  listPacks - List all animal packs");
            System.out.println("  s or save - Save the game state");
            System.out.println("  set - Set animal attributes");
            System.out.println("  spawn - Spawn resources on the map");
            System.out.println("  pack - Packs animals into groups");
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
