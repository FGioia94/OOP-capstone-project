package factoryMethod.AnimalFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception thrown when an animal lookup fails.
 * <p>
 * Provides detailed error messages including the missing ID and
 * a list of valid animal IDs in the repository to aid debugging.
 * </p>
 */
public class AnimalNotFoundException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(AnimalNotFoundException.class);


    public AnimalNotFoundException(String id, AnimalRepository repository) {
        super(buildMessage(id, repository));

        logger.error("AnimalNotFoundException thrown for ID '{}'. Repository contains {} animals.",
                id, repository.listAll().size());
    }

    private static String buildMessage(String id, AnimalRepository repository) {
        StringBuilder message = new StringBuilder(
                "Animal with ID '" + id + "' not found. Candidates are:"
        );

        for (String animalId : repository.listAll()) {
            message.append("\n- ").append(animalId);
        }

        return message.toString();
    }
}