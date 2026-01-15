package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete strategy for saving game snapshots to binary files.
 * <p>
 * Combines binary serialization and persistence to save snapshots
 * in compact binary format.
 * </p>
 */
public class BinarySaveStrategy implements SaveStrategy {

    private static final Logger logger = LogManager.getLogger(BinarySaveStrategy.class);

    @Override
    public void save(GameSnapshot snapshot, String filePath) {

        logger.info("Saving GameSnapshot to '{}'", filePath);

        BinarySerializationStrategy serializationStrategy = new BinarySerializationStrategy();
        BinaryPersistenceStrategy persistenceStrategy = new BinaryPersistenceStrategy(filePath);

        try {
            byte[] serializedData = serializationStrategy.serialize(snapshot);

            logger.debug("Snapshot serialized into {} bytes", serializedData.length);

            persistenceStrategy.save(serializedData);

            logger.info("GameSnapshot successfully saved to '{}'", filePath);

        } catch (Exception e) {

            logger.error("Failed to save GameSnapshot to '{}': {}", filePath, e.getMessage(), e);

            throw new SaveException(
                    "Unable to save GameSnapshot to file: " + filePath,
                    e
            );
        }
    }
}