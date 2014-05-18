package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
/**
 * Clase de Diagrama de Estados, maneja sus estados y transiciones.
 * También tiene un objeto que puede checkear la correctitud y validez
 * del diagrama.
 * @author Cate
 *
 */


public class StateDiagram {

	private ArrayList<State> states;
	private ArrayList<Transition> transitions;
	private StateDiagramChecker checker;
	public StateDiagram(){
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
		checker=new StateDiagramChecker();
	}
	/**
	 * Este método se ocupa al final y es el que verifica que el diagrama esté
	 * correcto. 
	 */
	public void isCorrect(){
		checker.checkAll(this);
	}
	public void addState(String name, int id){
		State s=new State(id, name);
		this.addState(s);
	}
	private void addState(State s){
		states.add(s);
	}
	public void addTransition(State origin, State destiny, String input, String output) throws InvalidTransitionException, AbsentStateException{
		Transition t=new Transition(origin, destiny, input, output);
		this.addTransition(t);
	}
	/**
	 * Agrega una transición, verificando que los estados de la transición
	 * especificados en ella sean del diagrama (nunca debería pasar).
	 * @param t
	 * @throws AbsentStateException
	 */
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
	/**
	 * Este método sirve para obtener la matriz que ocupará la Cami para generar
	 * su tabla de verdad. 
	 * @see RepresentationMatrix
	 * @return m
	 */
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
