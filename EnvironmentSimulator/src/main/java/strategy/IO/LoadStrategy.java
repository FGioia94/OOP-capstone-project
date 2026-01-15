package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

/**
 * Strategy interface for loading game snapshots from persistent storage.
 * <p>
 * Part of the Strategy pattern, defining the contract for loading snapshots
 * from different formats (JSON, binary).
 * </p>
 */
public interface LoadStrategy {
    GameSnapshot load(String filePath);
}