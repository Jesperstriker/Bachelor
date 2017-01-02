package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import automaton.components.Node;
import data.access.Log;
import data.access.LogReader;
import model.*;


public class Logic2 {
	public static void checkLog2(BuildingModel bm, String filePath)
	{
		LogReader reader = new LogReader(filePath);
    	HashMap<String,Actor> actors = bm.getActors();
    	ArrayList<Log> currentLogs = new ArrayList<Log>();
    	int maxObserved = 0;
    	while (reader.hasNext())
    		{
    		Log log = reader.getNextLog();		
    		int timeLog = log.getCounter();
    		if (timeLog > maxObserved){
    			maxObserved = timeLog;
    		}
    		currentLogs.add(log);
    		
    		}
    	findActors(maxObserved);
    }
	
	public static void findActors (int maxObserved){
		for (int t = 0; t < maxObserved; t++) {
			
		}
		
		
	}
	
}
