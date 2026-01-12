package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class BinaryDeserializationStrategy implements DeserializationStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinaryDeserializationStrategy.class);

    @Override
    public GameSnapshot deserialize(byte[] data) {

        logger.debug("Starting binary deserialization. Payload size={} bytes",
                data != null ? data.length : -1);

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            GameSnapshot snapshot = (GameSnapshot) ois.readObject();

            logger.info("Binary deserialization completed successfully.");
            return snapshot;

        } catch (Exception e) {

            logger.error("Binary deserialization failed: {}", e.getMessage(), e);

            throw new DeserializationException(
                    "Failed to deserialize GameSnapshot from binary data.",
                    e
            );
        }
    }
}