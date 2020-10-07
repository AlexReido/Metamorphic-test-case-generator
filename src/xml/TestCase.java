package xml;

import java.util.ArrayList;

import org.w3c.dom.Element;

public class TestCase<T>{
	
		T input, output;
		public TestCase(T input, T output){
			assert (input != null && output != null);
			this.input = input;
			this.output = output;
		}
		public T getInput() {
			return input;
		}
		public T getOutput() {
			return output;
		}
}
