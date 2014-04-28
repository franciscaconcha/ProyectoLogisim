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
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import java.util.List;

/**
 * The <CODE>DefaultToolBox</CODE> has all the tools for general editing of an
 * diagram.
 */

public class DefaultToolBox implements ToolBox {
	/**
	 * Returns a list of tools including a <CODE>ArrowTool</CODE>, <CODE>StateTool</CODE>,
	 * <CODE>TransitionTool</CODE> and <CODE>DeleteTool</CODE>, in that
	 * order.
	 * 
	 * @param view
	 *            the component that the diagram will be drawn in
	 * @param drawer
	 *            the drawer that will draw the diagram in the view
	 * @return a list of <CODE>Tool</CODE> objects.
	 */
	public List tools(DiagramPane view, DiagramDrawer drawer) {

		List list = new java.util.ArrayList();
		list.add(new ArrowTool(view, drawer));
		list.add(new StateTool(view, drawer));
		list.add(new TransitionTool(view, drawer));
		list.add(new DeleteTool(view, drawer));

		return list;
	}
}
