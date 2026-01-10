package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public class ListAnimalsCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("listAnimals")) {
            gameLoop.animalRepository.getAllExceptPacks().forEach(animal -> {
                System.out.println("ID: " + animal.getId() +
                        ", Type: " + animal.getAnimalType() +
                        ", Sex:" + animal.getSex() +
                        ", Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                        ", HP: " + animal.getHp() +
                        ", EXP: " + animal.getExp() +
                        ", Level: " + animal.getLevel() +
                        ", Pack: " + (animal.getPack() != null ? animal.getPack() : "None") +
                        "/");
            });
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
