package model;

import automaton.Automaton;
import automaton.components.Node;

public class Actor {
	private String name;
	private Role role;
	private Automaton a;
	private Node position;
	private int timestamp;
	
	public Actor(String name, Role role)
	{
		this.name = name;
		this.role = role;
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

