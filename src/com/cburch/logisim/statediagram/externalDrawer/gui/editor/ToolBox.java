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
 * A <CODE>ToolBox</CODE> is an object used for defining what tools are in a
 * <CODE>ToolBar</CODE> object.
 * 
 * @see gui.editor.ToolBar
 * @see gui.editor.Tool
 * 
 * @author Thomas Finley
 */

public interface ToolBox {
	/**
	 * Returns a list of tools in the order they should be in the tool bar.
	 * 
	 * @param view
	 *            the view that the diagram will be drawn in
	 * @param drawer
	 *            the diagram drawer for the view
	 */
	public List tools(DiagramPane view, DiagramDrawer drawer);
}
