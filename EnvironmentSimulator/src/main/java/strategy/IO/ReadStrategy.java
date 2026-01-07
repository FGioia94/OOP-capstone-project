package strategy.IO;

public interface ReadStrategy<T> {
    public T read(String filePath) throws Exception;
}
