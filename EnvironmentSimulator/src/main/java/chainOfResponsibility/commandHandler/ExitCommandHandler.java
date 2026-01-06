package chainOfResponsibility.commandHandler;

import template.GameLoop.GameLoop;

import java.util.Scanner;

public class ExitCommandHandler implements CommandHandler {
    private CommandHandler next;
    private final Runnable onExit;

    public ExitCommandHandler(Runnable onExit) {
        this.onExit = onExit;
    }

    @Override
    public void setNext(CommandHandler next) {
        this.next = next;
    }

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {
        if (cmd.equalsIgnoreCase("exit")) {
            System.out.println("Are you sure you want to exit? (yes/no)");
            String confirmation = scanner.hasNextLine() ? scanner.nextLine().trim().toLowerCase() : "";
            if (confirmation.equals("yes") || confirmation.equals("y")) onExit.run();
            return true;
        }
        return next != null && next.handle(cmd, scanner, gameLoop);

    }

}
