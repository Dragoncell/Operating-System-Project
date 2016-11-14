package cs131.pa1.filter.sequential;

import java.io.*;
import java.util.*;

public class Head extends SequentialFilter {

	private Scanner scanners;
	private int scannerNumber = 0;
	private int readLine = 10;

	
	public Head(String[] words) throws FileNotFoundException, RequiresParameterException, InvalidParameterException{
		
		if (words.length == 1 || (words.length == 2 && words[1].indexOf("-") == 0)) {
			throw new RequiresParameterException();
		}
		if (words.length >3) {
	       throw new InvalidParameterException();
		} else {
		  scanners = new Scanner(new File(words[words.length-1]));
		  if (words[1].contains("-") && (words.length == 3)) {
			 try {
				 readLine = Integer.parseInt(words[1].substring(1));
			 } catch (NumberFormatException e) {
				 throw new InvalidParameterException();
			 }
		  }
		}
	}

	@Override
	public void process(){
		while (!isDone()){	
			if (scanners.hasNextLine()) {
			output.add(scanners.nextLine());
			}
			scannerNumber++;
		}
	}
	
	@Override
	protected String processLine(String line) {
		return null;
	}

	@Override
	public boolean isDone() {
		return readLine <= scannerNumber;
	}
}