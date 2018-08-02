////    On the client side, you will create the different types of client threads (shuttles, coordinator, supervisor)
////    that will execute concurrently. These clients will ask the main server’s thread to establish a connection.
//
//
//
//
//    Thread ClientHelper = new Thread()
//    {
////        The client will ask the corresponding “client helper” thread to execute sequentially the methods
////        that were implemented in Project 1 as part of the run method. Before each method can be executed,
////        it will send the server a message containing its name and the method name/number to be executed.
////        This can be implemented in different ways. One way (but not the only way) would be to use a
////        switch-case structure. This is similar to the process of creating stubs in the client and server sites.
//    };

package client;
import java.util.Vector;

public class Client extends Thread
{
    String serverIP;
    int hostPort;
    int numShuttles;
    int numRecharge;

    public Vector<ShuttleClient> shuttleClients = new Vector<>();

    Client(String ip, int port, int shuttles)
    {
        serverIP = ip;
        hostPort = port;
        numShuttles = shuttles;
        numRecharge = 3;
    }

    public void run()
    {
        for (int i = 0; i < numShuttles; i++)
        {
            shuttleClients.add(new ShuttleClient(serverIP, hostPort, i + 1));
            shuttleClients.get(i).start();
        }

        ControllerClient controller = new ControllerClient(serverIP, hostPort);
        controller.start();

        SupervisorClient supervisor = new SupervisorClient(serverIP , hostPort);
        supervisor.start();
    }

    public static void main(String[] args)
    {
        if (args.length != 3)
        {
            System.out.println("ip_address, host_port, and num_shuttles needed to start client. Try again...");
            System.exit(1);
        }

        String ip = args[0];
        int port = Integer.valueOf(args[1]);
        int shuttles = Integer.valueOf(args[2]);

        Client cli = new Client(ip, port, shuttles);
        cli.start();
    }
}
