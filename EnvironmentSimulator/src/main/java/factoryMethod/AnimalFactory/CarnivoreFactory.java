package factoryMethod.AnimalFactory;

public class CarnivoreFactory extends AnimalFactory {
    @Override
    public Animal createAnimal() {
        return new Carnivore();
    }
}
