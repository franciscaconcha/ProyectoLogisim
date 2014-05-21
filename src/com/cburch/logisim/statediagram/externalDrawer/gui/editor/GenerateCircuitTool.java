package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;
import com.cburch.logisim.statediagram.externalDrawer.CircuitGenerator;

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
	public void select(EditCanvas view){
		//TODO 
		view.getCircuitGenerator().generate();
		
		view.getFrame().dispose();
	}

}
