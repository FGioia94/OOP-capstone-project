package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete strategy for loading game snapshots from binary files.
 * <p>
 * Combines binary reading and deserialization to restore snapshots
 * from compact binary format.
 * </p>
 */
public class BinaryLoadStrategy implements LoadStrategy {

    private static final Logger logger = LogManager.getLogger(BinaryLoadStrategy.class);

    @Override
    public GameSnapshot load(String filePath) {

        logger.info("Starting binary load from file '{}'", filePath);

        BinaryReadStrategy readStrategy = new BinaryReadStrategy();
        BinaryDeserializationStrategy deserializationStrategy = new BinaryDeserializationStrategy();

        try {
            byte[] data = readStrategy.read(filePath);

            logger.debug("Read {} bytes from '{}'", data.length, filePath);

            GameSnapshot snapshot = deserializationStrategy.deserialize(data);

            logger.info("Binary load completed successfully for '{}'", filePath);

            return snapshot;

        } catch (Exception e) {

            logger.error("Failed to load snapshot from '{}': {}", filePath, e.getMessage(), e);

            throw new LoadException(
                    "Unable to load game snapshot from file: " + filePath,
                    e
            );
        }
    }
}