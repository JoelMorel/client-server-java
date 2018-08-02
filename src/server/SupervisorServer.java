package server;

import java.util.*;

public class SupervisorServer extends Thread
{
    public static long time = System.currentTimeMillis();

    int waiting = 0;

    ShuttleServer ship;
    StationServer station;

    Object takeOff = new Object();


    // Default constructor
    public SupervisorServer()
    {}

    public SupervisorServer(int id)
    {
        setName("Supervisor-" + id);
    }

    public void msg(String m)
    {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName() + ": "+m);
    }

    public void wait(Object obj)
    {
        try
        {
            obj.wait();
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void signalDeparture()
    {

    }

    public void goHome()
    {
        msg("went home");
    }


    public void run()
    {
//        synchronized (station.stationObj)
//        {
//            wait(station.stationObj);
//        }


        while (station.openOrClose)
        {
            synchronized (station.takeOff)
            {

                station.takeOff.notifyAll();
                //waiting--;
            }

        }

        goHome();
    }
}
