package cs131.pa1.filter.sequential;

import java.io.File;

public class Ls extends SequentialFilter {
	@Override
	public void process() {
		File dir = new File(SequentialREPL.currentWorkingDirectory);
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
		        this.output.add(file.getName());
		    }
	}
	@Override
	protected String processLine(String line) {
		return null;
	}
}