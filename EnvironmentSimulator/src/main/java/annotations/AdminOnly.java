package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a command handler as requiring admin privileges.
 * Command handlers annotated with @AdminOnly will only be added to the 
 * command chain when the game is running in admin mode.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminOnly {
    String reason() default "This command requires administrative privileges";
}
