package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveGame {

    public static void save(String fileName, GameSnapshot snapshot, String type) throws Exception {

        // Project root (EnvironmentSimulator/)
        String projectRoot = System.getProperty("user.dir");

        // Full path to src/data/saved
        Path saveDir = Paths.get(projectRoot, "src", "data", "saved");

        // Ensure directory exists
        if (!Files.exists(saveDir)) {
            Files.createDirectories(saveDir);
        }

        // Select strategy
        SaveStrategy strategy;
        String extension;
        switch (type.toLowerCase()) {
            case "json":
                strategy = new JsonSaveStrategy();
                extension = ".json";
                break;

            case "binary":
                strategy = new BinarySaveStrategy();
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

        // Save
        strategy.save(snapshot, fullPath.toString());
        System.out.println("Game saved to " + fullPath.toString());
    }
}