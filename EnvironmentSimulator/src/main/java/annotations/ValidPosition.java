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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface ValidPosition {
    int minX() default 0;

    int minY() default 0;

    // -1 means no maximum limit.
    int maxX() default -1;

    // -1 means no maximum limit.
    int maxY() default -1;

    boolean nullable() default false;

    String message() default "Invalid position";
}
