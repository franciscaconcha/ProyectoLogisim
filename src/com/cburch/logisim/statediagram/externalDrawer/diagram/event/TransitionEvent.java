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
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;

import java.util.EventObject;

/**
 * This event is given to listeners of an diagram interested in events when a
 * transition on an diagram is added, removed, or changed.
 * 
 * @see diagram.Diagram
 * @see diagram.TransitionObject
 * @see diagram.Diagram#addTransition
 * @see diagram.Diagram#removeTransition
 * @see TransitionListener
 * 
 * @author Thomas Finley
 */

public class TransitionEvent extends EventObject {
	/**
	 * Instantiates a new <CODE>StateEvent</CODE>.
	 * 
	 * @param auto
	 *            the <CODE>Diagram</CODE> that generated the event
	 * @param transition
	 *            the <CODE>Transition</CODE> that was added or removed
	 * @param add
	 *            <CODE>true</CODE> if the transition is added, <CODE>false</CODE>
	 *            if removed
	 * @param change
	 *            <CODE>true</CODE> if some property of the transition was
	 *            changed, <CODE>false</CODE> if this is not a simple change
	 */
	public TransitionEvent(Diagram auto, TransitionObject transition,
                           boolean add, boolean change) {
		super(auto);
		myTransition = transition;
		myAdd = add;
	}

	/**
	 * Returns the <CODE>Transition</CODE> that was added/removed.
	 * 
	 * @return the <CODE>Transition</CODE> that was added/removed
	 */
	public TransitionObject getTransition() {
		return myTransition;
	}

	/**
	 * Returns if this was an add.
	 * 
	 * @return <CODE>true</CODE> if this event indicates the addition of a
	 *         transition, <CODE>false</CODE> otherwise
	 */
	public boolean isAdd() {
		return myAdd;
	}


	/** Was this an add? */
	private boolean myAdd;

	/** Which transition did we add/remove? */
	private TransitionObject myTransition;

}
