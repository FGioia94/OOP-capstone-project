package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

/**
 * Strategy interface for saving game snapshots to persistent storage.
 * <p>
 * Part of the Strategy pattern, defining the contract for saving snapshots
 * in different formats (JSON, binary).
 * </p>
 */
public interface SaveStrategy {
    void save(GameSnapshot snapshot, String filePath);
}