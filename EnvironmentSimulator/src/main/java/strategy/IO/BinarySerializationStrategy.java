package strategy.IO;

import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class BinarySerializationStrategy implements SerializationStrategy<byte[]> {

    private static final Logger logger = LogManager.getLogger(BinarySerializationStrategy.class);

    @Override
    public byte[] serialize(GameSnapshot snapshot) {

        logger.info("Serializing GameSnapshot to binary format");

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(snapshot);
            oos.flush();

            byte[] data = bos.toByteArray();

            logger.debug("GameSnapshot serialized into {} bytes", data.length);

            return data;

        } catch (Exception e) {

            logger.error("Binary serialization failed: {}", e.getMessage(), e);

            throw new SerializationException(
                    "Failed to serialize GameSnapshot to binary format",
                    e
            );
        }
    }
}