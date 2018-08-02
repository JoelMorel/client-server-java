package server;


public class ControllerServer extends Thread
{
    public static long time = System.currentTimeMillis();

    ShuttleServer ship;
    StationServer station;

    Object controllerObj = new Object();
    Object resovoirObj = new Object();


    public ControllerServer(int id)
    {
        setName("Controller-" + id);
        station = new StationServer();
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

    public void openChargingStation()
    {
        // notifyAll on numRecharge object
        if(station.waitingToRecharge == station.numRecharge)
        {
            synchronized (station.recharge)
            {
                msg("letting ships in to recharge numCharge at a time");
                station.recharge.notifyAll();

            }
        }
    }

    public void goHome()
    {
        msg("went home");
    }

    public void run()
    {
        synchronized (station.stationObj)
        {
            wait(station.stationObj);
        }

        while(station.openOrClose)
        {

            for (int i = 0; i < station.numRecharge; i++)
            {
                synchronized (station.waitingShuttles.get(i))
                {
                    station.waitingShuttles.get(i).notify();
                }
            }

            openChargingStation();

            synchronized (station.recharge)
            {
                station.recharge.notifyAll();
            }

            if (station.waitingToRecharge == station.numRecharge)
            {
                synchronized(station.recharge)
                {
                    wait(station.recharge);
                }
            }

            // wait on to be called to fill up reservoir
            if(station.waitingOnRersvoir > 0)
            {
                synchronized (station.reservoir)
                {
                    //wait(station.reservoir);
                }
                station.reservoir.notify();
                station.waitingOnRersvoir = 0;
            }

        }

        goHome();
    }

}