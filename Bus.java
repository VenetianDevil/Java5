class Bus implements BusInterface
{
    private int number;

    public int getBusNumber()
    {
        return number;
    }

    public Bus(int n)
    {
        number = n;
    }
}