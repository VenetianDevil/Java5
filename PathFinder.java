import java.util.Vector;

class PathFinder implements PathFinderInterface
{
    private Vector <Awtobus> lines = new Vector<Awtobus>();    

//zawiera numer autobusu i jego linie
    private class Awtobus
    {
        public BusInterface bus;
        public BusLineInterface busLine;

        Awtobus(BusInterface bus, BusLineInterface busLine)
        {
            this.bus=bus;
            this.busLine = busLine;
            
        }
    }

//informacje o danym przystanku - jaki autobus go obsluguje, ktory jest na danej linii
    private class BusStopInfo
    {
        public Awtobus awtobus;
        public int pozycja;

        BusStopInfo(Awtobus a, int p)
        {
            this.awtobus = a;
            this.pozycja = p;
        }
    }


    public void addLine(BusLineInterface line, BusInterface bus)
    {
        Awtobus autobus = new Awtobus(bus, line);
        lines.add(autobus);
    }

//znajdowanie wektora z informacjami o danym przystanku
    private Vector <BusStopInfo> findStopInfo(BusStopInterface busStop)
    {
        Vector <BusStopInfo> result = new Vector<BusStopInfo>();
        Awtobus checking = null;
        for(int i=0; i < lines.size(); i++)
        {
            checking = lines.get(i);
            int length = checking.busLine.getNumberOfBusStops();
            for(int j=0; j<length; j++)
            {
                if(busStop.getName() == checking.busLine.getBusStop(j).getName())
                {
                    result.add(new BusStopInfo(checking, j));
                }
            }
        }
        return result;
    }

//sprawdza czy moge isc jedna linia, jesli nie, musze sprawdzic wszystkie
    private Vector<BusStopInfo> infoFilter(Vector <BusStopInfo> fromInfo, Vector <BusStopInfo> toInfo)
    {
        for(BusStopInfo info: fromInfo)
        {
            for(BusStopInfo info2: toInfo)
            {
                if(info.awtobus.bus.getBusNumber() == info2.awtobus.bus.getBusNumber())
                {
                    Vector<BusStopInfo> ret = new Vector<BusStopInfo>();
                    ret.add(info);
                    return ret;
                }
            }
        }
        return fromInfo;
    }

//rekurencyjne szukanie kazdej mozliwej trasy
    private Vector<Vector<BusStopInfo>> findRek (BusStopInterface from, BusStopInterface to, Vector <BusStopInfo> toInfo, Vector<BusStopInfo> visited, int swaps, int transfers )
    {
    	
    	System.out.println("From: " + from.getName());
    	
    	for(BusStopInfo m: visited)
    	{
    		System.out.println(m.awtobus.busLine.getBusStop(m.pozycja).getName());
    	}
    	
    	
        Vector<Vector<BusStopInfo>> sciezka = new Vector<Vector<BusStopInfo>>();
        
        Vector <BusStopInfo> fromInfo = findStopInfo(from);
        
        if(from.getName()==to.getName())
        {
        	for(BusStopInfo last: toInfo)
        	{
        		for(BusStopInfo lastFrom: fromInfo)
        		if(lastFrom.awtobus.bus.getBusNumber()==last.awtobus.bus.getBusNumber())
        		{
        			sciezka.add(toInfo);
        			return sciezka;        			
        		}
        	}
        }

        for(BusStopInfo vis: visited)
        {
            if(vis.awtobus.busLine.getBusStop(vis.pozycja).getName()==from.getName())
            return sciezka;
        }


        System.out.println("FromInfo : " + fromInfo.size());

        fromInfo = infoFilter(fromInfo, toInfo);
        System.out.println("FromInfo : " + fromInfo.size());

        for(BusStopInfo info: fromInfo)
        {
            int swapsNow = swaps;
            if(visited.size()>0 && info.awtobus.bus.getBusNumber()!=visited.lastElement().awtobus.bus.getBusNumber())
            {
                swapsNow++;
            }

            if(swapsNow <= transfers)
            {
                int nextPosition = info.pozycja +1; //ide do przodu linii
                int prevPosition = info.pozycja -1; //ide do tylu linii

                Vector<BusStopInfo> temp =  new Vector<BusStopInfo>(visited);
                temp.add(info);
                Vector<Vector<BusStopInfo>> result ;
                if(nextPosition<info.awtobus.busLine.getNumberOfBusStops())
                {
                    BusStopInterface nextStop = info.awtobus.busLine.getBusStop(nextPosition);
                    result = findRek(nextStop, to, toInfo, temp, swapsNow, transfers);
                    for(Vector<BusStopInfo> res: result)
                    {
                        res.add(0, info);
                        sciezka.add(res);
                    }
                }
                if(prevPosition >= 0)
                {
                    BusStopInterface prevStop = info.awtobus.busLine.getBusStop(prevPosition);
                    result = findRek(prevStop, to, toInfo, temp, swapsNow, transfers);
                    for(Vector<BusStopInfo> res: result)
                    {
                        res.add(0, info);
                        sciezka.add(res);
                    }
                }
            }
        }

        return sciezka;
    }

    private Vector<Vector<BusStopInfo>> solutions;

    public void find( BusStopInterface from, BusStopInterface to, int transfers)
    {
        Vector <BusStopInfo> toInfo = findStopInfo(to);
        solutions = findRek(from, to, toInfo, new Vector<BusStopInfo>(), 0, transfers);
    }

    public int getNumerOfSolutions()
    {
        return solutions.size();
    }

    public int getBusStops( int solution )
    {
        return solutions.get(solution).size();
    }

    public BusStopInterface getBusStop( int solution, int busStop )
    {
        BusStopInfo info = solutions.get(solution).get(busStop);
        return info.awtobus.busLine.getBusStop(info.pozycja);
    }

    public BusInterface getBus( int solution, int busStop )
    {
        return solutions.get(solution).get(busStop).awtobus.bus;
    }

    public PathFinder()
    {}
}