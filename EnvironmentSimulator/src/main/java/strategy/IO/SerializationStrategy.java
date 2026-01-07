package strategy.IO;

import com.google.gson.Gson;
import memento.GameSnapshot.GameSnapshot;

import java.nio.charset.StandardCharsets;

public interface SerializationStrategy<T> {
    public T serialize(GameSnapshot snapshot) throws Exception;
}
