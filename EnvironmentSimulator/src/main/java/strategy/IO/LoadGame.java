package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadGame {
    public static GameSnapshot load(String fileName, String type) throws Exception {
        // Project root (EnvironmentSimulator/)
        String projectRoot = System.getProperty("user.dir");

        // Full path to src/data/saved
        Path saveDir = Paths.get(projectRoot, "src", "data", "saved");


        String extension;
        LoadStrategy strategy = null;
        switch (type) {
            case "json":
                strategy = new JsonLoadStrategy();
                extension = ".json";
                break;
            case "binary":
                strategy = new BinaryLoadStrategy();
                extension = ".bin";
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type: " + type);
        }

        if (!fileName.toLowerCase().endsWith(extension)) {
            fileName = fileName + extension;
        }

        // Build full file path by joining saveDir and fileName
        Path fullPath = saveDir.resolve(fileName);

        // Ensure file exists
        if (!Files.exists(fullPath)) {
            throw new Exception("Load directory does not exist: " + saveDir.toString());
        }
        GameSnapshot snapshot = strategy.load(fullPath.toString());
        System.out.println("Game loaded from " + fullPath.toString());
        return snapshot;

    }
}
