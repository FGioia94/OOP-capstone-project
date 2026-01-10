package chainOfResponsibility.commandHandler;

public class InvalidInputCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {
        System.out.println("Invalid command: " + cmd + ". Please try again.");
        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}

