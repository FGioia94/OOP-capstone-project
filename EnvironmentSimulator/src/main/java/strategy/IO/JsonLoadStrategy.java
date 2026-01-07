package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonLoadStrategy implements LoadStrategy {
    @Override
    public GameSnapshot load(String filePath) throws Exception {
        JsonReadStrategy readStrategy = new JsonReadStrategy();
        JsonDeserializationStrategy deserializationStrategy = new JsonDeserializationStrategy();

        try {
            String jsonData = readStrategy.read(filePath);
            return deserializationStrategy.deserialize(jsonData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
