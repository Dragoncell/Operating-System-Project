package cs131.pa1.filter.sequential;

import java.util.*;
import java.io.*;
import cs131.pa1.filter.Message;

public class SequentialCommandBuilder {
	
	private final static Set<String> PIPES_IN = new HashSet<String>(Arrays.asList(new String[] {"Grep","Wc","PrintFilter","Redirection"}));
	private final static Set<String> PIPES_OUT = new HashSet<String>(Arrays.asList(new String[] {"Head","Ls","Pwd","Grep","Wc"}));
	private final static Set<String> PIPE_IN = new HashSet<String>(Arrays.asList(new String[] {"grep","wc"}));		
	
	public static List<SequentialFilter> createFiltersFromCommand(String command){
		
		List<SequentialFilter> listFilter = new ArrayList<SequentialFilter>(); //store filters 
		SequentialFilter finalFilter = determineFinalFilter(command);
		String commandNew = adjustCommandToRemoveFinalFilter(command);
		if (commandNew != "") {
		List<String> subCommands = Arrays.asList(commandNew.split("\\|"));
		for (String subCommand : subCommands){
			SequentialFilter currentFilter = constructFilterFromSubCommand(subCommand.trim());	
			listFilter.add(currentFilter);
		}
		listFilter.add(finalFilter);
		int i = 0; 
		for (String subCommand : subCommands){
		  SequentialFilter currentFilter = listFilter.get(i);
		  SequentialFilter preFilter = (i != 0)? listFilter.get(i-1) : null;
		  SequentialFilter nextFilter = (i < listFilter.size()-2 )? listFilter.get(i+1) : null;
		  boolean subCommandError = (currentFilter instanceof PrintFilter) && ((PrintFilter) currentFilter).isStandardError();
		  if (subCommandError) {
			listFilter.clear();
			listFilter.add(currentFilter);
			return listFilter;
		  }
		  SequentialFilter invalidSequential = isInvalid(preFilter, currentFilter, nextFilter, subCommand);
		  if (invalidSequential != null) {
			listFilter.clear();
			listFilter.add(invalidSequential);
			return listFilter;
		  }
		  i++;
		}
		
		boolean subCommandError = (finalFilter instanceof PrintFilter) && ((PrintFilter) finalFilter).isStandardError();
		  if (subCommandError) {
			listFilter.clear();
			listFilter.add(finalFilter);
			return listFilter;
		  }
		  
		 if (finalFilter.getClass().getName() == "Redirection" && (!PIPES_OUT.contains(listFilter.get(i-1).getClass().getName()))) {
			listFilter.clear();
			listFilter.add(new PrintFilter(Message.REQUIRES_INPUT.with_parameter(command.substring(command.lastIndexOf(">")))));
			return listFilter;
		  }
		  
		linkFilters(listFilter);
		return listFilter;
		} else {
			listFilter.add(new PrintFilter(Message.REQUIRES_INPUT.with_parameter(command.substring(command.lastIndexOf(">")))));
			return listFilter;
		}
	}
	
	private static SequentialFilter isInvalid(SequentialFilter x, SequentialFilter y, SequentialFilter z, String subCommand) {
		boolean providesOutputx = (x!=null) && PIPES_OUT.contains(x.getClass().getSimpleName());
		boolean requiresInputy = PIPES_IN.contains(y.getClass().getSimpleName());
		boolean providesOutputy = PIPES_OUT.contains(y.getClass().getSimpleName());
		boolean requiresInputz = (z!=null) && PIPES_IN.contains(z.getClass().getSimpleName());
		if (providesOutputx && !requiresInputy) {
			return new PrintFilter(Message.CANNOT_HAVE_INPUT.with_parameter(subCommand));
		} else if (!providesOutputx && requiresInputy) {
			return new PrintFilter(Message.REQUIRES_INPUT.with_parameter(subCommand));
		} else if (!providesOutputy && requiresInputz) {
			return new PrintFilter(Message.CANNOT_HAVE_OUTPUT.with_parameter(subCommand));
		}
		return null;
		
	}
	private static SequentialFilter determineFinalFilter(String command){
		if (command.contains(">")){
			if (command.lastIndexOf(">")+1 >=command.length() ) {
				return new PrintFilter(Message.REQUIRES_PARAMETER.with_parameter(">"));	
			} else {
			String commandn = command.substring(command.lastIndexOf(">"));
			if (commandn.contains("|")) {
			  String commandw = commandn.substring(commandn.indexOf("|")+1).trim(); 
			  
			  String[] words = commandw.split(" ");
		
			  if (PIPE_IN.contains(words[0])) {
				
				  return new PrintFilter(Message.CANNOT_HAVE_OUTPUT.with_parameter(command.substring(command.lastIndexOf(">"),command.lastIndexOf("|"))));
			  }
			} 
			String filename = command.substring(command.lastIndexOf(">")+1).trim();
			try {
				return new Redirection(filename);
			}  catch (FileNotFoundException e) {
				
		   	   } 
			}
		}
		return new PrintFilter();
	}
	
	private static String adjustCommandToRemoveFinalFilter(String command){
		if (command.contains(">")){
			if (command.lastIndexOf(">")-1 >=0) {
				return command.substring(0,command.lastIndexOf(">")-1);
			} else return "";
		}
		else {
			return command;
		}
	}
	
	private static SequentialFilter constructFilterFromSubCommand(String subCommand){
		String[] words =subCommand.split(" ");
		switch (words[0].toLowerCase()){
		case "pwd":
			return new Pwd();
		case "ls":
			return new Ls();
		case "cd":
			try {
				return new Cd(words);
			} catch (IOException e) {
				return new PrintFilter(Message.DIRECTORY_NOT_FOUND.with_parameter(subCommand));		
			} catch (RequiresParameterException e) {
			    return new PrintFilter(Message.REQUIRES_PARAMETER.with_parameter(subCommand));
			} catch (InvalidParameterException e) {
				return new PrintFilter(Message.INVALID_PARAMETER.with_parameter(subCommand));
			} 
		case "head":
			try {
				return new Head(words);
			} catch (FileNotFoundException e) {
				return new PrintFilter(Message.FILE_NOT_FOUND.with_parameter(subCommand));		
			} catch (RequiresParameterException e) {
			    return new PrintFilter(Message.REQUIRES_PARAMETER.with_parameter(subCommand));
			} catch (InvalidParameterException e) {
				return new PrintFilter(Message.INVALID_PARAMETER.with_parameter(subCommand));
			}
		case "grep":
			try {
			    return new Grep(words);
			} catch (RequiresParameterException e) {
			    return new PrintFilter(Message.REQUIRES_PARAMETER.with_parameter(subCommand));
			} catch (InvalidParameterException e) {
				return new PrintFilter(Message.INVALID_PARAMETER.with_parameter(subCommand));
			}
			
		case "wc":
			return new Wc();
		default:
		  return new PrintFilter(Message.COMMAND_NOT_FOUND.with_parameter(subCommand));
		}
	}

	private static void linkFilters(List<SequentialFilter> filters){
		for (int i = 1; i <= filters.size() - 1; i++){
			filters.get(i).setPrevFilter(filters.get(i-1));
		}	
	}
}
