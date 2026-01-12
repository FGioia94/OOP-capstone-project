package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonSaveStrategy implements SaveStrategy {

    private static final Logger logger = LogManager.getLogger(JsonSaveStrategy.class);

    @Override
    public void save(GameSnapshot snapshot, String filePath) {

        logger.info("Saving GameSnapshot to JSON file '{}'", filePath);

        JsonSerializationStrategy serializationStrategy = new JsonSerializationStrategy();
        JsonPersistenceStrategy persistenceStrategy = new JsonPersistenceStrategy(filePath);

        try {
            String serializedData = serializationStrategy.serialize(snapshot);

            logger.debug("Snapshot serialized into {} characters", serializedData.length());

            persistenceStrategy.save(serializedData);

            logger.info("GameSnapshot successfully saved to JSON file '{}'", filePath);

        } catch (Exception e) {

            logger.error("Failed to save GameSnapshot to JSON file '{}': {}", filePath, e.getMessage(), e);

            throw new SaveException(
                    "Unable to save GameSnapshot to JSON file: " + filePath,
                    e
            );
        }
    }
}