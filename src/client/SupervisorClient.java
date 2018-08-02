package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SupervisorClient extends Thread
{
    String type = "Supervisor";
    String serverIP;
    int hostPort;
    int clientID;

    SupervisorClient(String ip, int port)
    {
        serverIP = ip;
        hostPort = port;
        clientID = 1;
        setName("SupervisorClient-" + clientID);
    }

    void msg(String s)
    {
        System.out.println(s);
    }


    public void run ()
    {
        try
        {
            // connect to server
            // create input and output streams
            msg(getName() + " started");
            Socket soc = new Socket(serverIP, hostPort);
            PrintWriter pw = new PrintWriter(soc.getOutputStream());
            BufferedReader brf = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            pw.println(type);
            pw.println(clientID);
            pw.println(0);
            pw.println(1);

            msg(getName() + " sending message to SeverHelper: " +  "signalDeparture"  + "goHome");

            pw.flush();

            String line;
            while ((line = brf.readLine()) == null)
            {
                // busy wait
            }

            msg(line);

            soc.close();

        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
