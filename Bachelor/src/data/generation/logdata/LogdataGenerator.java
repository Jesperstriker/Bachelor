package data.generation.logdata;

import data.generation.Tools;
import model.*;
import automaton.*;
import automaton.components.Connection;
import automaton.components.Node;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LogdataGenerator {

	// Data should be created from the non simplified automata (e-NFA)
	public static void generateLogdata(BuildingModel bm, Path logdataFilePath, Path errorPath, int numberOfLogs,
			int tailgatingViolations, int cardViolations) {
		int numberOfViolations = tailgatingViolations + cardViolations;

		if (numberOfLogs < 1) {
			System.out.println("Error no logs will be generated");
			return;
		}
		if (numberOfViolations < 0) {
			System.out.println("Error the amount of violations in the logdata can not be negative");
			return;
		}
		// Initialize components
		Automaton automaton = bm.getENFA();
		HashSet<Actor> allActors = new HashSet<Actor>();
		HashMap<Integer, HashSet<Actor>> availableActors = new HashMap<Integer, HashSet<Actor>>();
		for (Actor actor : bm.getActors().values()) {
			allActors.add(actor);
			actor.setAutomaton(automaton);
			actor.setTimestamp(0);
		}

		int timestamp = 0;

		availableActors.put(timestamp, allActors);

		int numberOfActors = allActors.size();
		// Used to check the number of logs created
		int counter = 0;
		// List of logs
		List<String> logdata = new ArrayList<String>();
		List<String> logdataError = new ArrayList<String>();
		HashSet<Integer> errorLines = new HashSet<Integer>();

		// Data generating loop
		while (true) {

			HashSet<Actor> actors = availableActors.get(timestamp);

			for (Actor currentActor : safe(actors)) {
				System.out.println(currentActor);
				Node currentNode = currentActor.getPosition();
				String nextNode;
				if (currentNode.getSuccessors().isEmpty()) {
					continue;
				} else {
					nextNode = selectRandomConnection(currentNode.getSuccessors(), currentActor, bm.getRoleHierarchy());
					if (nextNode == null) {
						continue;
					}
				}
				Connection nextCon = currentNode.getSuccessors().get(nextNode);
				Random rand = new Random();
				int  n = rand.nextInt(100) + 1;
				
			if (n < 70){
				if (nextCon.getFirstPolicy().isLogged() && nextCon.isPersonSpecific()) {
					String log = currentActor.getName() + " " + currentNode.getName() + " " + nextNode + " "
							+ nextCon.getFirstPolicy().getName() + " " + (timestamp + nextCon.getTime()); // Log
					logdata.add(log);
					counter++;
					
					if (availableActors.get(timestamp+nextCon.getTime()) == null) {
						HashSet<Actor> newset = new HashSet<Actor>();
						newset.add(currentActor);
						availableActors.put(timestamp+nextCon.getTime(), newset);
					} else {
						availableActors.get(timestamp+nextCon.getTime()).add(currentActor);
					}
					
					HashSet<Actor> tailgaters = checkLocation(allActors, currentNode, currentActor);
					if (!tailgaters.isEmpty()) {
						for (Actor tailgatingActors : tailgaters) {
							if (tailgatingViolations == 0) break;
							tailgatingActors.setPosition(nextCon.getNode());
							tailgatingViolations--;
						}
					}
				} else if (nextCon.getFirstPolicy().isLogged()) {
					String log = "Unknown " + currentNode.getName() + " " + nextNode + " "
							+ nextCon.getFirstPolicy().getName() + " " + (timestamp + nextCon.getTime()); // Log
																											// //
																											// string
					logdata.add(log);
					counter++;
					
					if (availableActors.get(timestamp+nextCon.getTime()) == null) {
						HashSet<Actor> newset = new HashSet<Actor>();
						newset.add(currentActor);
						availableActors.put(timestamp+nextCon.getTime(), newset);
					} else {
						availableActors.get(timestamp+nextCon.getTime()).add(currentActor);
					}
					
					HashSet<Actor> tailgaters = checkLocation(allActors, currentNode, currentActor);
					if (!tailgaters.isEmpty()) {
						for (Actor tailgatingActors : tailgaters) {
							if (tailgatingViolations == 0) break;
							tailgatingActors.setPosition(nextCon.getNode());
							tailgatingViolations--;
						}
					}
				}
			}
			else {
				if (availableActors.get(timestamp+1) == null) {
					HashSet<Actor> newset = new HashSet<Actor>();
					newset.add(currentActor);
					availableActors.put(timestamp+1, newset);
				} else {
					availableActors.get(timestamp+1).add(currentActor);
				}
			
			}

				currentActor.setPosition(nextCon.getNode());
				if (availableActors.get(timestamp+nextCon.getTime()) == null) {
					HashSet<Actor> newset = new HashSet<Actor>();
					newset.add(currentActor);
					availableActors.put(timestamp+nextCon.getTime(), newset);
				} else {
					// Add the actor to the next timestamp
					// without changing the position
					availableActors.get(timestamp+nextCon.getTime()).add(currentActor);
				}
			}

			// Break statement is used instead of logdata.size so we don't
			// constantly have to evaluate the length of the list.
			if (counter - tailgatingViolations >= numberOfLogs) {
				break;
			}

			timestamp++;
		}

		// Exceptions should probably be handled differently
		try {
			if (numberOfViolations > 0) {
				createViolations(tailgatingViolations, logdata, logdataError, errorLines, errorPath, logdataFilePath);
			} else {
				Tools.WriteAndCreateFile(logdataFilePath, logdata);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static HashSet<Actor> checkLocation(HashSet<Actor> actors, Node currentNode, Actor currentActor) {
		HashSet<Actor> tailgaters = new HashSet<Actor>();
		for (Actor actor : actors) {
			if (actor.getPosition().equals(currentNode) && !(actor.equals(currentActor))) {
				tailgaters.add(actor);
			}
		}
		return tailgaters;
	}

	private static HashSet<Actor> safe(HashSet<Actor> actors) {
		return actors == null ? new HashSet<Actor>() : actors;
	}

	private static boolean createViolation(int numberOfLogs, int counter, int cardViolations) {
		if (cardViolations == 0) {
			return false;
		}
		double probability = (double) cardViolations / (double) (numberOfLogs - counter);
		if (probability > 0.98) {
			return true;
		} else {
			return Math.random() >= 1.0 - probability;
		}
	}

	private static String selectRandomConnection(HashMap<String, Connection> successors, Actor actor,
			HashMap<String, Role> rh) {
		Random r = new Random();
		Role role = actor.getRole();
		int numberOfSuccessors = successors.keySet().size();
		List<String> successorKeys = new ArrayList<String>(successors.keySet());
		int i = 0;
		while (i < 25) // To make sure it will not execute forever....
		{
			int successor = r.nextInt(numberOfSuccessors);
			if (successors.get(successorKeys.get(successor)).hasRole(role, rh)) {
				return successorKeys.get(successor);
			}
			i++;
		}
		return null;

	}

	private static Actor selectRandomActor(int numberOfActors, List<Actor> actors) {
		Random r = new Random();
		int actorNumber = r.nextInt(numberOfActors);

		return actors.get(actorNumber);
	}

	private static void createViolations(int numberOfViolations, List<String> logdata, List<String> logdataError,
			HashSet<Integer> errorLines, Path errorPath, Path path) {
		Random r = new Random();
		int i = 0;
		List<String> errors = new ArrayList<String>();
		while (i < numberOfViolations) {
			int removelog = r.nextInt(logdata.size());

			if (!errorLines.contains(removelog)) {
				errors.add(logdata.get(removelog));
				logdata.set(removelog, "");
				errorLines.add(removelog);
				i++;
			}
		}

		for (int j = logdata.size() - 1; j >= 0; j--) {
			if (logdata.get(j).equals("")) {
				logdata.remove(j);
			}
		}
		logdata.addAll(logdataError);

		logdata.sort((s1, s2) -> Integer.valueOf(s1.split(" ")[4]).compareTo(Integer.valueOf(s2.split(" ")[4])));

		errors.sort((s1, s2) -> Integer.valueOf(s1.split(" ")[4]).compareTo(Integer.valueOf(s2.split(" ")[4])));

		try {
			Tools.WriteAndCreateFile(errorPath, errors);
			Tools.WriteAndCreateFile(path, logdata);
		} catch (IOException e) {
		}
	}

}