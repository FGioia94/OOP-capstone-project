package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

/**
 * Strategy interface for deserializing data back into game snapshots.
 * <p>
 * Part of the Strategy pattern, allowing different deserialization implementations
 * (JSON from String, binary from byte[]) to be used interchangeably.
 * </p>
 * @param <T> The input type of the deserialization
 */
public interface DeserializationStrategy<T> {
    public GameSnapshot deserialize(T data) throws Exception;
}
