package strategy.IO;

import memento.GameSnapshot.GameSnapshot;

public interface SerializationStrategy<T> {
    T serialize(GameSnapshot snapshot);
}