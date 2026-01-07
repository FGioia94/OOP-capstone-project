package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface LoadStrategy {
    public GameSnapshot load(String filePath) throws Exception;
}
