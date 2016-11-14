package cs131.pa1.filter.sequential;

public class Grep extends SequentialFilter {
	
	private String searchWords;
	
	public Grep(String[] pattern) throws RequiresParameterException, InvalidParameterException{
		
		if (pattern.length == 1) {
			throw new RequiresParameterException();
		}	
		if (pattern.length >2 ) {
			throw new InvalidParameterException();
		}	
		this.searchWords = pattern[1];
	}
	
	@Override
	protected String processLine(String line) {
		if (line.contains(searchWords)){
			return line;
		}
		return null;
	}
}