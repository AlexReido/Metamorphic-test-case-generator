package command;

import java.util.ArrayList;

public class Branch {
	/*
	 * Used to store each branch/ relation
	 */
    private ArrayList<String> inOperations, outOperations;

    private String inputType, outputType, inputExpression, outputExpression, name;
    
    private Integer inReps, outReps;


     
    public Branch(String name, ArrayList<String> inOperations, ArrayList<String> outOperations, String inputType, String outputType) {
        this.name = name;
        this.inputType = inputType;
        this.outputType = outputType;
        this.inOperations = inOperations;
        this.outOperations = outOperations;
    }
    
    public void setInReps(int repsIn) {
		inReps = repsIn;
	}
    
    public void setOutReps(int repsOut) {
		outReps = repsOut;
	}
    
    public void setInputExpression(String expressionIn) {
		inputExpression = expressionIn;
	}
    
    public void setOutputExpression(String expressionOut) {
		outputExpression = expressionOut;
	}
    
    public String getInputExpression() {
        if (inputExpression != null) {
            return inputExpression;
        } else {
            return null; //throw new NullPointerException("No Expression found (Input)");
        }
    }

    public String getOutputExpression() {
        if (outputExpression != null) {
            System.out.println("OUTPUT EXPRE :" + outputExpression);
            return outputExpression;
        } else {
            return null; //throw new NullPointerException("No Expression found (Output)");
        }

    }

    public int getInReps() {
    	 if (inReps != null) {
             return inReps;
         } else {
             return 1;//throw new NullPointerException("No reps found (Input)");
         }
	}
    
    public int getOutReps() {
    	if (outReps != null) {
            return outReps;
        } else {
        	return 1;//throw new NullPointerException("No reps found (Output)");
        }
	}
    
    public String getName() {
        return name;
    }

    public ArrayList<String> getInOperations() {
        return inOperations;
    }

    public ArrayList<String> getOutOperations() {
        return outOperations;
    }

    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }
}
