package strategy.IO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

public class BinaryPersistenceStrategy extends PersistenceStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinaryPersistenceStrategy.class);

    public BinaryPersistenceStrategy(String filePath) {
        super(filePath);
    }

    @Override
    public void save(byte[] serialized) {

        logger.info("Saving binary snapshot to '{}'", path);

        try {
            Files.write(path, serialized);

            logger.debug("Successfully wrote {} bytes to '{}'", serialized.length, path);

        } catch (IOException e) {

            logger.error("Failed to save binary snapshot to '{}': {}", path, e.getMessage(), e);

            throw new SaveException(
                    "Unable to save binary snapshot to file: " + path,
                    e
            );
        }
    }
}