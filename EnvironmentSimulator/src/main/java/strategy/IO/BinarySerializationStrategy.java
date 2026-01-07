package strategy.IO;


import memento.GameSnapshot.GameSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class BinarySerializationStrategy implements SerializationStrategy {
    @Override
    public byte[] serialize(GameSnapshot snapshot) throws Exception {
        // To serialize using the built-in Java serialization,
        // all classes must implement Serializable
        // transient fields will not be serialized
        // Records are serializable by default

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(snapshot);
        oos.flush();
        return bos.toByteArray();
    }

}
