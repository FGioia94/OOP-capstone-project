package strategy.IO;

import com.google.gson.Gson;
import memento.GameSnapshot.GameSnapshot;


public class JsonDeserializationStrategy implements DeserializationStrategy<String> {
    @Override
    public GameSnapshot deserialize(String data) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(data, GameSnapshot.class);
    }
}