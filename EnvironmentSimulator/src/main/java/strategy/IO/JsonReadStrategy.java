package strategy.IO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReadStrategy implements ReadStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonReadStrategy.class);

    @Override
    public String read(String filePath) {

        logger.info("Reading JSON snapshot from '{}'", filePath);

        Path path = Paths.get(filePath);

        try {
            String json = Files.readString(path);

            logger.debug("Successfully read {} characters from '{}'", json.length(), filePath);

            return json;

        } catch (Exception e) {

            logger.error("Failed to read JSON snapshot from '{}': {}", filePath, e.getMessage(), e);

            throw new ReadException(
                    "Unable to read JSON snapshot from file: " + filePath,
                    e
            );
        }
    }
}