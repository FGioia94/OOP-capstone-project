package chainOfResponsibility.commandHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClearMapResourcesCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ClearMapResourcesCommandHandler.class);

    @Override
    public boolean handle(String cmd, java.util.Scanner scanner, template.Game.GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("clearResources")) {
            if (askAreYouSure(scanner)) {
                int grass = gameLoop.builder.getGrassPositions().size();
                int water = gameLoop.builder.getWaterPositions().size();

                logger.info("Clearing map resources: grass={}, water={}", grass, water);

                gameLoop.builder.clear();

                System.out.println("All resources have been removed.");
            }
            return true;
        }

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}