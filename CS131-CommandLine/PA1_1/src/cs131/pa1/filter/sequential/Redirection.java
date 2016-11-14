package cs131.pa1.filter.sequential;

import java.io.*;
import cs131.pa1.filter.Filter;

public class Redirection extends SequentialFilter {
	private PrintStream printWriter;
	String name;
	Boolean h = true;
	public Redirection(String outputFileName) throws FileNotFoundException {	
			  this.name = outputFileName;
			  createFile();
	}
	
	public void createFile() throws FileNotFoundException{
		 try {
		  String cwd = SequentialREPL.currentWorkingDirectory;
		  File f = new File(cwd + Filter.FILE_SEPARATOR + this.name);
		  printWriter = new PrintStream(f);
		 } catch (FileNotFoundException e) {
			 
		 }
	}

	@Override
	protected String processLine(String line){
		if (!line.equals(this.name)) {
			printWriter.println(line);
		}
		isDone();
		return null;
	}
	
	public boolean isDone(){
		if (input.isEmpty()){
			printWriter.close();
			return true;
		}
		return false;
	}
}