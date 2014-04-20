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

import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A tool bar for editing and manipulating an diagram.
 * 
 * @author Thomas Finley
 */

public class ToolBar extends JToolBar implements ActionListener {
	/**
	 * Instantiates a new tool bar.
	 * 
	 * @param view
	 *            the view the diagram is displayed in
	 * @param drawer
	 *            the diagram drawer
	 * @param box
	 *            the toolbox to get the initial tools to put in the bar
	 */
	public ToolBar(EditCanvas view, DiagramDrawer drawer, ToolBox box) {
		super();
		adapter = new ToolAdapter(view);
		this.view = view;
		this.drawer = drawer;
		tools = box.tools(view, drawer);
		initBar();
		view.addMouseListener(adapter);
		view.addMouseMotionListener(adapter);
	}

	/**
	 * Returns the view that the diagram is drawn in.
	 * 
	 * @return the view that the diagram is drawn in
	 */
	protected Component getView() {
		return view;
	}

	/**
	 * Returns the diagram drawer for the diagram.
	 * 
	 * @return the diagram drawer for the diagram
	 */
	protected DiagramDrawer getDrawer() {
		return drawer;
	}

	/**
	 * Initializes the tool bar.
	 */
	private void initBar() {
		ButtonGroup group = new ButtonGroup();
		JToggleButton button = null;
		Iterator it = tools.iterator();
		KeyStroke key;

		while (it.hasNext()) {
			Tool tool = (Tool) it.next();
			button = new JToggleButton(tool.getIcon());
			buttonsToTools.put(button, tool);
			button.setToolTipText(tool.getShortcutToolTip());
			group.add(button);
			this.add(button);
			button.addActionListener(this);
			key = tool.getKey();
			if (key == null)
				continue;
			InputMap imap = button
					.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
			ActionMap amap = button.getActionMap();
			Object o = new Object();
			imap.put(key, o);
			amap.put(o, new ButtonClicker(button));
		}
	}

	/**
	 * If a tool is clicked, sets the new current tool.
	 */
	public void actionPerformed(ActionEvent e) {
		Tool tool = (Tool) buttonsToTools.get(e.getSource());
		if (tool != null) {
			adapter.setAdapter(tool);
			currentTool = tool;
			view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		if(tool instanceof DeleteTool){
			   Toolkit toolkit = Toolkit.getDefaultToolkit();  
			   //Image image = toolkit.getImage("/JFLAP09CVS/ICON/deletecursor.gif");   
			   URL url = getClass().getResource("/ICON/deletecursor.gif");
			   Image image = getToolkit().getImage(url);
			   Point hotSpot = new Point(5,5);  
			   Cursor cursor = toolkit.createCustomCursor(image, hotSpot, "Delete");  
			   view.setCursor(cursor);

		}
	}

	/**
	 * Draws the tool view.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	public void drawTool(Graphics g) {
		if (currentTool == null)
			return;
		currentTool.draw(g);
	}

	/**
	 * The action that clicks a button.
	 */
	private class ButtonClicker extends AbstractAction {
		public ButtonClicker(AbstractButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			button.doClick();
		}

		AbstractButton button;
	}

	private Component view;

	private DiagramDrawer drawer;

	private List tools;

	private HashMap buttonsToTools = new HashMap();

	private ToolAdapter adapter;

	private Tool currentTool = null;
}
