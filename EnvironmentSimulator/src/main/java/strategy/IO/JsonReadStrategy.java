package strategy.IO;

import exceptionShielding.ExceptionShieldingLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Concrete strategy for reading JSON data from files.
 * <p>
 * Reads JSON content as strings from disk, with exception shielding
 * to convert low-level I/O exceptions.
 * </p>
 */
public class JsonReadStrategy implements ReadStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonReadStrategy.class);

    @Override
    public String read(String filePath) {
        logger.info("Reading JSON snapshot from '{}'", filePath);

        // Use exception shielding to convert low-level I/O exceptions
        return ExceptionShieldingLayer.shieldRead(() -> {
            Path path = Paths.get(filePath);
            String json = Files.readString(path);
            logger.debug("Successfully read {} characters from '{}'", json.length(), filePath);
            return json;
        }, filePath);
    }
}