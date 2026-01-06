package strategy.IO;

import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;

public class JsonSerializationStrategy implements SerializationStrategy {
    @Override
    public byte[] serialize(Object data) {
        String json = new Gson().toJson(data);
        return json.getBytes(StandardCharsets.UTF_8);

    }


}
