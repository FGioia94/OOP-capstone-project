package chainOfResponsibility.commandHandler;

import template.GameLoop.GameLoop;

import java.util.Scanner;

public class ListAnimalsCommandHandler implements CommandHandler {
    private CommandHandler next;
    private final Runnable onExit;

    public ListAnimalsCommandHandler(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void setNext(CommandHandler next) {
        this.next = next;
    }

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("listAnimals")) {
            gameLoop.animalRepository.getAll().forEach(animal -> {
                System.out.println("ID: " + animal.getId() +
                        ", Type: " + animal.getAnimalType() +
                        ", Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                        ", HP: " + animal.getHp() +
                        ", EXP: " + animal.getExp() +
                        ", Hunger: " + animal.getLevel() +
                        "--------------------------");
            });
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
