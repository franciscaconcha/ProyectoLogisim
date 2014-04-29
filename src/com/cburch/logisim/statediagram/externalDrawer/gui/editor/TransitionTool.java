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
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.DiagramEnvironment;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A tool that handles the creation of transitions.
 * 
 * @author Thomas Finley
 */

public class TransitionTool extends Tool {


	/**
	 * Instantiates a new transition tool. The transition creator is obtained
	 * from whatever is returned from the transition creator factory.
	 * 
	 * @see gui.editor.TransitionCreator#creatorForAutomaton
	 */
	public TransitionTool(DiagramPane view, DiagramDrawer drawer) {
		super(view, drawer);
		this.creator = TransitionCreator.creatorForAutomaton(getDiagram(),
				getView());
	}

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		return "Transition Creator";
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the transition tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/ICON/transition.gif");
		return new javax.swing.ImageIcon(url);
	}

	/**
	 * When the user presses the mouse, we detect if there is a state here. If
	 * there is, then this may be the start of a transition.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void mousePressed(MouseEvent event) {
	
		first = getDrawer().stateAtPoint(event.getPoint());
		if (first == null)
			return;
		hover = first.getPoint();
	}

	/**
	 * When the mouse is dragged someplace, updates the "hover" point so the
	 * line from the state to the mouse can be drawn.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void mouseDragged(MouseEvent event) {
		if (first == null)
			return;
		hover = event.getPoint();
		getView().repaint();
	}

	/**
	 * When we release the mouse, a transition from the start state to this
	 * released state must be formed.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void mouseReleased(MouseEvent event) {
		// Did we even start at a state?
		if (first == null)
			return;
		StateObject state = getDrawer().stateAtPoint(event.getPoint());
		if (state != null) {
		    creator.createTransition(first, state);
		}
		first = null;
		getView().repaint();
	}

	/**
	 * Draws the line from the first clicked state to the drag point, if indeed
	 * we are even in a drag.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	public void draw(Graphics g) {
		if (first == null)
			return;
		java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
		Stroke s = g2.getStroke();
		g2.setStroke(STROKE);
		g2.setColor(COLOR);
		g2.drawLine(first.getPoint().x, first.getPoint().y, hover.x, hover.y);
		g2.setStroke(s);
	}

	/**
	 * Returns the keystroke to switch to this tool, the T key.
	 * 
	 * @return the keystroke to switch to this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('t');
	}

	/** The first clicked state. */
	protected StateObject first;

	/** The point over which we are hovering. */
	protected Point hover;

	/** The transition creator. */
	protected TransitionCreator creator;

	/** The stroke object that draws the lines. */
	private static Stroke STROKE = new java.awt.BasicStroke(2.4f);

	/** The color for the line. */
	private static java.awt.Color COLOR = new java.awt.Color(.5f, .5f, .5f, .5f);
}
