package com.cburch.logisim.statediagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StateDiagram {
	static class AbsentStateException extends Exception {	}
	static class InconsistentInputLengthException extends Exception {	}
	static class InconsistentOutputLengthException extends Exception {	}
	private ArrayList<State> states;
	private ArrayList<Transition> transitions;
	private int nextStateId;
	public StateDiagram(){
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		nextStateId=0;
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
	public void checkTransitionsLength(){
		try {
			this.checkInputsLengths();
		} catch (InconsistentInputLengthException e) {
			System.out.println("Inputs de distinto largo");
			e.printStackTrace();
		}
		try {
			this.checkOuputLengths();
		} catch (InconsistentOutputLengthException e) {
			System.out.println("Outputs de distinto largo");
			e.printStackTrace();
		}

		
	}
	public void checkInputsLengths() throws InconsistentInputLengthException {
		Transition[] t=(Transition [])transitions.toArray();
		Transition test=t[0];
		for (int i=1; i<t.length; i++)
			if (t[i].getInput().length()!=test.getInput().length())
				throw new InconsistentInputLengthException();
				
			
	}
	
	public void checkOuputLengths() throws InconsistentOutputLengthException{
		Transition[] t=(Transition [])transitions.toArray();
		Transition test=t[0];
		for (int i=1; i<t.length; i++)
			if (t[i].getOutput().length()!=test.getInput().length())
				throw new InconsistentOutputLengthException();
			
			
	}
}
