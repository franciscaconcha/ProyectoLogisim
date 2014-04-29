package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;

import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

/**
 * The arrow tool is used mostly for editing existing objects.
 * 
 * @author Thomas Finley, Henry Qin
 */

public class GenerateCircuitTool extends Tool {


	/**
	 * Instantiates a new arrow tool.
	 * 
	 * @param view
	 *            the view where the diagram is drawn
	 * @param drawer
	 *            the object that draws the diagram
	 */
	public GenerateCircuitTool(DiagramPane view, DiagramDrawer drawer) {
		super(view, drawer);
	}

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		
		return "Generate Circuit";
		
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the arrow tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/resources/ICON/export-green.gif");
		return new javax.swing.ImageIcon(url);
	}


	/**
	 * Returns the key stroke that will activate this tool.
	 * 
	 * @return the key stroke that will activate this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('g');
	}
	
	@Override
	public void select(Component view){
		//TODO
		System.out.println("Genetaring :D");
		((EditCanvas)view).getFrame().dispose();
	}

}
