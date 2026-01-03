package builder.MapBuilder;

import java.text.MessageFormat;

public class InvalidMapConfigurationException extends Exception {

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
