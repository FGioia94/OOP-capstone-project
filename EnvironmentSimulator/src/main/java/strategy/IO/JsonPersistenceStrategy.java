package strategy.IO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Concrete strategy for persisting JSON strings to files.
 * <p>
 * Writes JSON data to disk using UTF-8 encoding.
 * </p>
 */
public class JsonPersistenceStrategy extends PersistenceStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonPersistenceStrategy.class);

    public JsonPersistenceStrategy(String filePath) {
        super(filePath);
    }

    @Override
    public void save(String serialized) {

        logger.info("Saving JSON snapshot to '{}'", path);

        try {
            Files.writeString(path, serialized, StandardCharsets.UTF_8);

            logger.debug("Successfully wrote {} characters to '{}'", serialized.length(), path);

        } catch (IOException e) {

            logger.error("Failed to save JSON snapshot to '{}': {}", path, e.getMessage(), e);

            throw new SaveException(
                    "Unable to save JSON snapshot to file: " + path,
                    e
            );
        }
    }
}