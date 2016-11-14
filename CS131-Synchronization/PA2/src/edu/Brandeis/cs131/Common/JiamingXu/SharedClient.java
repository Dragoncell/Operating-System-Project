package edu.Brandeis.cs131.Common.JiamingXu;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class SharedClient extends MyClient {
	
  public SharedClient ( String name, Industry industry,int speed, int request_level) {
	    super (name, industry, speed, request_level);
  }
	    
  public String toString() {
	    return String.format("%s SHARED %s", this.getIndustry(), this.getName());
  }
  
}
