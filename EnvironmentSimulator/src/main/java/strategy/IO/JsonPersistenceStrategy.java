package strategy.IO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;

public class JsonPersistenceStrategy implements PersistenceStrategy {
    private final Path path;

    public JsonPersistenceStrategy(String filePath) {
        this.path = Path.of(filePath);
    }

    @Override
    public void save(byte[] serialized) throws IOException {
        Files.write(path, serialized);
    }
}
