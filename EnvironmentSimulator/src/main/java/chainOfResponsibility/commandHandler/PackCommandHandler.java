package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalComponent;
import factoryMethod.AnimalFactory.AnimalPack;
import template.Game.GameLoop;

import java.util.*;

public class PackCommandHandler extends CommandHandler {

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("pack")) {


            List<AnimalComponent> animalList = new ArrayList<>();
            boolean validInput = false;

            while (!validInput) {
                System.out.println("Please enter the IDs of the animals to pack separated by commas:");
                String ids = scanner.nextLine().trim();

                // Accept ANY non-empty tokens separated by commas (UUID-safe)
                if (ids.matches("^[^,]+(,[^,]+)*$")) {

                    String[] idList = ids.split(",");
                    for (String rawId : idList) {
                        String id = rawId.trim();

                        AnimalComponent animal = gameLoop.animalRepository.getAnimalById(id);

                        if (animal == null) {
                            System.out.println("No animal found with ID: " + id + " — Skipping.");
                        } else {
                            animalList.add(animal);
                        }
                    }

                    if (animalList.isEmpty()) {
                        System.out.println("No valid animals found. Try again.");
                    } else {
                        validInput = true;
                    }

                } else {
                    System.out.println("Invalid input. Please enter comma-separated IDs.");
                }
            }


            validInput = false;
            AnimalPack newPack = null;
            while (!validInput) {
                System.out.println("Type the ID of the destination pack (or 0 to create a new pack):");
                String packId = scanner.nextLine().trim();

                if (packId.equals("0")) {

                    // Create new pack
                    String newPackId = UUID.randomUUID().toString();
                    newPack = new AnimalPack(newPackId);

                    for (AnimalComponent animal : animalList) {
                        newPack.add(animal);
                        animal.setPack(newPack.getId());
                        System.out.println("Animal ID " + animal.getId() + " added to NEW Pack " + newPackId);
                    }


                    validInput = true;

                } else {

                    AnimalComponent packComponent = gameLoop.animalRepository.getAnimalById(packId);

                    if (packComponent instanceof AnimalPack pack) {

                        for (AnimalComponent animal : animalList) {
                            pack.add(animal);
                            animal.setPack(pack.getId());
                            System.out.println("Animal ID " + animal.getId() + " added to Pack ID " + packId);
                        }

                        validInput = true;

                    } else {
                        System.out.println("Pack not found or ID does not refer to a pack.");
                    }
                }
            }
            if (newPack != null) {
                gameLoop.animalRepository.add(newPack);
            }

            return true;
        }

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}