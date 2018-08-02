package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Vector;

public class SubServerThread extends Thread {
    private static final String SHUTTLE = "Shuttle";
    private static final String CONTROLLER = "Controller";
    private static final String SUPERVIRSOR = "Supervisor";
    private static Object lock = new Object();
    public static ControllerServer controller;
    public static SupervisorServer supervisor;
    public static ShuttleServer shuttle;

    public static int numShuttles = 0;
    private Socket incoming;
    private BufferedReader br;
    private PrintWriter pw;
    private ShuttleServer student;
    private String threadtype;

    static int threadCount = 0;
    int id, methodNumber;

    static Vector<ShuttleServer> shuttles = new Vector<>();


    //Socket incoming;

    SubServerThread(Socket soc)
    {
        incoming = soc;
        threadtype = "";
        setName("HelperThread-");
    }

    void msg(String s) {
        System.out.println(s);
    }

    public void run() {
        // get input stream, get output stream, other code

        synchronized (lock) {
            threadCount++;
            id = threadCount;
        }

        try {
            msg(getName() + id + " started");

            BufferedReader brf = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter pw = new PrintWriter(incoming.getOutputStream());

            while ((threadtype = brf.readLine()) == null)
            {
                // busy wait
            }

            if (threadtype.equals("Shuttle"))
            {
                id = Integer.parseInt(brf.readLine());

                System.out.println("ServerHelper-" + id + ": reading " + threadtype + " method: " + methodNumber);
                pw.println("ServerHelper-" + id + ": received message from " + threadtype + "-" + id);
                pw.flush();

                ShuttleServer shuttle = new ShuttleServer(threadCount);
                System.out.println("ServerHelper-" + id + ": creating Server" + threadtype + "-" + id);
                shuttles.add(shuttle);

                for (int i = 0; i < 3; i++)
                {
                    methodNumber = Integer.parseInt(brf.readLine());
                    System.out.println("ServerHelper-" + id + ": reading " + methodNumber + " from ShuttleClient-" + threadCount);
                    runMethod(i);
                }
            }
            else if (threadtype.equals("Controller"))
            {
                id = Integer.parseInt(brf.readLine());

                System.out.println("ServerHelper-" + id + ": reading " + threadtype + " method: " + methodNumber);
                pw.println("ServerHelper-" + id + ": received message from " + threadtype + "-" + id);
                pw.flush();

                controller = new ControllerServer(threadCount);
                System.out.println("ServerHelper-" + id + ": creating Server" + threadtype + "-" + id);
                for (int i = 0; i < 1; i++)
                {
                    methodNumber = Integer.parseInt(brf.readLine());
                    System.out.println("ServerHelper-" + id + ": reading " + methodNumber + " from ShuttleClient-" + threadCount);
                    runMethod(i);
                }

            }
            else if (threadtype.equals("Supervisor"))
            {
                id = Integer.parseInt(brf.readLine());

                System.out.println("ServerHelper-" + id + ": reading " + threadtype + " method: " + methodNumber);
                pw.println("ServerHelper-" + id + ": received message from " + threadtype + "-" + id);
                pw.flush();

                supervisor = new SupervisorServer(threadCount);
                System.out.println("ServerHelper-" + id + ": creating Server" + threadtype + "-" + id);
                for (int i = 0; i < 1; i++)
                {
                    methodNumber = Integer.parseInt(brf.readLine());
                    System.out.println("ServerHelper-" + id + ": reading " + methodNumber + " from ShuttleClient-" + threadCount);
                    runMethod(i);
                }

            }

            msg("ServerHelper-" + id + " is done executing, will terminate.");

            incoming.close();

        } catch (IOException e) {

        }
    }


    public void runMethod(int methodNumber)
    {
        if (threadtype.equals(SHUTTLE))
        {
            ShuttleServer shuttle = new ShuttleServer(this.id);
            msg("Shuttle-"  + " running method number " + methodNumber);
            switch (methodNumber) {
                case 0:
                    shuttle.recharge();
                    break;
                case 1:
                    shuttle.moveToDock();
                    break;
                case 2:
                    shuttle.takeOff();
                    break;
                default:
                    break;
            }
        } else if (threadtype.equals(SUPERVIRSOR))
        {
            SupervisorServer supervisor = new SupervisorServer(1);

            switch (methodNumber) {
                case 0:
                    supervisor.signalDeparture();
                    break;
                case 1:
                    supervisor.goHome();
                    break;
                default:
                    break;
            }
        } else if (threadtype.equals(CONTROLLER))
        {
            ControllerServer controller = new ControllerServer(1);

            switch (methodNumber) {
                case 0:
                    controller.openChargingStation();
                    break;
                case 1:
                    controller.goHome();
                    break;
                default:
                    break;

            }
        }
    }
};
