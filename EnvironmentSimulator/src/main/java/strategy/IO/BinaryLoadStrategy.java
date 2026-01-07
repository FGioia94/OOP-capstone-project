package strategy.IO;

import factoryMethod.AnimalFactory.AnimalRepository;
import memento.GameSnapshot.AnimalRepositoryState;
import memento.GameSnapshot.GameSnapshot;

public class BinaryLoadStrategy implements LoadStrategy {
    @Override
    public GameSnapshot load(String filePath) throws Exception {
        BinaryReadStrategy readStrategy = new BinaryReadStrategy();
        BinaryDeserializationStrategy deserializationStrategy = new BinaryDeserializationStrategy();

        byte[] data = readStrategy.read(filePath);
        return deserializationStrategy.deserialize(data);


    }
}
