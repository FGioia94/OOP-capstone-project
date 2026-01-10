package chainOfResponsibility.commandHandler;

public class ContinueCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("continue") || cmd.equalsIgnoreCase("c"))  {
            System.out.println("Turn finished. Continuing the game...");
            gameLoop.setTurnFinished(true);
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}

