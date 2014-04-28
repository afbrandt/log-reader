package main;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineParser 
{
	private String inputLine;
	private HashMap<String,String> logDetails;
	
	private final String DATE_REGEX = "\\[[a-zA-Z0-9\\:\\/\\-\\ ]*\\]";
	private final String BROWSER_REGEX = "\\\"[^\\\"]*?\\\"$";
	private final String METHOD_REGEX = "\\]\\ \\\"[A-Z]{3,}\\ ";
	
	public LineParser(String line)
	{
		inputLine = line;
		logDetails = new HashMap<String,String>();
	}
	
	public HashMap<String,String> getLogDetails()
	{
		//Extract log date
		Pattern datePattern = Pattern.compile(DATE_REGEX);
		Matcher dateMatch = datePattern.matcher(inputLine);
		
		if(dateMatch.find())
		{
			String dateString = inputLine.substring(dateMatch.start()+1, dateMatch.end()-1);
			logDetails.put("Date", dateString);
		}
;
		
		//Extract method
		Pattern methodPattern = Pattern.compile(METHOD_REGEX);
		Matcher methodMatch = methodPattern.matcher(inputLine);
		
		if(methodMatch.find())
		{
			String methodString = inputLine.substring(methodMatch.start()+3, methodMatch.end()-1);
			logDetails.put("Method", methodString);
		}

		//Extract browser information, if present
		Pattern browserPattern = Pattern.compile(BROWSER_REGEX);
		Matcher browserMatch = browserPattern.matcher(inputLine);
		
		if(browserMatch.find())
		{
			String browserString = inputLine.substring(browserMatch.start()+1, browserMatch.end()-1);
			//System.out.println(browserString);
			logDetails.put("Browser", browserString);
		}
		
		return logDetails;
	}
	
	
}
