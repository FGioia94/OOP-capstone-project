package builder.MapBuilder;

import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvalidMapConfigurationException extends Exception {

    private static final Logger logger = LogManager.getLogger(InvalidMapConfigurationException.class);

    public InvalidMapConfigurationException(
            boolean isWidthValid,
            int width,
            boolean isHeightValid,
            int height,
            boolean isWaterValid,
            boolean isGrassValid,
            boolean isObstaclesValid
    ) {
        super(buildMessage(
                isWidthValid, width,
                isHeightValid, height,
                isWaterValid,
                isGrassValid,
                isObstaclesValid
        ));

        logger.debug(
                "InvalidMapConfigurationException thrown: widthValid={}, width={}, heightValid={}, height={}, waterValid={}, grassValid={}, obstaclesValid={}",
                isWidthValid, width,
                isHeightValid, height,
                isWaterValid,
                isGrassValid,
                isObstaclesValid
        );
    }

    private static String buildMessage(
            boolean isWidthValid, int width,
            boolean isHeightValid, int height,
            boolean isWaterValid,
            boolean isGrassValid,
            boolean isObstaclesValid
    ) {
        return MessageFormat.format(
                "Invalid MapBuilder configuration: " +
                        "isWidthValid: {0}, widthValue: {1}, " +
                        "isHeightValid: {2}, heightValue: {3}, " +
                        "isWaterValid: {4}, " +
                        "isGrassValid: {5}, " +
                        "isObstaclesValid: {6}",
                isWidthValid, width,
                isHeightValid, height,
                isWaterValid,
                isGrassValid,
                isObstaclesValid
        );
    }
}