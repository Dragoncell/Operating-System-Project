package cs131.pa1.filter.sequential;

public class Pwd extends SequentialFilter {
	@Override
	public void process(){
		this.output.add(SequentialREPL.currentWorkingDirectory);
	}
	@Override
	protected String processLine(String line) {
		return null;
	}
}