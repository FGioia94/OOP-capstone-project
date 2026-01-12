package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface SaveStrategy {
    void save(GameSnapshot snapshot, String filePath);
}