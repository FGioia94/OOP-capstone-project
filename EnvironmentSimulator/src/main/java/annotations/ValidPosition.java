package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Position parameter for validation.
 * The annotated parameter will be validated to ensure:
 * - The position is not null
 * - The coordinates are within valid map bounds
 * 
 * Example usage:
 * <pre>
 * public void setPosition(@ValidPosition Position position) {
 *     this.position = position;
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface ValidPosition {
    /**
     * Minimum allowed X coordinate (inclusive).
     */
    int minX() default 0;
    
    /**
     * Minimum allowed Y coordinate (inclusive).
     */
    int minY() default 0;
    
    /**
     * Maximum allowed X coordinate (exclusive).
     * -1 means no maximum limit.
     */
    int maxX() default -1;
    
    /**
     * Maximum allowed Y coordinate (exclusive).
     * -1 means no maximum limit.
     */
    int maxY() default -1;
    
    /**
     * Whether null values are allowed.
     */
    boolean nullable() default false;
    
    /**
     * Custom error message when validation fails.
     */
    String message() default "Invalid position";
}
