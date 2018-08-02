package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ControllerClient extends Thread
{
    String type = "Controller";
    String serverIP;
    int hostPort;
    int clientID;

    ControllerClient(String ip, int port)
    {
        serverIP = ip;
        hostPort = port;
        clientID = 1;
        setName("ControllerClient-" + clientID);
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
            pw.println(0);
            pw.println(1);

            msg(getName() + " sending message to SeverHelper: " + "openChargingStation" + "goHome");

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
