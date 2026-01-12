package strategy.IO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BinaryReadStrategy implements ReadStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinaryReadStrategy.class);

    @Override
    public byte[] read(String filePath) {

        logger.info("Reading binary snapshot from '{}'", filePath);

        Path path = Paths.get(filePath);

        try {
            byte[] data = Files.readAllBytes(path);

            logger.debug("Successfully read {} bytes from '{}'", data.length, filePath);

            return data;

        } catch (Exception e) {

            logger.error("Failed to read binary snapshot from '{}': {}", filePath, e.getMessage(), e);

            throw new ReadException(
                    "Unable to read binary snapshot from file: " + filePath,
                    e
            );
        }
    }
}