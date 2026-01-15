package strategy.IO;

import exceptionShielding.ExceptionShieldingLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Concrete strategy for reading binary data from files.
 * <p>
 * Reads binary content as byte arrays from disk, with exception shielding
 * to convert low-level I/O exceptions.
 * </p>
 */
public class BinaryReadStrategy implements ReadStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinaryReadStrategy.class);

    @Override
    public byte[] read(String filePath) {
        logger.info("Reading binary snapshot from '{}'", filePath);

        // Use exception shielding to convert low-level I/O exceptions
        return ExceptionShieldingLayer.shieldRead(() -> {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);
            logger.debug("Successfully read {} bytes from '{}'", data.length, filePath);
            return data;
        }, filePath);
    }
}