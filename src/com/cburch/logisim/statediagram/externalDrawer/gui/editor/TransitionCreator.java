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





package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.*;
import java.awt.*;

/**
 * A <CODE>TransitionCreator</CODE> object is used to present a graphical
 * environment for the creation and editing of transitions for insertion into
 * automata.
 * 
 * @author Thomas Finley
 */

public abstract class TransitionCreator {
	public TransitionCreator() {

	}

	public TransitionCreator(DiagramPane parent) {
		this.parent = parent;
	}

	/**
	 * Returns the diagram.
	 * 
	 * @return the diagram
	 */
	protected Diagram getAutomaton() {
		return getParent().getDrawer().getDiagram();
	}

	/**
	 * Begins the process of creating a transition and returns it.
	 * 
	 * @param from
	 *            the state the transition will go from
	 * @param to
	 *            the state the transition will go to
	 */
	public abstract TransitionObject createTransition(StateObject from, StateObject to);

	/**
	 * Edits a given transition. Ideally this should use the same interface as
	 * that given by <CODE>createTransition</CODE>.
	 * 
	 * @param transition
	 *            the transition to edit
	 * @return <CODE>false</CODE> if the user decided to edit a transition
	 */
	public abstract boolean editTransition(TransitionObject transition);

	/**
	 * This is a static method used to return a transition creator for the sort
	 * of diagram that is being edited.
	 * 
	 * @param diagram
	 *            the diagram for which there will be created a compatible
	 *            transition creator
	 * @param parent
	 *            the component that should be the parent of any dialog boxes or
	 *            other windows brought up
	 * @return a transition creator that generates transitions compatible with
	 *         the type of diagram passed in, or <CODE>null</CODE> if this
	 *         type of diagram is unknown
	 */
	public static TransitionCreator creatorForDiagram(Diagram diagram,
			DiagramPane parent) {

			return new StateTransitionCreator(parent);

	}

	/**
	 * Edits the transition at the particular point. By default, this envokes
	 * the pointless method.
	 * 
	 * @param transition
	 *            the transition to edit
	 * @param point
	 *            the point to edit the transition at
	 */
	public void editTransition(TransitionObject transition, Point point) {
		editTransition(transition);
	}

	/**
	 * Returns the parent component of this transition creator.
	 * 
	 * @return the parent component of this transition creator
	 */
	public DiagramPane getParent() {
		return parent;
	}

	/**
	 * Reports an error to the user through a dialog box based on an illegal
	 * argument exception.
	 * 
	 * @param e
	 *            the illegal argument exception
	 */
	public void reportException(IllegalArgumentException e) {
		JOptionPane.showMessageDialog(getParent(), "Bad format!\n"
				+ e.getMessage(), "Bad Format", JOptionPane.ERROR_MESSAGE);
	}

	/** The parent component for this transition creator. */
	private DiagramPane parent = null;
}
