class BusStop implements BusStopInterface
{
    private String stopName;

    public String getName()
    {
        return stopName;
    }

    public BusStop(String n)
    {
        stopName = n;
    }
}