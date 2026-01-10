import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.Game;
import template.Game.GameAdmin;
import template.Game.GameDefault;

public class Main {

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger(Main.class);
        logger.info("Game is starting...");
        Game game;
        boolean adminMode = Game.sendWelcomeMessages();
        if (adminMode) {
            game = new GameAdmin();
        } else {
            game = new GameDefault();
        }
        game.start();
    }
}