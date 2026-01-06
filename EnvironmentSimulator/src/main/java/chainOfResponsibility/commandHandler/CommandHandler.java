package chainOfResponsibility.commandHandler;

import template.GameLoop.GameLoop;

import java.util.Scanner;

public interface CommandHandler {
    void setNext(CommandHandler next);

    boolean handle(String cmd, Scanner scanner, GameLoop gameLoop);
}