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

import com.cburch.logisim.statediagram.externalDrawer.gui.environment.DiagramEnvironment;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import java.awt.*;

/**
 * An <CODE>EditCanvas</CODE> is an extension of <CODE>DiagramPane</CODE>
 * more suitable for use as a place where automatons may be edited.
 * 
 * @author Thomas Finley
 */

public class EditCanvas extends DiagramPane {


	/**
	 * Instantiates a new <CODE>EditCanvas</CODE>.
	 * 
	 * @param drawer
	 *            the diagram drawer
	 * @param fit
	 *            <CODE>true</CODE> if the diagram should change its size to
	 *            fit in the diagram; this can be very annoying
	 */
	public EditCanvas(DiagramDrawer drawer, boolean fit) {
		super(drawer, fit);
	}

	/**
	 * Sets the toolbar for this edit canvas.
	 * 
	 * @param toolbar
	 *            the toolbar for this edit canvas
	 */
	public void setToolBar(ToolBar toolbar) {
		this.toolbar = toolbar;
	}
	

	

	/**
	 * Paints the component. In addition to what the diagram pane does, this
	 * also calls the current tool's draw method.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		toolbar.drawTool(g);
		Graphics2D g2 = (Graphics2D) g;
		double newXScale = 1.0/transform.getScaleX();
		double newYScale = 1.0/transform.getScaleY();
		g2.scale(newXScale, newYScale);
		g2.translate(-transform.getTranslateX(), -transform.getTranslateY());
	}

	public EnvironmentFrame getFrame() {
		return frame;
	}

	public void setFrame(EnvironmentFrame frame) {
		this.frame = frame;
	}

	/** The toolbar that is used for this edit canvas. */
	private ToolBar toolbar;
	
	/**The frame where de canvas is*/
	private EnvironmentFrame frame;
	
}
