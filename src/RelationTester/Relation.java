package RelationTester;

public abstract class Relation<I, O>{
	private I input;
	private O output;

	public Relation (I input, O output) {
		this.input = input;
		this.output = output;
		
	}
	
	
}
