package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

import java.io.IOException;

public class JsonSaveStrategy implements SaveStrategy {
    @Override
    public void save(GameSnapshot snapshot, String filePath) throws IOException {
        JsonSerializationStrategy serializationStrategy = new JsonSerializationStrategy();
        JsonPersistenceStrategy persistenceStrategy = new JsonPersistenceStrategy(filePath);

        try {
            String serializedData = serializationStrategy.serialize(snapshot);
            persistenceStrategy.save(serializedData);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
