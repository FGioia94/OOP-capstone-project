package strategy.IO;

public interface SerializationStrategy {
    public byte[] serialize(Object data);
}
