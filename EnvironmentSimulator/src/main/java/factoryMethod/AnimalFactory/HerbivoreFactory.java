package factoryMethod.AnimalFactory;

public class HerbivoreFactory extends AnimalFactory {
    @Override
    protected Animal createAnimal() {
        return new Herbivore();
    }
}
