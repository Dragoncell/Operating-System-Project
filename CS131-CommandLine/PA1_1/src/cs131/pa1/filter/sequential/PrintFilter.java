package cs131.pa1.filter.sequential;

import java.util.LinkedList;


public class PrintFilter extends SequentialFilter {
	
	private boolean standardError = false;
	
	public PrintFilter() {
	}
	
	public PrintFilter(String errorMessage) {
		standardError = true;
		input = new LinkedList<String>();
		input.add(errorMessage);
	}
	
	public boolean isStandardError() {
		return standardError;
	}
	
	@Override
	protected String processLine(String line) {
		if (standardError) {
			System.out.print(line);
		} else {
			System.out.println(line);
		}
		return null;
	}	
}
