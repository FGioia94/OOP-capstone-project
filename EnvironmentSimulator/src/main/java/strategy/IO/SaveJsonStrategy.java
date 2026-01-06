package strategy.IO;

import java.io.IOException;

public class SaveJsonStrategy implements SaveStrategy{
    @Override
    public void save(Object data, String filePath) throws IOException {
        JsonSerializationStrategy serializationStrategy = new JsonSerializationStrategy();
        JsonPersistenceStrategy persistenceStrategy = new JsonPersistenceStrategy(filePath);

        try {
            byte[] serializedData = serializationStrategy.serialize(data);
            persistenceStrategy.save(serializedData);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
