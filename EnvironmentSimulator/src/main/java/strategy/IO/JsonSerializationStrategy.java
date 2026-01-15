package strategy.IO;

import com.google.gson.Gson;
import exceptionShielding.ExceptionShieldingLayer;
import memento.GameSnapshot.GameSnapshot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Concrete strategy for serializing game snapshots to JSON format.
 * <p>
 * Uses Gson library to convert GameSnapshot objects to JSON strings.
 * Implements exception shielding to convert low-level exceptions.
 * </p>
 */
public class JsonSerializationStrategy implements SerializationStrategy<String> {

    private static final Logger logger = LogManager.getLogger(JsonSerializationStrategy.class);

    @Override
    public String serialize(GameSnapshot snapshot) {
        logger.info("Serializing GameSnapshot to JSON format");

        // Use exception shielding to convert low-level serialization exceptions
        return ExceptionShieldingLayer.shieldSerialization(() -> {
            String json = new Gson().toJson(snapshot);
            logger.debug("GameSnapshot serialized into {} characters", json.length());
            return json;
        }, "GameSnapshot to JSON");
    }
}