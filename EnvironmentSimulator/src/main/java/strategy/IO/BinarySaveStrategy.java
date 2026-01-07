package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

import java.io.IOException;

public class BinarySaveStrategy implements SaveStrategy {
    @Override
    public void save(GameSnapshot snapshot, String filePath) throws IOException {
        BinarySerializationStrategy serializationStrategy = new BinarySerializationStrategy();
        BinaryPersistenceStrategy persistenceStrategy = new BinaryPersistenceStrategy(filePath);

        try {
            byte[] serializedData = serializationStrategy.serialize(snapshot);
            persistenceStrategy.save(serializedData);
        } catch (IOException e) {
            throw new IOException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
