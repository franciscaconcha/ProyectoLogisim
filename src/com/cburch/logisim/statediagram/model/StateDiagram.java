package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.MissingTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.NoStatesException;
import com.cburch.logisim.statediagram.model.exceptions.NoTransitionsException;
import com.cburch.logisim.statediagram.model.exceptions.NotStronglyConnectedDiagram;
import com.cburch.logisim.statediagram.model.exceptions.RepeatedTransitionException;
/**
 * Clase de Diagrama de Estados, maneja sus estados y transiciones.
 * Tambien tiene un objeto que puede checkear la correctitud y validez
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
	 * Este metodo se ocupa al final y es el que verifica que el diagrama este
	 * correcto. 
	 * @throws notStronglyConnectedDiagram 
	 * @throws InconsistentOutputLengthException 
	 * @throws InconsistentInputLengthException 
	 * @throws MissingTransitionException 
	 * @throws RepeatedTransitionException 
	 * @throws NoTransitionsException 
	 * @throws NoStatesException 
	 * @throws InvalidTransitionException 
	 */
	public void isCorrect() throws InconsistentInputLengthException, InconsistentOutputLengthException, NotStronglyConnectedDiagram, MissingTransitionException, RepeatedTransitionException, NoStatesException, NoTransitionsException, InvalidTransitionException{
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
	 * Agrega una transicion, verificando que los estados de la transicion
	 * especificados en ella sean del diagrama (nunca deberia pasar).
	 * @param t
	 * @throws AbsentStateException
	 */
	private void addTransition(Transition t) throws AbsentStateException{
		if (this.isState(t.getOrigin()) && this.isState(t.getDestiny()))
			transitions.add(t);
		else
			throw new AbsentStateException();	
	}
	public boolean isState(State s) {
		for(State st: states){
			if(st.equals(s)){
				return true;}}
		return false;
	}
	public State getState(int id){
		return states.get(id);
	}

	public boolean isTransition(Transition t){
		for(Transition trans: transitions)
			if(trans.equals(t))
				return true;
		return false;
	}
	public ArrayList<Transition> getTransitions(){
		return this.transitions;
	}
	/**
	 * Este metodo sirve para obtener la matriz que ocupara la Cami para generar
	 * su tabla de verdad. Si se realiza un cambio en el diagrama, hay que generar
	 * la matriz de nuevo, ya que no es dinamica.
	 * @see RepresentationMatrix
	 * @return m
	 */

	public RepresentationMatrix getRepresentationMatrix() throws InvalidTransitionException{
		RepresentationMatrix m= new RepresentationMatrix(states.size());
		for(Transition t: transitions){
			m.addTransition(t);
		}
		m.setInputLength(transitions.get(0).getInput().length());
		m.setOutputLength(transitions.get(0).getOutput().length());
		return m;
	}
	public ArrayList<State> getStates() {
		return this.states;
	}
}
