/*
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

import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateListener;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionListener;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;


/**
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

public class Diagram implements Serializable, Cloneable {
	/**
	 * Creates an instance of <CODE>Diagram</CODE>. The created instance
	 * has no states and no transitions.
	 */
	public Diagram() {
		states = new HashSet();
		transitions = new HashSet();
	}

	/**
	 * Creates a clone of this diagram.
	 * 
	 * @return a clone of this diagram, or <CODE>null</CODE> if the clone
	 *         failed
	 */
	public Object clone() {
		Diagram a;
		// Try to create a new object.
		try {
				a = (Diagram) getClass().newInstance();
		} catch (Throwable e) {
			// Well golly, we're sure screwed now!
			System.err.println("Warning: clone of diagram failed!");
			return null;
		}
		a.setEnvironmentFrame(this.getEnvironmentFrame());
		
		
		// Copy over the states.
		HashMap map = new HashMap(); // Old states to new states.
		Iterator it = states.iterator();
		while (it.hasNext()) {

			StateObject state = (StateObject) it.next();
			StateObject nstate = new StateObject(state.getID(),
					new Point(state.getPoint()), a);
			nstate.setLabel(state.getLabel());
			nstate.setName(state.getName());
			map.put(state, nstate);
			a.addState(nstate);
		}
		it = states.iterator();
		while (it.hasNext()) {
			StateObject state = (StateObject) it.next();
			TransitionObject[] ts = getTransitionsFromState(state);
			StateObject from = (StateObject) map.get(state);
			for (int i = 0; i < ts.length; i++) {
				StateObject to = (StateObject) map.get(ts[i].getToState());
                TransitionObject toBeAdded = (TransitionObject) ts[i].clone(); //call clone instead of copy so that the gui stuff can get appropriately updated
                toBeAdded.setFromState(from);
                toBeAdded.setToState(to);
//				a.addTransition(ts[i].copy(from, to));
				a.addTransition(toBeAdded);
			}
		}

		// Should be done now!
		return a;
	}
	


	/**
	 * Retrieves all transitions that eminate from a state.
	 * 
	 * @param from
	 *            the <CODE>State</CODE> from which returned transitions
	 *            should come from
	 * @return an array of the <CODE>Transition</CODE> objects emanating from
	 *         this state
	 */
	public TransitionObject[] getTransitionsFromState(StateObject from) {
		TransitionObject[] toReturn = (TransitionObject[]) transitionArrayFromStateMap
				.get(from);
		if (toReturn == null) {
			List list = (List) transitionFromStateMap.get(from);
			toReturn = (TransitionObject[]) list.toArray(new TransitionObject[0]);
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
		TransitionObject[] toReturn = (TransitionObject[]) transitionArrayToStateMap
				.get(to);
		if (toReturn == null) {
			List list = (List) transitionToStateMap.get(to);
			toReturn = (TransitionObject[]) list.toArray(new TransitionObject[0]);
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
		ArrayList list = new ArrayList();
		for (int i = 0; i < t.length; i++)
			if (t[i].getToState() == to)
				list.add(t[i]);
		return (TransitionObject[]) list.toArray(new TransitionObject[0]);
	}

	/**
	 * Retrieves all transitions.
	 * 
	 * @return an array containing all transitions for this diagram
	 */
	public TransitionObject[] getTransitions() {
		if (cachedTransitions == null)
			cachedTransitions = (TransitionObject[]) transitions
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
        if(transitionFromStateMap == null) transitionFromStateMap = new HashMap();
		List list = (List) transitionFromStateMap.get(trans.getFromState());
		list.add(trans);
        if(transitionToStateMap == null) transitionToStateMap = new HashMap();
		list = (List)transitionToStateMap.get(trans.getToState()) ;
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
		List list = (List) transitionFromStateMap.get(oldTrans.getFromState());
		list.set(list.indexOf(oldTrans), newTrans);
		list = (List) transitionToStateMap.get(oldTrans.getToState());
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
		List l = (List) transitionFromStateMap.get(trans.getFromState());
		l.remove(trans);
		l = (List) transitionToStateMap.get(trans.getToState());
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
	protected final void addState(StateObject state) {
		states.add(state);
		transitionFromStateMap.put(state, new LinkedList());
		transitionToStateMap.put(state, new LinkedList());
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
	 * is gauranteed to be in order of ascending state IDs.
	 * 
	 * @return an array containing all the states in this diagram
	 */
	public StateObject[] getStates() {
		if (cachedStates == null) {
			cachedStates = (StateObject[]) states.toArray(new StateObject[0]);
			Arrays.sort(cachedStates, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((StateObject) o1).getID() - ((StateObject) o2).getID();
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
		Iterator it = states.iterator();
		while (it.hasNext()) {
			StateObject state = (StateObject) it.next();
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
	protected Class getTransitionClass() {
        return TransitionObject.PDATransition.class;
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
		Iterator it = stateListeners.iterator();
		while (it.hasNext()) {
			StateListener listener = (StateListener) it.next();
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
		Iterator it = transitionListeners.iterator();
		while (it.hasNext()) {
			TransitionListener listener = (TransitionListener) it
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

	private EnvironmentFrame myEnvFrame = null;

	/** The collection of states in this diagram. */
	protected Set states;

	/** The cached array of states. */
	private StateObject[] cachedStates = null;

	/** The cached array of transitions. */
	private TransitionObject[] cachedTransitions = null;

	/** The list of transitions in this diagram. */
	protected Set transitions;

	/**
	 * A mapping from states to a list holding transitions from those states.
	 */
	private HashMap transitionFromStateMap = new HashMap();

	/**
	 * A mapping from state to a list holding transitions to those states.
	 */
	private HashMap transitionToStateMap = new HashMap();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayFromStateMap = new HashMap();

	/**
	 * A mapping from states to an array holding transitions from a state. This
	 * is a sort of cashing.
	 */
	private HashMap transitionArrayToStateMap = new HashMap();



	private transient HashSet transitionListeners = new HashSet();

	private transient HashSet stateListeners = new HashSet();

}
