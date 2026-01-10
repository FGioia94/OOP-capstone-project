package chainOfResponsibility.commandHandler;

import template.Game.GameLoop;

import java.util.Scanner;

public class ExitCommandHandler extends CommandHandler {


    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("exit")) {
            System.out.println("Are you sure you want to exit? (yes/no)");
            String confirmation = scanner.hasNextLine() ? scanner.nextLine().trim().toLowerCase() : "";
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                GameLoop.requestExit();
                return true;
            }

        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
