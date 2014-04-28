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

import java.util.EventListener;

/**
 * An interface that those interested in changes in transitions in an automata
 * should listen to.
 * 
 * @see TransitionEvent
 * @see diagram.Diagram#addTransitionListener
 */

public interface TransitionListener extends EventListener {
	/**
	 * Registers with the listener that an event has occurred.
	 * 
	 * @param event
	 *            the event
	 */
	public void diagramTransitionChange(TransitionEvent event);
}
