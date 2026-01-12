package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface LoadStrategy {
    GameSnapshot load(String filePath);
}