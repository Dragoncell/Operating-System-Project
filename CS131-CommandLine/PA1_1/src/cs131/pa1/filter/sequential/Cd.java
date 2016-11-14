package cs131.pa1.filter.sequential;

import java.io.*;

public class Cd extends SequentialFilter {
	String newWorkingDirectory;
	
	
	public Cd (String[] path) throws IOException, RequiresParameterException, InvalidParameterException {
		
		if (path.length == 1) {
			throw new RequiresParameterException();
		}
		
		if (path.length > 2) {
			throw new InvalidParameterException();
		}

		String currentWorkingDirectory = SequentialREPL.currentWorkingDirectory;
		File newPath = new File(path[1]);
		if (newPath.isAbsolute()){
			newWorkingDirectory = newPath.getCanonicalPath();
		} else {
			newWorkingDirectory = new File(currentWorkingDirectory+FILE_SEPARATOR+newPath.getPath()).getCanonicalPath();
		}	
		
		File f = new File (newWorkingDirectory);
		if (!f.exists() || !f.isDirectory()){
			throw new IOException();
		}
	}
	@Override
	public void process(){
		SequentialREPL.currentWorkingDirectory = newWorkingDirectory;
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}
}