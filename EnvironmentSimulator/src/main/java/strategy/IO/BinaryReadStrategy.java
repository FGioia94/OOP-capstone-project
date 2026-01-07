package strategy.IO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BinaryReadStrategy implements ReadStrategy<byte[]> {
    @Override
    public byte[] read(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        return Files.readAllBytes(path);
    }
}
