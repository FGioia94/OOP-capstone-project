package factoryMethod.AnimalFactory;

// ANIMAL FACTORY IS THE CREATOR OF THE FACTORY METHOD PATTERN
abstract class AnimalFactory {
    // Declaring the FACTORY METHOD
    protected abstract Animal createAnimal();
}
