package edu.Brandeis.cs131.Common.JiamingXu;

import edu.Brandeis.cs131.Common.Abstract.Industry;

public class BasicClient extends MyClient {
		
    public BasicClient (String name, Industry industry, int speed, int request_level) {
      super (name, industry,speed,request_level);
    }
    
	public String toString() {
        return String.format("%s BASIC %s", this.getIndustry(), this.getName());
    }
	
}

