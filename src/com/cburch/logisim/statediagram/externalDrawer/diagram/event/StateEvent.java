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





package com.cburch.logisim.statediagram.externalDrawer.diagram.event;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;

import java.util.EventObject;

/**
 * This event is given to listeners of an diagram interested in events when a
 * state on an diagram is added or removed, or moved, or it's label was
 * changed, etc.
 * 
 * @see diagram.Diagram
 * @see diagram.StateObject
 * @see diagram.Diagram#addState
 * @see diagram.Diagram#removeState
 * @see StateListener
 * 
 * @author Thomas Finley
 */

public class StateEvent extends EventObject {
	/**
	 * Instantiates a new <CODE>StateEvent</CODE>.
	 * 
	 * @param auto
	 *            the <CODE>Diagram</CODE> that generated the event
	 * @param add
	 *            <CODE>true</CODE> if state added
	 * @param move
	 *            <CODE>true</CODE> if the state was merely moved
	 * @param label
	 *            <CODE>true</CODE> if the state was only changed in such a
	 *            fashion as
	 */
	public StateEvent(Diagram auto, StateObject state, boolean add,
                      boolean move, boolean label) {
		super(auto);
		myState = state;
		myAdd = add;
		myMove = move;
		myLabel = label;
	}

	/**
	 * Returns the <CODE>State</CODE> that was added/removed.
	 * 
	 * @return the <CODE>State</CODE> that was added/removed
	 */
	public StateObject getState() {
		return myState;
	}

	/**
	 * Returns if this was an add.
	 * 
	 * @return <CODE>true</CODE> if this event indicates the addition of a
	 *         state, <CODE>false</CODE> otherwise
	 */
	public boolean isAdd() {
		return myAdd;
	}

	/**
	 * Returns if this was a move.
	 * 
	 * @return <CODE>true</CODE> if this event indicates the mere moving of a
	 *         state, <CODE>false</CODE> otherwise
	 */
	public boolean isMove() {
		return myMove;
	}


	/**
	 * Returns if this was a delete of a state.
	 * 
	 * @return <CODE>true</CODE> if this event was the deletion of a state,
	 *         false otherwise
	 */
	public boolean isDelete() {
		return !(myMove || myAdd || myLabel);
	}

	/** Was this an add? */
	private boolean myAdd;

	/** Was this a move? */
	private boolean myMove;

	/** Was the label for the state changed? */
	public boolean myLabel;

	/** Which state did we add/remove? */
	private StateObject myState;
}
