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

import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject.StateTransition;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * This is the creator of transitions in push down automata.
 * 
 * @author Thomas Finley
 */

public class StateTransitionCreator extends TableTransitionCreator {
	/**
	 * Instantiates a new <CODE>PDATransitionCreator</CODE>.
	 * 
	 * @param parent
	 *            the parent of whatever dialogs/windows get brought up by this
	 *            creator
	 */
	public StateTransitionCreator(DiagramPane parent) {
		super(parent);
	}

	/**
	 * Initializes a new empty transition.
	 * 
	 * @param from
	 *            the from state
	 * @param to
	 *            to too state
	 */
	protected TransitionObject initTransition(StateObject from, StateObject to) {
		return new TransitionObject.StateTransition(from, to, "*", "*");
	}

	/**
	 * Creates a new table model.
	 * 
	 * @param transition
	 *            the transition to create the model for
	 * @return a table model for the transition
	 */
	protected TableModel createModel(TransitionObject transition) {
		final TransitionObject.StateTransition t = (TransitionObject.StateTransition) transition;
		return new AbstractTableModel() {
			public Object getValueAt(int row, int column) {
				return s[column];
			}

			public void setValueAt(Object o, int r, int c) {
				s[c] = (String) o;
			}

			public boolean isCellEditable(int r, int c) {
				return true;
			}

			public int getRowCount() {
				return 1;
			}

			public int getColumnCount() {
				return 2;
			}

			public String getColumnName(int c) {
				return NAME[c];
			}

			String s[] = new String[] { t.getInput(), t.getOutput()};
		};
	}

	private static final String NAME[] = { "Input","Output" };

	/**
	 * Modifies a transition according to what's in the table.
	 */
	public TransitionObject modifyTransition(TransitionObject transition, TableModel model) {
		String input = (String) model.getValueAt(0, 0);
		String output = (String) model.getValueAt(0, 1);
		StateTransition t = (StateTransition) transition;
		try {
			return new StateTransition(t.getFromState(), t.getToState(), input,output);
		} catch (IllegalArgumentException e) {
			reportException(e);
			return null;
		}
	}
}
