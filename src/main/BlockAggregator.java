package main;

import java.util.Date;
import java.util.HashMap;

public class BlockAggregator 
{
	
	private HashMap<String,Integer> collectedMethodResults;
	private HashMap<String,Integer> collectedBrowserResults;
	
	private Date earliestDate, latestDate;
		
	public BlockAggregator()
	{	
		collectedMethodResults = new HashMap<String,Integer>();
		collectedBrowserResults = new HashMap<String, Integer>();
	}

	public synchronized void receiveMethodDetails(HashMap<String,Integer> details) 
	{
		for(String key: details.keySet())
		{
			if(collectedMethodResults.containsKey(key))
			{
				Integer val1 = collectedMethodResults.get(key);
				Integer val2 = details.get(key);
				collectedMethodResults.put(key, val1 + val2);
			}
			else
			{
				Integer value = details.get(key);
				collectedMethodResults.put(key, value);
			}
		}
	}	
	
	public synchronized void receiveBrowserDetails(HashMap<String,Integer> details) 
	{
		for(String key: details.keySet())
		{
			if(collectedBrowserResults.containsKey(key))
			{
				Integer val1 = collectedBrowserResults.get(key);
				Integer val2 = details.get(key);
				collectedBrowserResults.put(key, val1 + val2);
			}
			else
			{
				Integer value = details.get(key);
				collectedBrowserResults.put(key, value);
			}
		}
	}

	public synchronized void receiveDateDetails(Date earliest, Date latest) 
	{
		if(earliestDate == null)
		{
			earliestDate = earliest;
		} 
		else if (earliest.before(earliestDate))
		{
			earliestDate = earliest;
		}
		
		if(latestDate == null)
		{
			latestDate = latest;
		}
		else if (latest.after(latestDate))
		{
			latestDate = latest;
		}
	}
	
	public String report()
	{
		StringBuilder str = new StringBuilder();
		int mozilla = 0, msie = 0, other = 0;
		
		str.append("Earliest log entry: " + earliestDate.toString() + "\n");
		str.append("Latest log entry: " + latestDate.toString() + "\n");
		
		str.append("Methods recorded: \n");
		
		for(String key: collectedMethodResults.keySet())
		{
			str.append(key + ": " + collectedMethodResults.get(key) + "\n");
		}
		
		str.append("Browser records:\n");
		for(String key: collectedBrowserResults.keySet())
		{
			if(key.contains("Mozilla"))
			{
				mozilla += collectedBrowserResults.get(key).intValue();
			}
			else if (key.contains("Internet Explorer"))
			{
				msie += collectedBrowserResults.get(key).intValue();
			}
			else
			{
				other += collectedBrowserResults.get(key).intValue();
			}			
		}
		
		str.append("Internet Explorer family: " + msie + "\n");
		str.append("Mozilla family: " + mozilla + "\n");
		str.append("Other: " + other + "\n");
		
		return str.toString();
	}
	
	
}
