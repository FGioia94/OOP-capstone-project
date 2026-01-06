package strategy.IO;

public interface SaveStrategy {
    public void save(Object data, String filePath) throws Exception;
}
