package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveGame {

    private static final Logger logger = LogManager.getLogger(SaveGame.class);

    public static void save(String fileName, GameSnapshot snapshot, String type) {

        logger.info("Requested save of '{}' as type '{}'", fileName, type);

        // Normalize type
        type = type.toLowerCase();

        // Project root (EnvironmentSimulator/)
        String projectRoot = System.getProperty("user.dir");

        // Full path to src/data/saved
        Path saveDir = Paths.get(projectRoot, "src", "data", "saved");

        try {
            if (!Files.exists(saveDir)) {
                logger.warn("Save directory '{}' does not exist. Creating it.", saveDir);
                Files.createDirectories(saveDir);
            }
        } catch (Exception e) {
            logger.error("Failed to create save directory '{}': {}", saveDir, e.getMessage(), e);
            throw new SaveException("Unable to create save directory: " + saveDir, e);
        }

        // Select strategy
        SaveStrategy strategy;
        String extension;

        switch (type) {
            case "json":
                strategy = new JsonSaveStrategy();
                extension = ".json";
                break;

            case "binary":
                strategy = new BinarySaveStrategy();
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

        logger.debug("Full resolved save path: '{}'", fullPath);

        try {
            strategy.save(snapshot, fullPath.toString());
            logger.info("Game successfully saved to '{}'", fullPath);

        } catch (Exception e) {
            logger.error("Failed to save game to '{}': {}", fullPath, e.getMessage(), e);
            throw new SaveException("Unable to save game to: " + fullPath, e);
        }
    }
}