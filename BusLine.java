import java.util.Vector;

class BusLine implements BusLineInterface
{

    private Vector<BusStop> busLine;

    public int getNumberOfBusStops()
    {
        return busLine.size();
    }
    
    public BusStopInterface getBusStop(int n)
    {
        BusStopInterface busStop;
        busStop = busLine.get(n);
        return busStop;
    }

    public BusLine(Vector<BusStop> line)
    {
        busLine = new Vector<BusStop>(line);
    }
}