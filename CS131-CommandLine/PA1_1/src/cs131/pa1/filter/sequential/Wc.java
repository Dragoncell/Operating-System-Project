package cs131.pa1.filter.sequential;

public class Wc extends SequentialFilter {
	
	int nLines;  int nWords;  int nChars;
	
	public Wc(){
	}
	@Override
	public void process(){
		if (input.isEmpty()) {
			output.add("0 0 0");
		} else {
			while (!input.isEmpty()){
				String line = input.poll();
				String processedLine = processLine(line);
				if (processedLine != null){
					output.add(processedLine);
				}
			}	
		}
		
	}
	@Override
	protected String processLine(String line) {
		nLines =nLines + 1;
		for (String s : line.split(" ")){
			if (s.trim().length() > 0){
				nWords =nWords + 1;
			}
		}
		nChars =nChars + line.length();
		if (this.isDone()) {
			return nLines + " " + nWords + " " + nChars;
	    } else {
			return null;
		}
	}
}