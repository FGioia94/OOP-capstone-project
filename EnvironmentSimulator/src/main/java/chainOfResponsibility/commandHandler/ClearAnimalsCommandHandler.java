package chainOfResponsibility.commandHandler;

public class ClearAnimalsCommandHandler extends CommandHandler {

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("clearAnimals")) {
            gameLoop.animalRepository.clear();
            System.out.println("All animals have been removed.");
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}
