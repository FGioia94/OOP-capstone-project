package factoryMethod.AnimalFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.*;
import java.util.*;

/**
 * Utility class demonstrating Java Reflection API usage.
 * Provides runtime introspection capabilities for Animal objects and Factory classes.
 * 
 * This class showcases:
 * - Class.forName() for dynamic class loading
 * - getDeclaredFields() for field introspection
 * - getDeclaredMethods() for method discovery
 * - Method.invoke() for dynamic method invocation
 * - Field.get() for accessing field values
 */
public class AnimalInspector {

    private static final Logger logger = LogManager.getLogger(AnimalInspector.class);

    /**
     * Inspects an Animal object and returns detailed information about its structure.
     * Uses reflection to access fields, methods, and runtime type information.
     * 
     * @param animal The animal to inspect
     * @return Map containing inspection results
     */
    public static Map<String, Object> inspectAnimal(AnimalComponent animal) {
        logger.info("Starting reflection-based inspection of animal: {}", animal.getId());
        
        Map<String, Object> inspection = new LinkedHashMap<>();
        Class<?> animalClass = animal.getClass();

        // Class information
        inspection.put("className", animalClass.getSimpleName());
        inspection.put("fullClassName", animalClass.getName());
        inspection.put("package", animalClass.getPackage().getName());
        inspection.put("superclass", animalClass.getSuperclass().getSimpleName());

        // Inspect interfaces
        Class<?>[] interfaces = animalClass.getInterfaces();
        List<String> interfaceNames = new ArrayList<>();
        for (Class<?> iface : interfaces) {
            interfaceNames.add(iface.getSimpleName());
        }
        inspection.put("interfaces", interfaceNames);

        // Inspect declared fields using reflection
        Field[] fields = animalClass.getDeclaredFields();
        Map<String, Object> fieldValues = new LinkedHashMap<>();
        for (Field field : fields) {
            try {
                field.setAccessible(true); // Access private fields
                Object value = field.get(animal);
                fieldValues.put(field.getName(), value);
                logger.debug("Field '{}' = {}", field.getName(), value);
            } catch (IllegalAccessException e) {
                fieldValues.put(field.getName(), "INACCESSIBLE");
                logger.warn("Could not access field: {}", field.getName());
            }
        }
        inspection.put("fields", fieldValues);

        // Inspect methods
        Method[] methods = animalClass.getDeclaredMethods();
        List<String> methodNames = new ArrayList<>();
        for (Method method : methods) {
            String signature = method.getName() + "(" + 
                             String.join(", ", Arrays.stream(method.getParameterTypes())
                                     .map(Class::getSimpleName)
                                     .toArray(String[]::new)) + ")";
            methodNames.add(signature);
        }
        inspection.put("methods", methodNames);

        logger.info("Inspection complete for animal: {}", animal.getId());
        return inspection;
    }

    /**
     * Dynamically invokes a getter method on an animal using reflection.
     * Demonstrates Method.invoke() usage.
     * 
     * @param animal The animal object
     * @param methodName Name of the getter method (e.g., "getHp", "getLevel")
     * @return The return value of the method, or null if invocation fails
     */
    public static Object invokeGetter(AnimalComponent animal, String methodName) {
        try {
            Class<?> animalClass = animal.getClass();
            Method method = animalClass.getMethod(methodName); // Get public method
            Object result = method.invoke(animal); // Invoke the method
            
            logger.info("Invoked method '{}' on {} - Result: {}", 
                       methodName, animal.getId(), result);
            return result;
            
        } catch (NoSuchMethodException e) {
            logger.error("Method '{}' not found on class {}", 
                        methodName, animal.getClass().getSimpleName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Failed to invoke method '{}': {}", methodName, e.getMessage());
        }
        return null;
    }

    /**
     * Dynamically loads an AnimalFactory class by name using reflection.
     * Demonstrates Class.forName() and newInstance() usage.
     * 
     * @param factoryClassName Simple class name (e.g., "CarnivoreFactory", "HerbivoreFactory")
     * @return A new instance of the factory, or null if loading fails
     */
    public static AnimalFactory loadFactoryByName(String factoryClassName) {
        try {
            String fullClassName = "factoryMethod.AnimalFactory." + factoryClassName;
            Class<?> factoryClass = Class.forName(fullClassName);
            
            logger.info("Dynamically loaded class: {}", fullClassName);
            
            // Check if it's actually an AnimalFactory
            if (!AnimalFactory.class.isAssignableFrom(factoryClass)) {
                logger.error("Class {} is not an AnimalFactory", fullClassName);
                return null;
            }
            
            // Create new instance using reflection
            Constructor<?> constructor = factoryClass.getDeclaredConstructor();
            AnimalFactory factory = (AnimalFactory) constructor.newInstance();
            
            logger.info("Successfully instantiated factory: {}", factoryClassName);
            return factory;
            
        } catch (ClassNotFoundException e) {
            logger.error("Factory class not found: {}", factoryClassName);
        } catch (NoSuchMethodException e) {
            logger.error("No default constructor found for: {}", factoryClassName);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("Failed to instantiate factory '{}': {}", factoryClassName, e.getMessage());
        }
        return null;
    }

    /**
     * Discovers all AnimalFactory implementations in the package using reflection.
     * This is a simplified version - in production, you'd use classpath scanning.
     * 
     * @return List of available factory class names
     */
    public static List<String> discoverFactories() {
        logger.info("Discovering available AnimalFactory implementations");
        
        // In a real implementation, you'd scan the classpath
        // For this demo, we'll check known factories
        List<String> factories = new ArrayList<>();
        String[] knownFactories = {"CarnivoreFactory", "HerbivoreFactory"};
        
        for (String factoryName : knownFactories) {
            try {
                String fullClassName = "factoryMethod.AnimalFactory." + factoryName;
                Class<?> clazz = Class.forName(fullClassName);
                
                // Verify it extends AnimalFactory
                if (AnimalFactory.class.isAssignableFrom(clazz)) {
                    factories.add(factoryName);
                    logger.debug("Found factory: {}", factoryName);
                }
            } catch (ClassNotFoundException e) {
                logger.debug("Factory not found: {}", factoryName);
            }
        }
        
        logger.info("Discovered {} factories", factories.size());
        return factories;
    }

    /**
     * Compares two animals using reflection to find differences in field values.
     * 
     * @param animal1 First animal
     * @param animal2 Second animal
     * @return Map of field names to their different values
     */
    public static Map<String, String> compareAnimals(AnimalComponent animal1, AnimalComponent animal2) {
        Map<String, String> differences = new LinkedHashMap<>();
        
        Class<?> class1 = animal1.getClass();
        Class<?> class2 = animal2.getClass();
        
        if (!class1.equals(class2)) {
            differences.put("type", class1.getSimpleName() + " vs " + class2.getSimpleName());
        }
        
        // Compare common fields using reflection
        String[] commonFields = {"hp", "level", "sex", "position"};
        for (String fieldName : commonFields) {
            try {
                Object value1 = invokeGetter(animal1, "get" + capitalize(fieldName));
                Object value2 = invokeGetter(animal2, "get" + capitalize(fieldName));
                
                if (value1 != null && !value1.equals(value2)) {
                    differences.put(fieldName, value1 + " vs " + value2);
                }
            } catch (Exception e) {
                logger.debug("Could not compare field: {}", fieldName);
            }
        }
        
        return differences;
    }

    /**
     * Prints a detailed inspection report to console.
     * 
     * @param animal The animal to inspect
     */
    public static void printInspectionReport(AnimalComponent animal) {
        System.out.println("\n=== REFLECTION-BASED ANIMAL INSPECTION ===");
        Map<String, Object> inspection = inspectAnimal(animal);
        
        for (Map.Entry<String, Object> entry : inspection.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("==========================================\n");
    }

    // Helper method
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
