package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface DeserializationStrategy<T> {
    public GameSnapshot deserialize(T data) throws Exception;
}
