
//    The server will be multithreaded. The main server’s thread takes care of the connection requests
//        (establish rendezvous). The spawned server-threads will carry out the extended rendezvous with the clients.
//    Most of the (already) implemented code will be on the server site.

//        .
//        .
//        .
//    When the connection is accepted by the server, the main server will create another “client helper” thread that
//    will carry out the two-way communication with the client thread.

package server;

import java.io.IOException;
import java.net.*;

public class Server extends Thread
{
    int connectionCount;


    public Server (int port)
    {
        connectionCount = 0;

        try
        {
            msg("Server is starting... ");
            ServerSocket ss = new ServerSocket(port);

            while (true)
            {
                // code for having it listen to a dedicated port for incoming connections
                //spawn a helper,in here is named SubServerThread

                msg("waiting for incoming connections...");
                Socket connection = ss.accept();

                msg("Client connected successfully!");
                connectionCount++;

                SubServerThread helper = new SubServerThread(connection);
                helper.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Unable to listen to port.");
            e.printStackTrace();
        }
    }

    void msg(String s)
    {
        System.out.println(s);
    }

    public static void main (String [] args)
    {
        if(args.length != 1)
        {
            System.out.println("host_port needed to start server. Try again...");
            System.exit(1);
        }

        new Server(Integer.parseInt(args[0]));

        System.exit(0);
    }
}
