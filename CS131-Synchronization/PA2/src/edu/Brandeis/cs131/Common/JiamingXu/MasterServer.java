package edu.Brandeis.cs131.Common.JiamingXu;

import java.util.HashMap;
import java.util.LinkedList;

import edu.Brandeis.cs131.Common.Abstract.Client;
import edu.Brandeis.cs131.Common.Abstract.Log.Log;
import edu.Brandeis.cs131.Common.Abstract.Server;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MasterServer extends Server {

    private final Map<Integer, List<Client>> mapQueues = new HashMap<Integer, List<Client>>();
    private final Map<Integer, Server> mapServers = new HashMap<Integer, Server>();

    private final Map<Integer,Lock> locks  = new HashMap<Integer,Lock>();
    private final Map<Integer,Condition> connects  = new HashMap<Integer,Condition>();
   
   
    public MasterServer(String name, Collection<Server> servers, Log log) {
        super(name, log);
        Iterator<Server> iter = servers.iterator();
        while (iter.hasNext()) {
            this.addServer(iter.next());
        }
    }

    public void addServer(Server server) {
        int location = mapQueues.size();
        this.mapServers.put(location, server);
        this.locks.put(location, new ReentrantLock());
        this.connects.put(location, locks.get(location).newCondition());
        this.mapQueues.put(location, new LinkedList<Client>());
    }

    @Override
    public boolean connectInner(Client client) {
    	int serverKey = this.getKey(client);
    	List<Client> serverQueue = this.mapQueues.get(serverKey);
    	Server server = this.mapServers.get(serverKey);
    	Lock lock = this.locks.get(serverKey);
    	Condition connect = this.connects.get(serverKey);
    	lock.lock();
         try {
          serverQueue.add(client);
          while (serverQueue.indexOf(client) != 0 ) {
        	try {
        		connect.await();  
        	} catch (InterruptedException e) {	
        	}
          }
          while (!server.connect(client)) {
        	try {
          		connect.await();  
          	} catch (InterruptedException e) {	
          	} 
          }
          serverQueue.remove(client);
          connect.signalAll();
          return true;
         } finally {
           lock.unlock();
         }
    }

    @Override
    public void disconnectInner(Client client) {
    	int serverKey = this.getKey(client);
    	Server server = this.mapServers.get(serverKey);
    	Lock lock = this.locks.get(serverKey);
    	Condition connect = this.connects.get(serverKey);
        lock.lock();
        try {
         server.disconnect(client);
         connect.signalAll();
        } finally {
        	lock.unlock();
        }
    }

	//returns a number from 0- mapServers.size -1
    // MUST be used when calling get() on mapServers or mapQueues
    private int getKey(Client client) {
        return client.getSpeed() % mapServers.size();
    }
}
