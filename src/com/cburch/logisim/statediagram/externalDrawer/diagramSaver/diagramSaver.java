package com.cburch.logisim.statediagram.externalDrawer.diagramSaver;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
/**
 * That interface must be implemented by the classes that save diagrams and allows get saved diagrams.
 * @author Romina
 *
 */
public interface diagramSaver {
	
	/**
	 * Save diagram into filename
	 * @param diagram
	 * @param filename
	 */
	public void saveDiagram(Diagram diagram, String filename);
	/**
	 * Get diagram from filename
	 * @param filename
	 * @return
	 */
	public Diagram getSavedDiagram(String filename);

}
