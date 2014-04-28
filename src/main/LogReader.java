package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogReader 
{
	private static final int MAX_THREAD_COUNT = 6;
	
	public static void main(String[] args)
	{
		Scanner input = null;
		File file = null;
		long fileSize, blockSize, entrySize;
		String logEntry;
		StringBuilder logBlock;
		ExecutorService ex = Executors.newCachedThreadPool();
		BlockAggregator aggregate = new BlockAggregator();
		
		try 
		{
			file = new File("opt/access_log.txt");
			input = new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			System.err.println("Error opening file!");
		}
		
		fileSize = file.length();
		blockSize = fileSize / MAX_THREAD_COUNT;
								
		while(input.hasNextLine())
		{
			logBlock = new StringBuilder();
			
			for(long i = blockSize; i > 0 && input.hasNextLine(); i-=entrySize)
			{
				logEntry = input.nextLine();
				entrySize = logEntry.getBytes().length;
				logBlock.append(logEntry + "\n");
			}
			
			ex.execute(new BlockProcessor(logBlock, aggregate));
			//System.out.println("New thread started!");
		}
		
		System.out.println("Processing file...");
		
		ex.shutdown();
		try 
		{
			ex.awaitTermination(20, TimeUnit.SECONDS);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		System.out.println(aggregate.report());
		
	}
}
