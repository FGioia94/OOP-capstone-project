package strategy.IO;

import java.io.IOException;

public class SaveBinaryStrategy implements SaveStrategy {
    @Override
    public void save(Object data, String filePath) throws IOException {
        BinarySerializationStrategy serializationStrategy = new BinarySerializationStrategy();
        BinaryPersistenceStrategy persistenceStrategy = new BinaryPersistenceStrategy();

        try {
            byte[] serializedData = serializationStrategy.serialize(data);
            persistenceStrategy.save(serializedData);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
