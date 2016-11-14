Part 1

1.there are two kinds of client(Basic and Shared) which inherits from Myclient class. in the BasicClient 
and SharedClient class, they have specific to_string method, which returns "Type Industry Name"
2.in the concreteFactory, it can generate basic clients, shared clients, basic server and master server.
and for the clients, the initial speed is random from 0-9, the request_level is 3. For other parameters, it
receives from the test. 
3.for the BasicServer, it has two synchronized method connectInner and disconnectInner, which only allows one thread 
to enter them, avoid the race condition. 
 In the basic server, I use an arraylist clientRun to record the current running client
 In the method of connectInner, if there is none client in it, the add the client and return true to show this client
 can use this server. if there are two clients, then return false, the cilent can't use the server. if there is one client
 in the clientRun, according to rules of basic server, if we want to add another client to the server, the situation is that
 two client are both shared clients and their industry are not same.
 So in the ClientRun can be : 
   none, 
   one basic client
   one shared client
   two shared clients which are not in the same industry
 In the method of disconnectInner, just remove the client from the clientRun list
 
 Part 2
  1.I implement ReentrantLock and Condition Variables to MasterServer class. For each server, they have their own lock and 
  condition variable which stores in HashMap, because different servers are independent, thus avoid  informing 
  clients waiting to gain access to one server about events pertaining to another server 
  
  2. In the connectInner method, the serverQueue is the shared variable, so it should include in critical section. First, i 
  add the client to the queue and waits until the cilent is the first element in the queue, then waits until it can connect the server, 
  and then remove it from the queue and signalall clients waiting for this server.
  
  3. In the disconnectInner method, it is very simple, just disconnet the server and signalall clients waiting for this server 
   
 Attesting: 
 the submitted homework is finished by my own. 
 
 
                            