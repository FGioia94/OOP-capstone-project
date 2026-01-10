package chainOfResponsibility.commandHandler;

import builder.MapBuilder.Position;
import factoryMethod.AnimalFactory.*;
import template.Game.GameLoop;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CreateCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("create")) {
            Map<String, String> animalSpecs = askAnimalSpecs(scanner);
            AnimalFactory factory = animalSpecs.get("typeOfAnimal").equals("carnivore") ?
                    new CarnivoreFactory() : new HerbivoreFactory();

            Animal animal = factory.buildAnimal(gameLoop.builder, gameLoop.animalRepository, gameLoop.builder.getRandomValidPosition(), animalSpecs.get("sex"), 100, 0, 1);
            gameLoop.animalRepository.add(animal);
            System.out.println("New " + animalSpecs.get("typeOfAnimal")
                    + " created with ID: "
                    + animal.getId()
                    + " at position ("
                    + animal.getPosition().x()
                    + ", " + animal.getPosition().y() + ")");
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

    private Map<String, String> askAnimalSpecs(Scanner scanner) {

        boolean validInput = false;
        String typeOfAnimal = "";
        while (!validInput) {
            System.out.println("Enter the type of animal to create (carnivore/herbivore): ");
            typeOfAnimal = scanner.nextLine().trim().toLowerCase();
            if (typeOfAnimal.equals("carnivore") || typeOfAnimal.equals("herbivore")) {
                validInput = true;
            } else {
                System.out.println("Invalid type. Please enter 'carnivore' or 'herbivore'.");
            }
        }
        validInput = false;
        String sex = "";
        while (!validInput) {
            System.out.println("Enter the sex of the animal to create (m/f): ");
            sex = scanner.nextLine().trim().toLowerCase();
            if (sex.equals("m") || sex.equals("f")) {
                validInput = true;
            } else {
                System.out.println("Invalid sex. Please enter 'm' or 'f'.");
            }
        }
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("typeOfAnimal", typeOfAnimal);
        infoMap.put("sex", sex);
        return infoMap;
    }
}
