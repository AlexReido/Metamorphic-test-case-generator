README 
The tool is split up into two parts, the test case generator and the execution of those test cases.

Test case generator: run the MetaTestOrchestrator found in the command package with a single argument. 
This argument is the filepath of the test desccriptor file. The supplied test descriptors are:
	"src/xml/sineTestDescriptor.xml"
	"src/xml/sortTestDescriptor.xml"
	"src/xml/graphTestDescriptor.xml"

These files can be changed to:
 	Describe different relations
	Change number of test cases to be generated
	Change the source test case
	Change the program under test
Running the MetaTestOrchestrator will create/ replace the file "src/xml/TestCases.xml" although the 
file written to can be changed on line 229 of MetaTestOrchestrator


Now the Execution of the test cases can be carried out by running the class Execution in the execution package. 
IMPORTANT remember to enable assertions in the java vm using the flag -ea. This will read from the file "src/xml/TestCases.xml"
but this can be changed on line 84 of Execution if needed. The results will be output to the console, the output
of the program under test is displayed by default. 
