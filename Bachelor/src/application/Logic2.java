package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import automaton.Automaton;
import automaton.components.Node;
import data.access.Log;
import data.access.LogReader;
import model.*;


public class Logic2 {
	static Set<Movement> movements = new HashSet<Movement>();
	
	public static void checkLog2(BuildingModel bm, String filePath, Automaton enfa)
	{
		HashMap<String,Node> nodes = enfa.getAllNodes();
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
    	findActors(bm, maxObserved, nodes);
    }
	
	public static void findActors (BuildingModel bm, int maxObserved, HashMap<String,Node> nodes){
		for (int t = 0; t <= maxObserved; t++) {
			
			
			
			
		}
		
		
	}
	
}
