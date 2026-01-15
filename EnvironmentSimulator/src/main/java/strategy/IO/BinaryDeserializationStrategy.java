package strategy.IO;

import exceptionShielding.ExceptionShieldingLayer;
import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * Concrete strategy for deserializing game snapshots from binary format.
 * <p>
 * Uses Java's ObjectInputStream to convert byte arrays back to GameSnapshot objects.
 * Implements exception shielding to convert low-level exceptions.
 * </p>
 */
public class BinaryDeserializationStrategy implements DeserializationStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinaryDeserializationStrategy.class);

    @Override
    public GameSnapshot deserialize(byte[] data) {
        logger.debug("Starting binary deserialization. Payload size={} bytes",
                data != null ? data.length : -1);

        // Use exception shielding to convert low-level serialization exceptions
        return ExceptionShieldingLayer.shieldDeserialization(() -> {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {

                GameSnapshot snapshot = (GameSnapshot) ois.readObject();
                logger.info("Binary deserialization completed successfully.");
                return snapshot;
            }
        }, "GameSnapshot from binary data");
    }
}