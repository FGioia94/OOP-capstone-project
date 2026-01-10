package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public class ListAnimalsCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("listAnimals")) {
            gameLoop.animalRepository.getAll().forEach(animal -> {
                System.out.println("ID: " + animal.getId() +
                        ", Type: " + animal.getAnimalType() +
                        ", Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                        ", HP: " + animal.getHp() +
                        ", EXP: " + animal.getExp() +
                        ", Level: " + animal.getLevel() +
                        "--------------------------");
            });
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
