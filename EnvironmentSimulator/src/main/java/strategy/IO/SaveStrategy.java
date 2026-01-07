package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface SaveStrategy {
    public void save(GameSnapshot snapshot, String filePath) throws Exception;
}
