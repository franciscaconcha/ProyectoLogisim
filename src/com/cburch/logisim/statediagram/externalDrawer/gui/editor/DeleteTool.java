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
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.DiagramEnvironment;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.*;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.net.URL;
/**
 * A tool that handles the deletion of states and transitions.
 * 
 * @author Thomas Finley
 */

public class DeleteTool extends Tool {
	/**
	 * Instantiates a new delete tool.
	 */
	public DeleteTool(DiagramPane view, DiagramDrawer drawer) {
		super(view, drawer);
	}

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		return "Deleter";
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the delete tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/ICON/delete.gif");
		return new javax.swing.ImageIcon(url);
	}

	/**
	 * Returns the key stroke to switch to this tool, the D key.
	 * 
	 * @return the key stroke to switch to this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('d');
	}

	/**
	 * When the user clicks, we delete either the state or, if no state, the
	 * transition found at this point. If there's nothing at this point, nothing
	 * happens.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void mouseClicked(MouseEvent event) {
		
		StateObject state = getDrawer().stateAtPoint(event.getPoint());
		if (state != null) {
			getDrawer().saveHistory();
			getDiagram().removeState(state);
			getView().repaint();
			return;
		}
		TransitionObject trans = getDrawer().transitionAtPoint(event.getPoint());
		if (trans != null) {
			getDiagram().removeTransition(trans);
			getView().repaint();
		}
	}
	@Override
	public void select(EditCanvas view){
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
	    URL url = getClass().getResource("/ICON/deletecursor.gif");
	    Image image = toolkit.getImage(url);
	    Point hotSpot = new Point(5,5);  
	    Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Delete");  
	    view.setCursor(cursor);
	}
}
