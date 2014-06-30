/*
 *  **Model of simplified instance of JFLAP to draw state diagrams into Logisim**
 *  
 *  
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */


package com.cburch.logisim.statediagram.externalDrawer.diagram;

import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject.StateTransition;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateListener;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionListener;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;
import com.cburch.logisim.statediagram.model.*;
import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

import java.io.Serializable;
import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * ** Model of external state diagram drawer**
 * 
 * The automata object is the root class for the representation of all forms of
 * automata, including FSA, PDA, and Turing machines. This object does NOT
 * simulate the behavior of any of those machines; it simply maintains a
 * structure that holds and maintains the data necessary to represent such a
 * machine.
 * 
 * @see StateObject.State
 * @see TransitionObject.Transition
 * 
 * @author Thomas Finley
 */

public class Diagram implements Serializable{
	/**
	 * Creates an instance of <CODE>Diagram</CODE>. The created instance
	 * has no states and no transitions.
	 */
	public Diagram() {
		states = new HashSet<StateObject>();
		transitions = new HashSet<TransitionObject>();
	}

	/**
	 * Retrieves all transitions that eliminate from a state.
	 * 
	 * @param from
	 *            the <CODE>State</CODE> from which returned transitions
	 *            should come from
	 * @return an array of the <CODE>Transition</CODE> objects emanating from
	 *         this state
	 */
	public TransitionObject[] getTransitionsFromState(StateObject from) {
		TransitionObject[] toReturn = transitionArrayFromStateMap
				.get(from);
		if (toReturn == null) {
			List<TransitionObject> list = transitionFromStateMap.get(from);
			toReturn = list.toArray(new TransitionObject[0]);
			transitionArrayFromStateMap.put(from, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions that travel from a state.
	 * 
	 * @param to
	 *            the <CODE>State</CODE> to which all returned transitions
	 *            should go to
	 * @return an array of all <CODE>Transition</CODE> objects going to the
	 *         State
	 */
	public TransitionObject[] getTransitionsToState(StateObject to) {
		TransitionObject[] toReturn = transitionArrayToStateMap
				.get(to);
		if (toReturn == null) {
			List<TransitionObject> list = transitionToStateMap.get(to);
			toReturn = list.toArray(new TransitionObject[0]);
			transitionArrayToStateMap.put(to, toReturn);
		}
		return toReturn;
	}

	/**
	 * Retrieves all transitions going from one given state to another given
	 * state.
	 * 
	 * @param from
	 *            the state all returned transitions should come from
	 * @param to
	 *            the state all returned transitions should go to
	 * @return an array of all transitions coming from <CODE>from</CODE> and
	 *         going to <CODE>to</CODE>
	 */
	public TransitionObject[] getTransitionsFromStateToState(StateObject from, StateObject to) {
		TransitionObject[] t = getTransitionsFromState(from);
		ArrayList<TransitionObject> list = new ArrayList<TransitionObject>();
		for (int i = 0; i < t.length; i++)
			if (t[i].getToState() == to)
				list.add(t[i]);
		return list.toArray(new TransitionObject[0]);
	}

	/**
	 * Retrieves all transitions.
	 * 
	 * @return an array containing all transitions for this diagram
	 */
	public TransitionObject[] getTransitions() {
		if (cachedTransitions == null)
			cachedTransitions = transitions
					.toArray(new TransitionObject[0]);
		return cachedTransitions;
	}

	/**
	 * Adds a <CODE>Transition</CODE> to this diagram. This method may do
	 * nothing if the transition is already in the diagram.
	 * 
	 * @param trans
	 *            the transition object to add to the diagram
	 */
	public void addTransition(TransitionObject trans) {

		if (transitions.contains(trans))
			return;
        if(trans.getToState() == null || trans.getFromState() == null) return;
		transitions.add(trans);
        if(transitionFromStateMap == null) transitionFromStateMap = new HashMap<StateObject, LinkedList<TransitionObject>>();
		List<TransitionObject> list = transitionFromStateMap.get(trans.getFromState());
		list.add(trans);
        if(transitionToStateMap == null) transitionToStateMap = new HashMap<StateObject, LinkedList<TransitionObject>>();
		list = transitionToStateMap.get(trans.getToState()) ;
		list.add(trans);
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new TransitionEvent(this, trans,
				true, false));
	}

	/**
	 * Replaces a <CODE>Transition</CODE> in this diagram with another
	 * transition with the same from and to states. This method will delete the
	 * old if the transition is already in the diagram.
	 * 
	 * @param oldTrans
	 *            the transition object to add to the diagram
	 * @param newTrans
	 *            the transition object to add to the diagram
	 */
	public void replaceTransition(TransitionObject oldTrans,TransitionObject newTrans) {
		if (!getTransitionClass().isInstance(newTrans)) {
			throw new IncompatibleTransitionException();
		}
		if (oldTrans.equals(newTrans)) {
			return;
		}
		if (transitions.contains(newTrans)) {
			removeTransition(oldTrans);
			return;
		}
		if (!transitions.remove(oldTrans)) {
			throw new IllegalArgumentException(
					"Replacing transition that not already in the diagram!");
		}
		transitions.add(newTrans);
		List<TransitionObject> list = transitionFromStateMap.get(oldTrans.getFromState());
		list.set(list.indexOf(oldTrans), newTrans);
		list = transitionToStateMap.get(oldTrans.getToState());
		list.set(list.indexOf(oldTrans), newTrans);
		transitionArrayFromStateMap.remove(oldTrans.getFromState());
		transitionArrayToStateMap.remove(oldTrans.getToState());
		cachedTransitions = null;
		distributeTransitionEvent(new TransitionEvent(this, newTrans,
				true, false));
	}

	/**
	 * Removes a <CODE>Transition</CODE> from this diagram.
	 * 
	 * @param trans
	 *            the transition object to remove from this diagram.
	 */
	public void removeTransition(TransitionObject trans) {
		transitions.remove(trans);
		List<TransitionObject> l = transitionFromStateMap.get(trans.getFromState());
		l.remove(trans);
		l = transitionToStateMap.get(trans.getToState());
		l.remove(trans);
		// Remove cached arrays.
		transitionArrayFromStateMap.remove(trans.getFromState());
		transitionArrayToStateMap.remove(trans.getToState());
		cachedTransitions = null;

		distributeTransitionEvent(new TransitionEvent(this, trans,
				false, false));
	}

	/**
	 * Creates a state, inserts it in this diagram, and returns that state.
	 * The ID for the state is set appropriately.
	 * 
	 * @param point
	 *            the point to put the state at
	 */
	public StateObject createState(Point point) {
		int i = 0;
		while (getStateWithID(i) != null)
			i++;
		StateObject state = new StateObject(i, point, this);
		addState(state);
		return state;
	}


	/**
	 * Adds a new state to this automata. Clients should use the <CODE>createState</CODE>
	 * method instead.
	 * 
	 * @param state
	 *            the state to add
	 */
	public void addState(StateObject state) {
		states.add(state);
		transitionFromStateMap.put(state, new LinkedList<TransitionObject>());
		transitionToStateMap.put(state, new LinkedList<TransitionObject>());
		cachedStates = null;
		distributeStateEvent(new StateEvent(this, state, true, false,
				false));
	}

	/**
	 * Removes a state from the diagram. This will also remove all transitions
	 * associated with this state.
	 * 
	 * @param state
	 *            the state to remove
	 */
	public void removeState(StateObject state) {
		TransitionObject[] t = getTransitionsFromState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		t = getTransitionsToState(state);
		for (int i = 0; i < t.length; i++)
			removeTransition(t[i]);
		distributeStateEvent(new StateEvent(this, state, false, false,
				false));
		states.remove(state);

		transitionFromStateMap.remove(state);
		transitionToStateMap.remove(state);

		transitionArrayFromStateMap.remove(state);
		transitionArrayToStateMap.remove(state);

		cachedStates = null;

	}

	/**
	 * Returns an array that contains every state in this diagram. The array
	 * is guaranteed to be in order of ascending state IDs.
	 * 
	 * @return an array containing all the states in this diagram
	 */
	public StateObject[] getStates() {
		if (cachedStates == null) {
			cachedStates = states.toArray(new StateObject[0]);
			Arrays.sort(cachedStates, new Comparator<StateObject>() {
				public int compare(StateObject o1, StateObject o2) {
					return o1.getID() - o2.getID();
				}

				public boolean equals(Object o) {
					return this == o;
				}
			});
		}
		return cachedStates;
	}
	
	public void selectStatesWithinBounds(Rectangle bounds){
		StateObject[] states = getStates();
		for(int k = 0; k < states.length; k++){
			states[k].setSelect(false);
			if(bounds.contains(states[k].getPoint())){	
				states[k].setSelect(true);
			}
		}
	}

	
	/**
	 * Returns the <CODE>State</CODE> in this diagram with this ID.
	 * 
	 * @param id
	 *            the ID to look for
	 * @return the instance of <CODE>State</CODE> in this diagram with this
	 *         ID, or <CODE>null</CODE> if no such state exists
	 */
	public StateObject getStateWithID(int id) {
		Iterator<StateObject> it = states.iterator();
		while (it.hasNext()) {
			StateObject state = it.next();
			if (state.getID() == id)
				return state;
		}
		return null;
	}


	/**
	 * Returns the particular class that added transition objects should be a
	 * part of. Subclasses may wish to override in case they want to restrict
	 * the type of transitions their diagram will respect. By default this
	 * method simply returns the class object for the abstract class <CODE>Transition</CODE>.
	 * 
	 * @see #addTransition
	 * @see TransitionObject.Transition
	 * @return the <CODE>Class</CODE> object that all added transitions should
	 *         derive from
	 */
	protected Class<StateTransition> getTransitionClass() {
        return TransitionObject.StateTransition.class;
	}

	/**
	 * Returns a string representation of this <CODE>Diagram</CODE>.
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(super.toString());
		buffer.append('\n');
		StateObject[] states = getStates();
		for (int s = 0; s < states.length; s++) {
			
			buffer.append(states[s]);
			buffer.append('\n');
			TransitionObject[] transitions = getTransitionsFromState(states[s]);
			for (int t = 0; t < transitions.length; t++) {
				buffer.append('\t');
				buffer.append(transitions[t]);
				buffer.append('\n');
			}
		}

		return buffer.toString();
	}

	/**
	 * Adds a <CODE>StateListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addStateListener(StateListener listener) {
		stateListeners.add(listener);
	}

	/**
	 * Adds a <CODE>TransitionListener</CODE> to this automata.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addTransitionListener(TransitionListener listener) {
		transitionListeners.add(listener);
	}



	/**
	 * Gives an automata state change event to all state listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeStateEvent(StateEvent event) {
		Iterator<StateListener> it = stateListeners.iterator();
		while (it.hasNext()) {
			StateListener listener = it.next();
			listener.diagramStateChange(event);
		}
	}


	/**
	 * Gives an automata transition change event to all transition listeners.
	 * 
	 * @param event
	 *            the event to distribute
	 */
	void distributeTransitionEvent(TransitionEvent event) {
		Iterator<TransitionListener> it = transitionListeners.iterator();
		while (it.hasNext()) {
			TransitionListener listener = it
					.next();
			listener.diagramTransitionChange(event);
		}
	}


	/**
	 * Gets the Environment Frame the diagram is in.
	 * @return the environment frame.
	 */
	public EnvironmentFrame getEnvironmentFrame() {
		return myEnvFrame;
	}

	/**
	 * Changes the environment frame this diagram is in.
	 * @param frame the environment frame
	 */
	public void setEnvironmentFrame(EnvironmentFrame frame) {
		myEnvFrame = frame;
	}
	
	public int hashCode(){
		int ret = 0;

		for (Object o:transitions)
			ret+=((TransitionObject) o).specialHash();

		return ret;
	}
	
	/**
	 * **get model used by Logisim from JFLAP model**
	 * 
	 * @return internal model
	 * @throws InvalidTransitionException
	 * @throws AbsentStateException
	 */
	public StateDiagram getAlternativeModel() throws InvalidTransitionException, AbsentStateException{
		StateDiagram toGet=new StateDiagram();
		StateObject[] states=this.getStates();
		for(StateObject s: states){
			String name=s.getName();
			int id=s.getID();
			toGet.addState(name, id);
		}
		TransitionObject[] trans=this.getTransitions();
		for(TransitionObject t: trans){
			int originID=t.getFromState().getID();
			int destinyID=t.getToState().getID();
			State origin=toGet.getState(originID);
			State destiny=toGet.getState(destinyID);
			String input=((StateTransition) t).getInput();
			String output=((StateTransition) t).getOutput();
			toGet.addTransition(origin, destiny, input, output);
		}
		
		return toGet;
		
	}
	
	public Diagram copy(){
	
		Diagram copiedDiagram = new Diagram();  
		copiedDiagram.setEnvironmentFrame(myEnvFrame);
		
		Map<StateObject, StateObject> stateMap = new HashMap<StateObject, StateObject>();
		//copy of states
		for (StateObject currentState : states){
			
			StateObject copiedState = currentState.copy();
			stateMap.put(currentState, copiedState);
			copiedDiagram.addState(copiedState);
		}

		//copy of transitions
		for(TransitionObject currentTransition : transitions){
			
			TransitionObject copiedTransition = currentTransition.copy();
			copiedTransition.setFromState(stateMap.get(currentTransition.getFromState()));
			copiedTransition.setToState(stateMap.get(currentTransition.getToState()));
			copiedDiagram.addTransition(copiedTransition);
			
		}
		
		return copiedDiagram;
		
	}

	private EnvironmentFrame myEnvFrame = null;

	/** The collection of states in this diagram. */
	protected Set<StateObject> states;

	/** The cached array of states. */
	private StateObject[] cachedStates = null;

	/** The cached array of transitions. */
	private TransitionObject[] cachedTransitions = null;

	/** The list of transitions in this diagram. */
	protected Set<TransitionObject> transitions;

	/**
	 * A mapping from states to a list holding transitions from those states.
	 */
	private HashMap<StateObject, LinkedList<TransitionObject>> transitionFromStateMap = new HashMap<StateObject, LinkedList<TransitionObject>>();

	/**
	 * A mapping from state to a list holding transitions to those states.
	 */
	private HashMap<StateObject, LinkedList<TransitionObject>> transitionToStateMap = new HashMap<StateObject, LinkedList<TransitionObject>>();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap<StateObject, TransitionObject[]> transitionArrayFromStateMap = new HashMap<StateObject, TransitionObject[]>();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap<StateObject, TransitionObject[]> transitionArrayToStateMap = new HashMap<StateObject, TransitionObject[]>();



	private transient HashSet<TransitionListener> transitionListeners = new HashSet<TransitionListener>();

	private transient HashSet<StateListener> stateListeners = new HashSet<StateListener>();

}
