package model;

import java.util.HashSet;
import java.util.Set;

import automaton.Automaton;
import automaton.components.Node;

public class Actor {
	private String name;
	private Role role;
	private Automaton a;
	private Node position;
	private int timestamp;
	private Set<String> locations = new HashSet<String>();
	
	public Actor(String name, Role role)
	{
		this.name = name;
		this.role = role;
		//Everybody starts at s
		this.locations.add("s");
	}

	public Actor (String name)
	{
		this.name = name;
	}
	
	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }
	public Automaton getAutomaton() { return a; }
	public void setAutomaton(Automaton a) { this.a = a; position = a.getStart(); }
	public String getName() { return name; }
	public Node getPosition() { return position; }
	public void setPosition(Node position) { this.position = position; }
	public int getTimestamp() {return timestamp;}
	public void setTimestamp(int timestamp) {this.timestamp = timestamp;}
	
	public Set<String> getLocations(){return this.locations;}
	public void addLocation(String location){this.locations.add(location);}

	public String toString()
	{
		return name + ", " + role.toString();
	}
	
	public boolean canTransition(Node newPos)
	{
		return a.canTransition(position,newPos);
	}
	public boolean hasTailgated(Node newPos)
	{
		return a.hasTailgated(position, newPos);
	}

}

