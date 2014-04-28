package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class BlockProcessor implements Runnable 
{

	private String logBlock;
	private BlockAggregator aggregator;
	private HashMap<String,Integer> collectedMethodResults;
	private HashMap<String,Integer> collectedBrowserResults;
	private Date earliestDate = null, latestDate = null;
	
	public BlockProcessor(StringBuilder block, BlockAggregator agg)
	{
		logBlock = block.toString();
		aggregator = agg;
		collectedMethodResults = new HashMap<String,Integer>();
		collectedBrowserResults = new HashMap<String, Integer>();
	}
	
	@Override
	public void run() 
	{
		Scanner parser = new Scanner(logBlock);
		
		while(parser.hasNextLine())
		{
			String entry = parser.nextLine();
			LineParser logEntry = new LineParser(entry);
			HashMap<String, String> result = logEntry.getLogDetails();
			String dateString = result.get("Date");
			String methodString = result.get("Method");
			String browserString = result.get("Browser");
						
			//Process date
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
			Date date = null;
			try
			{
				date = sdf.parse(dateString);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			
			if(earliestDate == null)
			{
				earliestDate = date;
			} 
			else if (date.before(earliestDate))
			{
				earliestDate = date;
			}
			
			if(latestDate == null)
			{
				latestDate = date;
			}
			else if (date.after(latestDate))
			{
				latestDate = date;
			}
			
			//Process method
			if(collectedMethodResults.containsKey(methodString))
			{
				Integer methodNum = collectedMethodResults.get(methodString);
				collectedMethodResults.put(methodString, methodNum+1);
			}
			else if(methodString != null)
			{
				collectedMethodResults.put(methodString, 1);				
			}
			
			//Process browser, if logged
			if(browserString != null)
			{
				if(collectedBrowserResults.containsKey(browserString))
				{
					Integer browserNum = collectedBrowserResults.get(browserString);
					collectedBrowserResults.put(browserString, browserNum +1);
				}
				else
				{
					collectedBrowserResults.put(browserString, 1);	
				}
			}
		}
				
		aggregator.receiveBrowserDetails(collectedBrowserResults);
		aggregator.receiveMethodDetails(collectedMethodResults);
		aggregator.receiveDateDetails(earliestDate, latestDate);
	}

}
