package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public class ListMapCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("listMap")) {
            System.out.println("Map Details: + " +
                    "Water amount: " + gameLoop.builder.getWaterPositions().size() +
                    ", Grass amount: " + gameLoop.builder.getGrassPositions().size() +
                    ", Obstacle amount: " + gameLoop.builder.getObstaclesPositions().size() +
                    ", Animals amount: " + gameLoop.animalRepository.getAll().size());
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
