package strategy.IO;

import com.google.gson.Gson;
import memento.GameSnapshot.GameSnapshot;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class BinaryDeserializationStrategy implements DeserializationStrategy<byte[]> {
    @Override
    public GameSnapshot deserialize(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (GameSnapshot) ois.readObject();
    }
}