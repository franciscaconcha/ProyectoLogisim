package com.cburch.logisim.statediagram;

import java.util.HashSet;
import java.util.Set;

public class StateDiagram {
	static class AbsentStateException extends Exception {	}
	private Set<State> states;
	private Set<Transition> transitions;
	public StateDiagram(){
		states = new HashSet<State>();
		transitions = new HashSet<Transition>();
	}
	public void addState(State s){
		states.add(s);
	}
	public void addTransition(Transition t) throws AbsentStateException{
		if (states.contains(t.getOrigin()) && states.contains(t.getDestiny()))
			transitions.add(t);
		else
			throw new AbsentStateException();	
	}
	public boolean isState(State s){
		return states.contains(s);
	}
	public boolean isTransition(Transition t){
		return transitions.contains(t);
	}
}
