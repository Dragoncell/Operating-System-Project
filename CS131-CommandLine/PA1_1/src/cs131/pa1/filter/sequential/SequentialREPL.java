package cs131.pa1.filter.sequential;

import java.util.List;
import java.util.Scanner;
import cs131.pa1.filter.Message;
import cs131.pa1.filter.sequential.SequentialFilter;
import cs131.pa1.filter.sequential.SequentialCommandBuilder;


public class SequentialREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		System.out.print(Message.WELCOME);
        Scanner console = new Scanner(System.in);  
        while (true) {
        	System.out.print(Message.NEWCOMMAND);
        	String command = console.nextLine();
        	if (command.equals("exit")) {
             	System.out.print(Message.GOODBYE);
        		console.close();
        		break;
        	} else {
        		List<SequentialFilter> filters = SequentialCommandBuilder.createFiltersFromCommand(command);
        		goToFilters(filters);    		    
        	}
        }
	}
    public static void goToFilters(List<SequentialFilter> filters){
       	for (SequentialFilter filter : filters){
       		filter.process();
       	}
    }
	
}
