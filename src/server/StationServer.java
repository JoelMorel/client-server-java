package server;

import java.util.Vector;

public class StationServer extends Thread
{
    public static long time = System.currentTimeMillis();

    public int reservoirCapacity = 200;
    public int currentReservoir = 200;

    static int numShuttles = 0;
    int waitingToRecharge = 0;
    int waitingOnRersvoir = 0;
    int tracksInUse = 0;
    int totalTracks = 3;
    int numRecharge = 3;
    boolean openOrClose = false;

    Object reservoir = new Object();
    Object recharge = new Object();
    Object takeOff = new Object();
    Object stationObj = new Object();

    Vector<ShuttleServer> Shuttles= new Vector<ShuttleServer>();
    Vector<Object> waitingShuttles= new Vector<Object>(numShuttles);


    public StationServer()
    {
    }

    public StationServer(int id)
    {
        setName("Station-" + id);
    }

    public void msg(String m)
    {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName() + ": "+m);
    }

    public int getCurrentReservoir()
    {
        return reservoirCapacity;
    }

    public void setCurrentReservoir()  // should be synchronozized
    {
        this.currentReservoir = reservoirCapacity;
    }

    public void getFuel(int fuelAmount)
    {
        currentReservoir = currentReservoir - fuelAmount;
    }

    public void run()
    {


    }

}
