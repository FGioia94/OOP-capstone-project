package chainOfResponsibility.commandHandler;

import factoryMethod.AnimalFactory.AnimalComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import template.Game.GameLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListPacksCommandHandler extends CommandHandler {

    private static final Logger logger = LogManager.getLogger(ListPacksCommandHandler.class);

    @Override
    public boolean handle(String cmd, Scanner scanner, GameLoop gameLoop) {

        if (cmd.equalsIgnoreCase("listPacks")) {

            logger.info("ListPacks command received.");

            var packs = gameLoop.animalRepository.getAllByType("Pack");

            logger.debug("Found {} packs.", packs.size());

            if (packs.isEmpty()) {
                logger.warn("No packs found to list.");
                System.out.println("No packs present on the map.");
                return true;
            }

            packs.forEach(pack -> {

                logger.trace("Listing pack ID={} with {} members.",
                        pack.getId(), pack.getMembers().size());

                System.out.println("ID: " + pack.getId());

                for (AnimalComponent animal : pack.getMembers()) {

                    logger.trace("Pack member -> ID={}, Type={}, Sex={}, Pos={}, HP={}, EXP={}, Level={}",
                            animal.getId(),
                            animal.getAnimalType(),
                            animal.getSex(),
                            animal.getPosition(),
                            animal.getHp(),
                            animal.getExp(),
                            animal.getLevel()
                    );

                    System.out.println(
                            "   ID: " + animal.getId() +
                                    ",     Type: " + animal.getAnimalType() +
                                    ",     Sex:" + animal.getSex() +
                                    ",     Position: (" + animal.getPosition().x() + ", " + animal.getPosition().y() + ")" +
                                    ",     HP: " + animal.getHp() +
                                    ",     EXP: " + animal.getExp() +
                                    ",     Level: " + animal.getLevel() +
                                    "/"
                    );
                }
            });

            return true;
        }

        logger.trace("Command '{}' not handled by {}. Passing to next handler.",
                cmd, this.getClass().getSimpleName());

        return next != null && next.handle(cmd, scanner, gameLoop);
    }
}