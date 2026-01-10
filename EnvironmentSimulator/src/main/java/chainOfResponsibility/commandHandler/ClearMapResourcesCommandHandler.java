package chainOfResponsibility.commandHandler;

public class ClearMapResourcesCommandHandler extends CommandHandler {
    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("clearResources")) {
            gameLoop.builder.clear();
            System.out.println("All resoruces have been removed.");
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}
