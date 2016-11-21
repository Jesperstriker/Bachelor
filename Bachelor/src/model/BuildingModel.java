package model;

import java.util.HashMap;

import automaton.Automaton;

public class BuildingModel {
	
	private Automaton eNFA;
	private HashMap<Role,Automaton> automata;
	private HashMap<String,Actor> actors;
	private HashMap<String,Role> roleHierarchy;
	
	//Building model consist of all automatons for all roles in the building.
	public BuildingModel(HashMap<Role,Automaton> automata, HashMap<String,Actor> actors, HashMap<String,Role> roleHierarchy, Automaton eNFA) {
		this.actors = actors;
		this.automata = automata;
		this.roleHierarchy = roleHierarchy;
		this.eNFA = eNFA;
	}
	public HashMap<Role,Automaton> getAutomata() {
		return automata;
	}
	public HashMap<String,Actor> getActors() {
		return actors;
	}
	public HashMap<String,Role> getRoleHierarchy() {
		return roleHierarchy;
	}
	public Automaton getENFA() {
		return eNFA;
	}
}
