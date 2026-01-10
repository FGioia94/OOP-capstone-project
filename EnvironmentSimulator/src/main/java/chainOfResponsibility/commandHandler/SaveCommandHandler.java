package chainOfResponsibility.commandHandler;

import memento.GameSnapshot.GameSnapshot;
import strategy.IO.SaveGame;
import template.Game.GameLoop;

import java.util.Scanner;

public class SaveCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("s") || (cmd.equalsIgnoreCase("save"))) {
            String fileName = this.askFileName(scanner);
            String format = this.askFormat(scanner);

            try {
                GameSnapshot snapshot = new GameSnapshot(gameLoop.animalRepository, gameLoop.builder);
                SaveGame.save(fileName, snapshot, format);
                return true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
