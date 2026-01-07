package strategy.IO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReadStrategy implements ReadStrategy<String> {
    @Override
    public String read(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        return Files.readString(path);
    }
}

