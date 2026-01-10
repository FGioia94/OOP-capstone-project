package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalComponent;
import template.Game.GameLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListPacksCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("listPacks")) {
            gameLoop.animalRepository.getAllByType("Pack").forEach(pack -> {
                List<AnimalComponent> animalsInThePack = new ArrayList<>();
                System.out.println("ID: " + pack.getId());
                for (AnimalComponent animal : pack.getMembers()) {
                    System.out.println(
                            "   ID: " + animal.getId() +
                                    ",     Type: " + animal.getAnimalType() +
                                    ",     Sex:" + animal.getSex() +
                                    ",     Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                                    ",     HP: " + animal.getHp() +
                                    ",     EXP: " + animal.getExp() +
                                    ",     Level: " + animal.getLevel() +
                                    "/");
                }


            });
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
