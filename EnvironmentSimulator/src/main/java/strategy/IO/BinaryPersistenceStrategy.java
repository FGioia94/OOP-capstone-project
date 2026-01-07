package strategy.IO;

import java.io.IOException;
import java.nio.file.Files;

public class BinaryPersistenceStrategy extends PersistenceStrategy<byte[]> {

    public BinaryPersistenceStrategy(String filePath) {
        super(filePath);
    }

    @Override
    public void save(byte[] serialized) throws IOException {
        Files.write(path, serialized);
    }

}
