package workers;

public interface FileInterface {
	public String rawToType(String raw, boolean input) throws Exception;
	public Object readFile(String filename) throws Exception;
	public <T> Class<T> getType() throws Exception;
	public void saveCurrinput();
	public void addCurrTestCase();
	public void writeTestCases(String filename);
    public boolean checkEqual(String path1, String path2) throws Exception;
    public void permute(boolean input) throws Exception;
    public void random(boolean input) throws Exception;
    public void dupMin(boolean input) throws Exception;
    public void dupMax(boolean input) throws Exception;
    public void randomMultiplication(boolean input) throws Exception;
    public void applyFunction(boolean input, String function) throws Exception;
    public String applyFunction(boolean input, String valueIn, String expression) throws Exception;
    public void reverse(boolean input) throws Exception;
    public void duplicate(boolean input) throws Exception;
}
