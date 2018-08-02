package server;

import java.util.*;
import java.util.concurrent.*;

public class ShuttleServer extends Thread
{
    public static long time = System.currentTimeMillis();
    int amountOfFuel = 0;
    int tripsTaken = 0;

    SupervisorServer supervisor;
    StationServer station;
    ControllerServer controller;


    public ShuttleServer()
    {
        station = new StationServer();
        supervisor = new SupervisorServer();
    }

    public ShuttleServer(int id)
    {
        setName("Shuttle-" + id);
        station = new StationServer();
        supervisor = new SupervisorServer();

    }

    public void msg(String m)
    {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName() + ": "+m);
    }

    public void sleep(int seconds)
    {
        try
        {
            if(seconds <= 0)
            {
                Random rand = new Random();
                int randomSleepTime = rand.nextInt((5 * 1000) + 1);
                msg("Sleeping for " + randomSleepTime);
                Thread.sleep(randomSleepTime);
            }
            else
            {
                int time = seconds * 1000;
                msg("Sleeping for " + time);
                Thread.sleep(time);
            }

        }

        catch (InterruptedException e)
        {
            msg(e + "");
        }
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

    public void recharge()
    {
        amountOfFuel = 0;
        msg("is moving to recharge station");
        sleep(0);

        //Shuttles are waiting in groups of size numRecharge for the controller to allow them to proceed
        // (use a different notification object for each group)
        station.waitingToRecharge++;
        station.waitingShuttles.add(this);

//        synchronized (station.recharge)
//        {
//            station.recharge.notify();
//            wait(station.recharge);
//        }

        msg("is in recharge zone");

        Random rand = new Random();
        int randomRefuel = ThreadLocalRandom.current().nextInt(50, 100 + 1);

        // if not enough fuel in the reservoir, signal the controller to refill and wait
        if(randomRefuel > station.getCurrentReservoir())
        {
            // signal the controller

            synchronized (controller.controllerObj)
            {
                station.waitingOnRersvoir = 1;
                controller.resovoirObj.notify();
                //wait(controller.controllerObj);
            }
        }

        station.getFuel(randomRefuel);
        //msg("Station Reservoir Amount: " + station.getCurrentReservoir());
        this.amountOfFuel = amountOfFuel + randomRefuel;

        msg("filled up by " + randomRefuel + ". New amount of fuel is: " + amountOfFuel);
    }

    public void moveToDock()
    {
        msg("is moving to the dock zone");
        sleep(0);
    }

    public void takeOff()
    {
        msg("is waiting to take off");
        // wait to be signaled by supervisor
        Object waitObj= new Object();


        supervisor.waiting++;
        synchronized(station.takeOff)
        {

           // wait(station.takeOff);

        }
        msg("is taking off");
        tripsTaken++;
    }

    public void run()
    {
        while(station.openOrClose && tripsTaken <= 3)
        {
            // As an initial state, all shuttles are in the air, cruising (simulated by sleep of random time)
            sleep(0);

            // land & recharge
            recharge();

            //move to landing/take-off zone and wait for next take off
            moveToDock();

            // take off into space and repeat
            takeOff();
        }
        msg("completed three trips");

    }

}
