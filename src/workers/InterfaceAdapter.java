package workers;

public class InterfaceAdapter implements FileInterface {
	
	public String rawToType(String raw, boolean input) throws Exception {
		throw new Exception("Operation not implemented in selected type");
	}
	
	public void saveCurrinput() {
		
	}
	
	public void addCurrTestCase() {
		
	}
	
	public void writeTestCases(String filename) {
		
	}
	
	public void random(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void permute(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void dupMin(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void dupMax(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void randomMultiplication(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void applyFunction(boolean input, String function)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public String applyFunction(boolean input, String valueIn, String expression) throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void reverse(boolean input)throws Exception{
        throw new Exception("Operation not implemented in selected type");
    };

    public void duplicate(boolean input) throws Exception{
        throw new Exception("Operation not implemented in selected type");
    }

	@Override
	public Object readFile(String filename) throws Exception {
		throw new Exception("Operation not implemented in selected type");
	}

	@Override
	public <T> Class<T> getType() throws Exception {
		throw new Exception("Operation not implemented in selected type");
	}

	@Override
	public boolean checkEqual(String path1, String path2) throws Exception {
		throw new Exception("Operation not implemented in selected type");
	}

	
}
