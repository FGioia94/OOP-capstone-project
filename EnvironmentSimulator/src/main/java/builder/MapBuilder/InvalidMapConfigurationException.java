package builder.MapBuilder;

import java.text.MessageFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception thrown when a {@code MapBuilder} instance contains an invalid or inconsistent
 * configuration. This typically indicates that one or more structural constraints
 * (such as width, height, or terrain placement rules) failed validation during
 * map construction.
 *
 */
public class InvalidMapConfigurationException extends Exception {

    private static final Logger logger = LogManager.getLogger(InvalidMapConfigurationException.class);

    /**
     * Creates a new {@code InvalidMapConfigurationException} with a detailed message
     * describing which configuration elements failed validation.
     *
     * @param isWidthValid      whether the provided width value is valid
     * @param width             the width value supplied to the builder
     * @param isHeightValid     whether the provided height value is valid
     * @param height            the height value supplied to the builder
     * @param isWaterValid      whether the water positions satisfy validation rules
     * @param isGrassValid      whether the grass positions satisfy validation rules
     * @param isObstaclesValid  whether the obstacle positions satisfy validation rules
     */
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

    /**
     * Builds a structured and human‑readable error message summarizing all validation
     * results. This method is used internally by the constructor to ensure consistent
     * formatting.
     *
     * @param isWidthValid      validation flag for width
     * @param width             provided width value
     * @param isHeightValid     validation flag for height
     * @param height            provided height value
     * @param isWaterValid      validation flag for water positions
     * @param isGrassValid      validation flag for grass positions
     * @param isObstaclesValid  validation flag for obstacle positions
     * @return a formatted message describing the invalid configuration
     */
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