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





package com.cburch.logisim.statediagram.externalDrawer.gui.viewer;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * An extension of the <CODE>DiagramDrawer</CODE> that allows the selection
 * (i.e. highlighting) of states.
 * 
 * @author Thomas Finley
 */

public class SelectionDrawer extends DiagramDrawer {
	/**
	 * Instantiates a new selection drawer with no states selected.
	 * 
	 * @param diagram
	 *            the diagram to select
	 */
	public SelectionDrawer(Diagram diagram) {
		super(diagram);
	}

	/**
	 * Listens for changes in the states of the diagram. In the event that one
	 * has it checks the selected states.
	 * 
	 * @param event
	 *            the state event
	 */
	protected void stateChange(StateEvent event) {
		if (event.isDelete())
			selected.remove(event.getState());
		super.stateChange(event);
	}

	/**
	 * If a state is selected, draw it somewhat darker than the others. If it is
	 * not, then simply use the regular means for drawing a state.
	 * 
	 * @param g
	 *            the graphics object to draw on
	 * @param state
	 *            the state to draw
	 */
	public void drawState(Graphics g, StateObject state) {
		if (selected.contains(state)) {
			getStateDrawer().drawState(g, getDiagram(), state,
					state.getPoint(), SELECTED_COLOR);
			if (doesDrawStateLabels())
				getStateDrawer().drawStateLabel(g, state, state.getPoint(),
						StateDrawer.STATE_COLOR);
		} else
			super.drawState(g, state);
	}

	/**
	 * Draws the transitions normally, then draws the highlight for the selected
	 * transitions.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	protected void drawTransitions(Graphics g) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		super.drawTransitions(g);
		Iterator it = selectedTransitions.iterator();
		while (it.hasNext()) {
			TransitionObject t = (TransitionObject) it.next();
			try {
				arrowForTransition(t).drawHighlight(g2);
			} catch (NullPointerException e) {
				// Then this transition isn't in here.
			}
		}
	}

	/**
	 * Retrieves the set of selected states.
	 * 
	 * @return the set of selected states
	 */
	protected Set selected() {
		return selected;
	}


	/** The set of selected states, and the set of selected transitions. */
	private Set selected = new HashSet(), selectedTransitions = new HashSet();

	/** The color to draw selected states in. */
	protected static final Color SELECTED_COLOR = StateDrawer.STATE_COLOR
			.darker().darker();

	/** This set of listeners. */
	private Set listeners = new HashSet();
}
