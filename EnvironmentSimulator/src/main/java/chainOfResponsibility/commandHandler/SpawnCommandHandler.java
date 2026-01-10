package chainOfResponsibility.commandHandler;

import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import template.Game.GameLoop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SpawnCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("spawn")) {
            Map<String, String> specs = askSpecs(scanner);
            switch (specs.get("type")) {
                case "water": {
                    List<Position> newWater = gameLoop.builder.spawnElements(Integer.parseInt(specs.get("amount")), gameLoop.builder.getWaterPositions());
                    gameLoop.builder.setWaterPositions(newWater);
                    break;
                }
                case "grass": {
                    List<Position> newGrass = gameLoop.builder.spawnElements(Integer.parseInt(specs.get("amount")), gameLoop.builder.getGrassPositions());
                    gameLoop.builder.setGrassPositions(newGrass);
                    break;
                }
                case "herbivore": {
                    for (int i = 0; i < Integer.parseInt(specs.get("amount")); i++) {
                        AnimalFactory factory = new HerbivoreFactory();
                        factory.buildAnimal(gameLoop.builder, gameLoop.animalRepository, gameLoop.builder.getRandomValidPosition(), "m", 100, 0, 1);
                    }
                    break;
                }
                case "carnivore": {
                    for (int i = 0; i < Integer.parseInt(specs.get("amount")); i++) {
                        AnimalFactory factory = new CarnivoreFactory();
                        factory.buildAnimal(gameLoop.builder, gameLoop.animalRepository, gameLoop.builder.getRandomValidPosition(), "m", 100, 0, 1);

                    }
                    break;

                }
                default: {
                    System.out.println("Invalid type specified.");
                    return true;
                }


            }
            return true;


        }
        return next != null && next.handle(cmd, scanner, gameLoop);
    }

    private Map<String, String> askSpecs(Scanner scanner) {

        boolean validInput = false;
        String typeOfObject = "";
        while (!validInput) {
            System.out.println("What do you want to spawn? (carnivore, herbivore, water, grass): ");
            typeOfObject = scanner.nextLine().trim().toLowerCase();
            if (typeOfObject.equals("carnivore") || typeOfObject.equals("herbivore") || typeOfObject.equals("water") || typeOfObject.equals("grass")) {
                validInput = true;
            } else {
                System.out.println("Invalid type. Please enter 'carnivore', 'herbivore', 'grass' or 'water'.");
            }
        }
        validInput = false;
        String amount = "0";
        while (!validInput) {
            System.out.println("How many do you want to spawn? ");
            String amountInput = scanner.nextLine().trim();
            try {
                amount = amountInput;
                validInput = !amount.equals("0");
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number > 0.");
            }
        }

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("type", typeOfObject);
        infoMap.put("amount", amount);
        return infoMap;
    }
}
