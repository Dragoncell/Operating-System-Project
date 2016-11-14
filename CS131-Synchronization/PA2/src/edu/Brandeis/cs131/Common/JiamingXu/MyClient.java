package edu.Brandeis.cs131.Common.JiamingXu;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Industry;


public abstract class MyClient extends Client {

    public MyClient(String name, Industry industry, int speed, int request_level) {
        super(name , industry , speed , request_level);
    }
}
