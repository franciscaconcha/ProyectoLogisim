package com.cburch.logisim.statediagram.model;

public class State {
	private int id;
	private String name;
	public State(int id, String name){
		this.id=id;
		this.name=name;
	}
	public String toString(){
		return this.name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
