package strategy.IO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Files;

public class JsonPersistenceStrategy extends PersistenceStrategy<String> {


    public JsonPersistenceStrategy(String filePath) {
        super(filePath);
    }

    @Override
    public void save(String serialized) throws IOException {
        Files.writeString(path, serialized, StandardCharsets.UTF_8);
    }
}
