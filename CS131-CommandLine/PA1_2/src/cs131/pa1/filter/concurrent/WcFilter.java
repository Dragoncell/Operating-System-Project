package cs131.pa1.filter.concurrent;

public class WcFilter extends ConcurrentFilter {
	private int linecount;
	private int wordcount;
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {			
		while (!isDone()){
			while (!input.isEmpty()) {
				String line = input.poll();
				if (line != null) {
					String processedLine = processLine(line);
				}
			}
		}	
		output.add(linecount + " " + wordcount + " " + charcount);
	}
	
	public String processLine(String line) {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;	
	}

}
