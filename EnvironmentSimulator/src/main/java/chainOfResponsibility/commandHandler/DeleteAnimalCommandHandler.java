package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalNotFoundException;

public class DeleteAnimalCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("deleteAnimal")) {
            System.out.println("Enter the ID of the animal to delete:");
            String idInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            try {
                gameLoop.animalRepository.remove(idInput);
                System.out.println("Animal with ID " + idInput + " has been deleted.");
                return true;
            } catch (Exception e) {
                throw new AnimalNotFoundException(idInput, gameLoop.animalRepository);
            }
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }
}
