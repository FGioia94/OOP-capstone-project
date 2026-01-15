package strategy.IO;

import java.nio.file.Path;

/**
 * Abstract base class for persisting serialized data to files.
 * <p>
 * Provides common path handling for concrete persistence strategies
 * (JSON and binary). Part of the Strategy pattern.
 * </p>
 * @param <T> The type of serialized data to persist
 */
public abstract class PersistenceStrategy<T>  {

    protected final Path path;

    public PersistenceStrategy(String filePath) {
        this.path = Path.of(filePath);
    }

    abstract void save(T serialized);
}