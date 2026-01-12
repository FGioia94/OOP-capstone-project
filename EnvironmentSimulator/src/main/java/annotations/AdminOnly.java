package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a command handler as requiring admin privileges.
 * Command handlers annotated with @AdminOnly will only be added to the 
 * command chain when the game is running in admin mode.
 * 
 * Example usage:
 * <pre>
 * @AdminOnly
 * public class InspectCommandHandler extends CommandHandler {
 *     // This handler only available in admin mode
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AdminOnly {
    /**
     * Optional reason/description for why admin privileges are required.
     */
    String reason() default "This command requires administrative privileges";
}
