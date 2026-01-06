package strategy.IO;

public interface DeserializationStrategy {
    public GameSnapshot deserialize(byte[] data) throws Exception;
}
