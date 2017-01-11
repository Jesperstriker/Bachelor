package application;

import model.Actor;

public class Movement {
	String locSource;
	String locTarget;
	Actor actor;
	int time;
	int edgeTime;
	
	public Movement(String locSource, String locTarget ,int edgeTime, Actor actor, int time){
	this.locSource = locSource;
	this.locTarget = locTarget;
	this.actor = actor;
	this.time = time;
	this.edgeTime = edgeTime;
	}
	
	public String getlocSource(){
		return locSource;
	}
	public void setlocSource(String locSource){
		this.locSource = locSource;
	}
	
	public String getlocTarget(){
		return locTarget;
	}
	public void setlocTarget(String locTarget){
		this.locTarget = locTarget;
	}
	
	public Actor getActor(){
		return actor;
	}
	
	public void setActor(Actor actor){
		this.actor = actor;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setTime(int time){
		this.time = time;
	}
	
}
