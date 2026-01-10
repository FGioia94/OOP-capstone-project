package chainOfResponsibility.commandHandler;

import memento.GameSnapshot.GameSnapshot;
import strategy.IO.LoadGame;
import template.Game.GameLoop;

import java.util.Scanner;

public class LoadCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("l") || (cmd.equalsIgnoreCase("load"))) {
            String fileName = this.askFileName(scanner);
            String format = this.askFormat(scanner);
            if (this.askAreYouSure(scanner)) {
                try {
                    GameSnapshot snapshot = LoadGame.load(fileName, format);
                    gameLoop.animalRepository.fromState(snapshot.getAnimalState());
                    gameLoop.builder.fromState(snapshot.getMapState());
                    return true;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }


}
