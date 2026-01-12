package strategy.IO;

import com.google.gson.Gson;
import exceptionShielding.ExceptionShieldingLayer;
import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JsonDeserializationStrategy implements DeserializationStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonDeserializationStrategy.class);

    @Override
    public GameSnapshot deserialize(String data) {
        logger.info("Starting JSON deserialization");

        // Use exception shielding to convert low-level JSON exceptions
        return ExceptionShieldingLayer.shieldDeserialization(() -> {
            Gson gson = new Gson();
            GameSnapshot snapshot = gson.fromJson(data, GameSnapshot.class);
            logger.debug("JSON deserialization completed successfully");
            return snapshot;
        }, "GameSnapshot from JSON");
    }
}