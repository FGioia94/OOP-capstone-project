package strategy.IO;

import java.io.IOException;

public interface PersistenceStrategy {
    void save(byte[] data) throws IOException;

}
