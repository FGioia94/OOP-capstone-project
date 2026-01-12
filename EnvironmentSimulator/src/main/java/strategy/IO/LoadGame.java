package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadGame {

    private static final Logger logger = LogManager.getLogger(LoadGame.class);

    public static GameSnapshot load(String fileName, String type) {

        logger.info("Requested load of '{}' as type '{}'", fileName, type);

        // Normalize type
        type = type.toLowerCase();

        // Project root (EnvironmentSimulator/)
        String projectRoot = System.getProperty("user.dir");

        // Full path to src/data/saved
        Path saveDir = Paths.get(projectRoot, "src", "data", "saved");

        String extension;
        LoadStrategy strategy;

        switch (type) {
            case "json":
                strategy = new JsonLoadStrategy();
                extension = ".json";
                break;
            case "binary":
                strategy = new BinaryLoadStrategy();
                extension = ".bin";
                break;
            default:
                logger.error("Unsupported file type '{}'", type);
                throw new IllegalArgumentException("Unsupported file type: " + type);
        }

        if (!fileName.toLowerCase().endsWith(extension)) {
            fileName = fileName + extension;
        }

        Path fullPath = saveDir.resolve(fileName);

        logger.debug("Full resolved load path: '{}'", fullPath);

        if (!Files.exists(fullPath)) {
            logger.error("Save file '{}' does not exist", fullPath);
            throw new LoadException("Save file does not exist: " + fullPath);
        }

        try {
            GameSnapshot snapshot = strategy.load(fullPath.toString());
            logger.info("Game successfully loaded from '{}'", fullPath);
            return snapshot;

        } catch (Exception e) {
            logger.error("Failed to load game from '{}': {}", fullPath, e.getMessage(), e);
            throw new LoadException("Unable to load game from: " + fullPath, e);
        }
    }
}