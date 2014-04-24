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
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.SelectionDrawer;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
//import automata.Note;

/**
 * This is a view that holds a tool bar and the canvas where the diagram is
 * displayed.
 * 
 * @author Thomas Finley
 */

public class EditorPane extends JComponent {
	/**
	 * Instantiates a new editor pane for the given diagram.
	 * 
	 * @param diagram
	 *            the diagram to create the editor pane for
	 */
	public EditorPane(Diagram diagram) {
		this(new SelectionDrawer(diagram));
	}

	/**
	 * Instantiates a new editor pane with a given diagram drawer.
	 * 
	 * @param drawer
	 *            the special diagram drawer for this editor
	 */
	public EditorPane(DiagramDrawer drawer) {
		this(drawer, new DefaultToolBox());
	}

	/**
	 * Instantiates a new editor pane with a given diagram drawer.
	 * 
	 * @param drawer
	 *            the special diagram drawer for this editor
	 * @param box
	 *            the tool box to get the tools from
	 */
	public EditorPane(DiagramDrawer drawer, ToolBox box) {
		this(drawer, box, false);
	}

	/**
	 * Instantiates a new editor pane with a given diagram drawer.
	 * 
	 * @param drawer
	 *            the special diagram drawer for this editor
	 * @param box
	 *            the tool box to get teh tools from
	 * @param fit
	 *            <CODE>true</CODE> if the editor should resize its view to
	 *            fit the diagram; note that this can be <I>very</I> annoying
	 *            if the diagram changes
	 */
	public EditorPane(DiagramDrawer drawer, ToolBox box, boolean fit) {
		pane = new EditCanvas(drawer, fit);
		pane.setCreator(this);
		this.drawer = drawer;
		this.diagram = drawer.getDiagram();
		this.setLayout(new BorderLayout());

		JPanel superpane = new JPanel();
		superpane.setLayout(new BorderLayout());
		superpane.add(new JScrollPane(pane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
		superpane.setBorder(new BevelBorder(BevelBorder.LOWERED));

		toolbar = new ToolBar(pane, drawer, box);
		pane.setToolBar(toolbar);

		this.add(superpane, BorderLayout.CENTER);
		this.add(toolbar, BorderLayout.NORTH);
		this.add(new AutomatonSizeSlider(pane, drawer), BorderLayout.SOUTH);

	}

	/**
	 * Returns the diagram drawer for the editor pane canvas.
	 * 
	 * @return the drawer that draws the diagram being edited
	 */
	public DiagramDrawer getDrawer() {
		return pane.getDrawer();
	}

	/**
	 * Prints this component. This will print only the diagram section of the
	 * component.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	public void printComponent(Graphics g) {
		pane.print(g);
	}

	/**
	 * Children are not painted here.
	 * 
	 * @param g
	 *            the graphics object to paint to
	 */
	public void printChildren(Graphics g) {

	}

	/**
	 * Returns the diagram pane.
	 * 
	 * @return the diagram pane
	 */
	public Diagram getDiagram() {
		return diagram;
	}


	public EnvironmentFrame getFrame() {
		return frame;
	}

	public void setFrame(EnvironmentFrame frame) {
		this.frame = frame;
		//propagation
		pane.setFrame(frame);
	}


	/** The diagram. */
	protected Diagram diagram;

	/** The diagram drawer. */
	protected DiagramDrawer drawer;

	/** The diagram pane. */
	protected EditCanvas pane;

	/** The tool bar. */
	protected ToolBar toolbar;
	
	/**The frame where the editor is*/
	private EnvironmentFrame frame;

}
