package com.cburch.logisim.statediagram.model;
/**
 * Clase estado, contiene informaci�n importante de cada estado (nombre e id). Contiene un m�todo toString
 * y getters y setters. No creo que sea necesario decir nada m�s.
 * @author Cate
 *
 */
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
	public boolean equals(State s){
		return (this.getId()==s.getId() && this.getName()==s.getName());
	}

}
