package edu.Brandeis.cs131.Common.JiamingXu;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Server;
import java.util.ArrayList;

public class BasicServer extends Server {
    private ArrayList<Client> clientRun = new ArrayList<Client>();
    
	public BasicServer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public synchronized boolean connectInner(Client client) {
		if (clientRun.size() == 0) {
		 clientRun.add(client);
		 return true;
		} else if (clientRun.size() == 1) {
			Client client1 = clientRun.get(0);
			
			if ((client instanceof SharedClient) && (client1 instanceof SharedClient) && (client.getIndustry() != client1.getIndustry())) {
				clientRun.add(client);
				return true;
			}
		} 
		return false;
	}

	@Override
	public synchronized void disconnectInner(Client client) {
		clientRun.remove(client);
		// TODO Auto-generated method stub
	}
}
