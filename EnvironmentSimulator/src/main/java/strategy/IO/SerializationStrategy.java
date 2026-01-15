package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

/**
 * Strategy interface for serializing game snapshots to different formats.
 * <p>
 * Part of the Strategy pattern, allowing different serialization implementations
 * (JSON, binary) to be used interchangeably.
 * </p>
 * @param <T> The output type of the serialization (String for JSON, byte[] for binary)
 */
public interface SerializationStrategy<T> {
    T serialize(GameSnapshot snapshot);
}