package annotations;

import chainOfResponsibility.commandHandler.CommandHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for checking @AdminOnly annotations on command handlers.
 * Uses reflection to determine if a handler requires admin privileges.
 */
public class AdminAnnotationChecker {

    private static final Logger logger = LogManager.getLogger(AdminAnnotationChecker.class);

    /**
     * Checks if a command handler is annotated with @AdminOnly.
     * 
     * @param handler The command handler to check
     * @return true if the handler requires admin mode, false otherwise
     */
    public static boolean requiresAdminMode(CommandHandler handler) {
        Class<?> handlerClass = handler.getClass();
        boolean isAdminOnly = handlerClass.isAnnotationPresent(AdminOnly.class); // checks if the handler is an admin-only handler
        
        if (isAdminOnly) {
            AdminOnly annotation = handlerClass.getAnnotation(AdminOnly.class);
            logger.debug("Handler {} requires admin mode: {}", 
                handlerClass.getSimpleName(), annotation.reason());
        }
        
        return isAdminOnly;
    }

    /**
     * Checks if a command handler class is annotated with @AdminOnly.
     * 
     * @param handlerClass The command handler class to check
     * @return true if the handler requires admin mode, false otherwise
     */
    // Overloaded method to check using class type
    public static boolean requiresAdminMode(Class<? extends CommandHandler> handlerClass) {
        boolean isAdminOnly = handlerClass.isAnnotationPresent(AdminOnly.class);
        
        if (isAdminOnly) {
            AdminOnly annotation = handlerClass.getAnnotation(AdminOnly.class);
            logger.debug("Handler class {} requires admin mode: {}", 
                handlerClass.getSimpleName(), annotation.reason());
        }
        
        return isAdminOnly;
    }

    /**
     * Gets the reason why admin mode is required for a handler.
     * 
     * @param handler The command handler
     * @return The reason string, or null if not admin-only
     */
    public static String getAdminReason(CommandHandler handler) {
        Class<?> handlerClass = handler.getClass();
        
        if (handlerClass.isAnnotationPresent(AdminOnly.class)) {
            AdminOnly annotation = handlerClass.getAnnotation(AdminOnly.class);
            return annotation.reason();
        }
        
        return null;
    }
}
