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





package com.cburch.logisim.statediagram.externalDrawer.gui.environment;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateListener;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionListener;
import com.cburch.logisim.statediagram.externalDrawer.gui.editor.UndoKeeper;

public class DiagramEnvironment extends Environment {
	/**
	 * Instantiates an <CODE>DiagramEnvironment</CODE> for the given
	 * diagram. By default this method will set up an environment with an
	 * editor pane for this diagram.
	 * 
	 * @param diagram
	 *            the diagram to set up an environment for
	 * @see gui.editor.EditorPane
	 */
	public DiagramEnvironment(Diagram diagram) {
		super(diagram);
		Listener listener = new Listener();

		initUndoKeeper();
	}

	/**
	 * Returns the diagram that this environment manages.
	 * 
	 * @return the diagram that this environment manages
	 */
	public Diagram getAutomaton() {
		return (Diagram) super.getObject();
	}
	
	/*Start undo methods*/
   /* public UndoKeeper getUndoKeeper(){
        return myKeeper;	
    }*/
    public void initUndoKeeper(){
        myKeeper = new UndoKeeper(getAutomaton());
    }
    public void saveStatus(){
        myKeeper.saveStatus();	
    }
    
    public boolean shouldPaint(){
        return myKeeper == null ? true: !myKeeper.sensitive;
    }
	
	private UndoKeeper myKeeper;
	/**
	 * The transition and state listener for an diagram detects if there are
	 * changes in the environment, and if so, sets the dirty bit.
	 */
	private class Listener implements StateListener,
            TransitionListener {
		public void diagramTransitionChange(TransitionEvent e) {
			//setDirty();
		}

		public void diagramStateChange(StateEvent e) {
			//setDirty();
		}


	}
}
