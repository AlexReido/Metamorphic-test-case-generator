package command;

public class SourceTest {
    private String inVal, outVal, inType, outType;

    public SourceTest(String input, String inType, String output, String outType){
        this.inVal = input;
        this.inType = inType;
        this.outVal = output;
        this.outType = outType;
    }

    public String getInput(){
        return inVal;
    }

    public String getInType(){
        return inType;
    }

    public String getOutput(){
        return outVal;
    }

    public String getOutType(){
        return outType;
    }
}
