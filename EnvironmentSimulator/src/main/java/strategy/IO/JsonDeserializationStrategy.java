package strategy.IO;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;

public class JsonDeserializationStrategy implements DeserializationStrategy {
    @Override
    public GameSnapshot deserialize(byte[] data) throws Exception {
        Gson gson = new Gson();
        String jsonString = new String(data, StandardCharsets.UTF_8);
        return gson.fromJson(jsonString, GameSnapshot.class);
    }
}