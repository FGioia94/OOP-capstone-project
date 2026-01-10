package factoryMethod.AnimalFactory;

public class AnimalNotFoundException extends RuntimeException {
    private AnimalRepository repository;

    public AnimalNotFoundException(String id, AnimalRepository repository) {
        String message = "Animal with ID '" + id + "' not found. Candidates are:";
        for (String animalId : repository.listAll()) {
            message += "\n- " + animalId;
        }
        super(message);


    }


}

