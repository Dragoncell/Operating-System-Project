package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	private String job;
	
	public PrintFilter() {
		
	}
	
	public void process() {
		while(!isDone()) {
		  while (!input.isEmpty()) {
			  processLine(input.poll());
			} 
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}
	@Override
	public void run() {
		  // job = ConcurrentREPL.jobs.get(ConcurrentREPL.jobs.size()-1);
		   this.process();   
		   ConcurrentREPL.jobs.remove(0);
		   this.setstatus(false);
		   if (ConcurrentREPL.jobs.size() <= 0) ConcurrentREPL.task = "true";
	}
}
