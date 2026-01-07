package strategy.IO;

import com.google.gson.Gson;
import memento.GameSnapshot.GameSnapshot;

public class JsonSerializationStrategy implements SerializationStrategy {
    @Override
    public String serialize(GameSnapshot snapshot) throws Exception {
        return new Gson().toJson(snapshot);
    }

}
