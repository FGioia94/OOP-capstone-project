package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete strategy for loading game snapshots from JSON files.
 * <p>
 * Combines JSON reading and deserialization to restore snapshots
 * from human-readable JSON format.
 * </p>
 */
public class JsonLoadStrategy implements LoadStrategy {

    private static final Logger logger = LogManager.getLogger(JsonLoadStrategy.class);

    @Override
    public GameSnapshot load(String filePath) {

        logger.info("Loading GameSnapshot from JSON file '{}'", filePath);

        JsonReadStrategy readStrategy = new JsonReadStrategy();
        JsonDeserializationStrategy deserializationStrategy = new JsonDeserializationStrategy();

        try {
            String jsonData = readStrategy.read(filePath);

            logger.debug("Read {} characters from '{}'", jsonData.length(), filePath);

            GameSnapshot snapshot = deserializationStrategy.deserialize(jsonData);

            logger.info("JSON load completed successfully for '{}'", filePath);

            return snapshot;

        } catch (Exception e) {

            logger.error("Failed to load GameSnapshot from JSON file '{}': {}", filePath, e.getMessage(), e);

            throw new LoadException(
                    "Unable to load GameSnapshot from JSON file: " + filePath,
                    e
            );
        }
    }
}