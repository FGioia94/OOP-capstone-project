package annotations;

import builder.MapBuilder.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Parameter;

/**
 * Utility class for validating positions annotated with @ValidPosition.
 * Uses reflection to read annotation metadata and perform validation.
 */
public class PositionValidator {

    private static final Logger logger = LogManager.getLogger(PositionValidator.class);

    /**
     * Validates a position based on @ValidPosition annotation constraints.
     * 
     * @param position The position to validate
     * @param annotation The @ValidPosition annotation with validation rules
     * @throws IllegalArgumentException if validation fails
     */
    public static void validate(Position position, ValidPosition annotation) {
        logger.debug("Validating position with annotation constraints");

        // Check nullable
        if (position == null) {
            if (!annotation.nullable()) {
                String message = annotation.message() + ": Position cannot be null";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
            return; // null is allowed
        }

        // Validate X coordinate
        if (position.x() < annotation.minX()) {
            String message = annotation.message() + 
                ": X coordinate " + position.x() + " is less than minimum " + annotation.minX();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (annotation.maxX() >= 0 && position.x() >= annotation.maxX()) {
            String message = annotation.message() + 
                ": X coordinate " + position.x() + " exceeds maximum " + annotation.maxX();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        // Validate Y coordinate
        if (position.y() < annotation.minY()) {
            String message = annotation.message() + 
                ": Y coordinate " + position.y() + " is less than minimum " + annotation.minY();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        if (annotation.maxY() >= 0 && position.y() >= annotation.maxY()) {
            String message = annotation.message() + 
                ": Y coordinate " + position.y() + " exceeds maximum " + annotation.maxY();
            logger.error(message);
            throw new IllegalArgumentException(message);
        }

        logger.debug("Position {} passed validation", position);
    }

    /**
     * Validates a position using default constraints (non-null, non-negative coordinates).
     * 
     * @param position The position to validate
     * @throws IllegalArgumentException if validation fails
     */
    public static void validateDefault(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (position.x() < 0 || position.y() < 0) {
            throw new IllegalArgumentException(
                "Position coordinates must be non-negative: " + position);
        }
    }

    /**
     * Checks if a position is valid according to annotation constraints.
     * 
     * @param position The position to check
     * @param annotation The @ValidPosition annotation with validation rules
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Position position, ValidPosition annotation) {
        try {
            validate(position, annotation);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
