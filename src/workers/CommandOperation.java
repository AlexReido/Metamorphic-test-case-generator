package workers;

public interface CommandOperation {
    void operation(FileInterface fileInterface, boolean input) throws Exception;
}
