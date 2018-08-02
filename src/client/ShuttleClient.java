package client;

import java.net.*;
import java.io.*;

public class ShuttleClient extends Thread
{
    String type = "Shuttle";
    String serverIP;
    int hostPort;
    int clientID;


    ShuttleClient(String IP, int port, int id)
    {
        serverIP = IP;
        hostPort = port;
        clientID = id;
        setName("ShuttleClient-" + clientID);
    }

    void msg(String s)
    {
        System.out.println(s);
    }

    public void run()
    {
        try
        {
            msg(getName() + " started");
            Socket soc = new Socket(serverIP, hostPort);
            PrintWriter pw = new PrintWriter(soc.getOutputStream());
            BufferedReader brf = new BufferedReader(new InputStreamReader(soc.getInputStream()));

            pw.println(type);
            pw.println(clientID);
            pw.println(1);
            pw.println(2);
            pw.println(3);

            msg(getName() + " sending message to SeverHelper: " + "recharge" + " " + "moveToDock" + " " + "takeOff");

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
