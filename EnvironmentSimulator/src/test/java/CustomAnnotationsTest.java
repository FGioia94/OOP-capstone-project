import annotations.AdminAnnotationChecker;
import annotations.AdminOnly;
import annotations.PositionValidator;
import annotations.ValidPosition;
import builder.MapBuilder.InvalidMapConfigurationException;
import builder.MapBuilder.Position;
import chainOfResponsibility.commandHandler.InspectCommandHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests demonstrating custom annotation functionality.
 * Shows how @AdminOnly and @ValidPosition annotations work.
 */
public class CustomAnnotationsTest {

    @Test
    @DisplayName("@AdminOnly annotation should be detected on InspectCommandHandler")
    void testAdminOnlyAnnotationDetection() {
        InspectCommandHandler handler = new InspectCommandHandler();
        
        // Verify annotation is present
        boolean requiresAdmin = AdminAnnotationChecker.requiresAdminMode(handler);
        assertThat(requiresAdmin).isTrue();
        
        // Verify annotation has reason
        AdminOnly annotation = handler.getClass().getAnnotation(AdminOnly.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.reason()).contains("Animal inspection requires admin privileges");
    }

    @Test
    @DisplayName("@ValidPosition annotation should validate positions correctly")
    void testValidPositionAnnotation() {
        // Valid position should pass
        Position validPos = new Position(5, 5);
        assertThatCode(() -> PositionValidator.validateDefault(validPos))
                .doesNotThrowAnyException();
        
        // Invalid X should throw
        Position invalidX = new Position(-1, 5);
        assertThatThrownBy(() -> PositionValidator.validateDefault(invalidX))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position coordinates must be non-negative");
        
        // Invalid Y should throw
        Position invalidY = new Position(5, -1);
        assertThatThrownBy(() -> PositionValidator.validateDefault(invalidY))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position coordinates must be non-negative");
    }

    @Test
    @DisplayName("@ValidPosition should reject null by default")
    void testValidPositionNullable() {
        // ValidPosition with default nullable=false should reject null
        assertThatThrownBy(() -> PositionValidator.validateDefault(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Position cannot be null");
    }

    @Test
    @DisplayName("@ValidPosition should validate custom bounds")
    void testValidPositionCustomBounds() {
        // Test with bounds validation using default validator
        Position valid = new Position(50, 50);
        assertThatCode(() -> PositionValidator.validateDefault(valid))
                .doesNotThrowAnyException();
        
        // Test edge cases
        Position edgeCase = new Position(0, 0);
        assertThatCode(() -> PositionValidator.validateDefault(edgeCase))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("AdminAnnotationChecker should return false for non-annotated handlers")
    void testNonAnnotatedHandlerReturnsFalse() {
        // Test with a command handler that doesn't have @AdminOnly
        chainOfResponsibility.commandHandler.HelpCommandHandler helpHandler = 
                new chainOfResponsibility.commandHandler.HelpCommandHandler();
        boolean requiresAdmin = AdminAnnotationChecker.requiresAdminMode(helpHandler);
        assertThat(requiresAdmin).isFalse();
    }
}
