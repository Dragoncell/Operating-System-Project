package cs131.pa1.filter.concurrent;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import cs131.pa1.filter.Message;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	static String task; //task is used to show if there is a thread running. it is "true" that we can print '>' prompt
    static List<String> jobs; //jobs is used to record the running jobs for command repl_jobs
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		jobs = new ArrayList<String>(); //jobs initializes to a new Arraylist 
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
        boolean isContainsAnd = false; //used to show if "&" is contained in the end of command
		task = "true"; // task is "true",there is no running thread right now.
		while(true) {
			if (task.equals("true")) { //if all thread is completed or the former command accepts new command
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			isContainsAnd = false; //suppose the command is not contained "&"
			if(command.equals("exit")) {
				break;
			} if (command.equals("repl_jobs")) {  //show the current running command 
				  int i = jobs.size();
			      for (int j =1;j<=i;j++) {
				  System.out.println("\t"+j+". "+jobs.get(j-1)); 
			  }
				
			} else if(!command.trim().equals("")) {
				String newcommand = command; //deal with the command endswith "&"
				if (command.endsWith("&")) {
				  newcommand = command.substring(0, command.length()-1);
				  isContainsAnd = true;
				}
				//building the filters list from the command
				List<ConcurrentFilter> filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(newcommand);
					
				//checking to see if construction was successful. If not, prompt user for another command
				if(filterlist != null) {
					//we add the command to jobs, if there was error, before we add the command to job, it will throw an exception.
					//run each filter process manually.
					if (!isContainsAnd) {
						task = "false"; //thread is not completed yet
						jobs.clear();
					} //task is true , if it is contained "&"
					jobs.add(command);
					Iterator<ConcurrentFilter> iter = filterlist.iterator();
					while(iter.hasNext()){ //create thread for every filter
						ConcurrentFilter filter = iter.next();
						Thread T = new Thread(filter);
						filter.setstatus(true);	//set status to true show the thread is not done.	
						T.start(); // start the thread of filter; 					
					}
				}
			}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
