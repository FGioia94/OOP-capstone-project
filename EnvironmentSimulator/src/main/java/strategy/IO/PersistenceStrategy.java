package strategy.IO;

import java.io.IOException;
import java.nio.file.Path;

public abstract class PersistenceStrategy<T>  {
    protected final Path path;

    public PersistenceStrategy(String filePath) {
        this.path = Path.of(filePath);
    }

    abstract void save(T serialized) throws IOException;

}
