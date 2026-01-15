package strategy.IO;

/**
 * Strategy interface for reading serialized data from files.
 * <p>
 * Part of the Strategy pattern, allowing different file reading implementations
 * for different formats (JSON as String, binary as byte[]).
 * </p>
 * @param <T> The type of data read from the file
 */
public interface ReadStrategy<T> {
    public T read(String filePath) throws Exception;
}
