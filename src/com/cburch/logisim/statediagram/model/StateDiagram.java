package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StateDiagram {
	static class AbsentStateException extends Exception {	}
	private ArrayList<State> states;
	private ArrayList<Transition> transitions;
	private int nextStateId;
	private StateDiagramChecker checker;
	public StateDiagram(){
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		nextStateId=0;
		checker=new StateDiagramChecker();
	}
	public void isCorrect(){
		checker.checkAll(this);
	}
	public void addState(String name){
		State s=new State(nextStateId, name);
		nextStateId++;
		this.addState(s);
	}
	private void addState(State s){
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
	public State getState(int id){
		return states.get(id);
	}
	public boolean isTransition(Transition t){
		return transitions.contains(t);
	}
	public ArrayList<Transition> getTransitions(){
		return this.transitions;
	}
	public RepresentationMatrix getRepresentationMatrix(){
		RepresentationMatrix m=new RepresentationMatrix(states.size());
		for (Transition t: transitions){
			int o=t.getOrigin().getId();
			int d=t.getDestiny().getId();
			String in=t.getInput();
			String out=t.getOutput();
			m.setInput(o, d, in);
			m.setOutput(o, d, out);
		}
		return m;							
	}
}
