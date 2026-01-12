package strategy.IO;

import com.google.gson.Gson;
import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonDeserializationStrategy implements DeserializationStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonDeserializationStrategy.class);

    @Override
    public GameSnapshot deserialize(String data) {

        logger.info("Starting JSON deserialization");

        try {
            Gson gson = new Gson();
            GameSnapshot snapshot = gson.fromJson(data, GameSnapshot.class);

            logger.debug("JSON deserialization completed successfully");
            return snapshot;

        } catch (Exception e) {

            logger.error("JSON deserialization failed: {}", e.getMessage(), e);

            throw new DeserializationException(
                    "Failed to deserialize GameSnapshot from JSON",
                    e
            );
        }
    }
}