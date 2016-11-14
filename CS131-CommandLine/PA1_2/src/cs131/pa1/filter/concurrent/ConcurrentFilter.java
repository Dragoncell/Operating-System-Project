package cs131.pa1.filter.concurrent;
//import java.util.LinkedList;
//import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import cs131.pa1.filter.Filter;


public abstract class ConcurrentFilter extends Filter implements Runnable {
	
	protected LinkedBlockingQueue<String> input;
	protected LinkedBlockingQueue<String> output;
	private boolean status; //used to record whether the thread of this filter is done. it is false if the thread is done! 
	
	public boolean getstatus(){
		return status;
	}
	public void setstatus(boolean status) {
		this.status = status;
	}
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this.output = new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	public void process(){
		while (!isDone()){ //the thread is not done!
			while (!input.isEmpty()) { // the input is not empty
				String line = input.poll();
				String processedLine = processLine(line);
				if (processedLine != null){
					output.add(processedLine);
				}
			} 
		}	
	}
	
	@Override
	public boolean isDone() { //it is done when the input is empty and the prevfilter is done.
		ConcurrentFilter prevFilter = (ConcurrentFilter) prev;
		if (prevFilter == null) {
			return !this.getstatus();
		} else {
		return (input.size() == 0) && (!prevFilter.getstatus());
		}
	}
	@Override
	public void run() {
		this.process(); 
		this.setstatus(false); //the thread is done! 
	}
	protected abstract String processLine(String line);
	
}
