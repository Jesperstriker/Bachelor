package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automaton.Automaton;
import automaton.components.Connection;
import automaton.components.Node;
import data.access.Log;
import data.access.LogReader;
import model.*;

public class Logic2 {
	static Set<Movement> movements = new HashSet<Movement>();

	public static void checkLog2(BuildingModel bm, String filePath) {
		LogReader reader = new LogReader(filePath);
		ArrayList<Log> currentLogs = new ArrayList<Log>();
		int maxObserved = 0;
		while (reader.hasNext()) {
			Log log = reader.getNextLog();
			int timeLog = log.getCounter();
			if (timeLog > maxObserved) {
				maxObserved = timeLog;
			}
			currentLogs.add(log);

		}
		findActors(bm, maxObserved, currentLogs);
	}

	public static void findActors(BuildingModel bm, int maxObserved,
			ArrayList<Log> currentLogs) {
		HashMap<String, Node> nodes = bm.getENFA().getAllNodes();
		// Timespan of the logs, from 0 to max observed
		String s = "";
		boolean flag = true;
		for (Actor actor : bm.getActors().values()) {
			if (flag) {
				s = String.format("%-30s", actor.getName());
				flag = false;
			} else {
				s = String.format("%s %-30s", s, actor.getName());
			}
		}
		System.out.println(String.format("%s %-20s", s,"Time"));

		// If the t = to the time in the log, we have observed
		// the actor at this place.
		for (int t = 0; t <= maxObserved; t++) {
			if (compareLogs(bm, currentLogs, t, nodes)) {
				continue;
			}

			// Foreach actor
			for (Actor actor : bm.getActors().values()) {
				// Foreach location that the actor has
				for (String location : actor.getLocations()) {
					// Foreach edge that each of theese locations has.
					for (Map.Entry<String, Connection> entry : nodes.get(location).getSuccessors().entrySet()) {
						Connection edge = entry.getValue();
						edge.getNode().getName();
						edge.getTime();
						movements.add(new Movement(location, edge.getNode().getName(), edge.getTime(), actor, t));
					}
				}
			}

			for (Iterator<Movement> i = movements.iterator(); i.hasNext();) {
				Movement movement = i.next();
				if (movement.edgeTime + movement.getTime() == t) {
					movement.getActor().addLocation(movement.getlocTarget());
					i.remove();
				}
			}
			printLocations(bm, t);
		}

	}

	private static boolean compareLogs(BuildingModel bm, ArrayList<Log> currentLogs, int t,
			HashMap<String, Node> nodes) {
		boolean flag = false;
		for (Log log : currentLogs) {
			if (log.getCounter() == t && !log.getActorID().equals("Unknown")) {
				Actor actor = bm.getActors().get(log.getActorID());
				actor.resetLocation(log.getTo());
				printLocations(bm, t);
				for (Iterator<Movement> i = movements.iterator(); i.hasNext();) {
					Movement movement = i.next();
					if (movement.getActor() == actor) {
						i.remove();
					}
				}
				for (Map.Entry<String, Connection> entry : nodes.get(log.getTo()).getSuccessors().entrySet()) {
					Connection edge = entry.getValue();
					edge.getNode().getName();
					edge.getTime();
					movements.add(new Movement(log.getTo(), edge.getNode().getName(), edge.getTime(), actor, t));
				}
				flag = true;
			}
		}
		return flag;
	}

	private static void printLocations(BuildingModel bm, int t) {
		String s = "";
		for (Actor actor : bm.getActors().values()) {
			boolean flag = true;
			String s1 = "";
			for (String location : actor.getLocations()) {
				if (flag) {
					s1 = s1+location;
					flag = false;
				} else {
					s1 = s1 + "," + location;
				}
			}
				s = String.format("%s %-30s", s, s1);
			
		}
		System.out.println(String.format("%s %-10d", s,t));
	}

}
