package com.cburch.logisim.statediagram.externalDrawer.gui.viewer;

import com.cburch.logisim.statediagram.externalDrawer.CircuitGenerator;
import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.gui.editor.DefaultToolBox;
import com.cburch.logisim.statediagram.externalDrawer.gui.editor.EditorPane;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.DiagramEnvironment;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.Environment;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;

import java.awt.*;

/**
 * Created by romina on 4/15/14.
 */
public class ExternalDiagramDrawer {

	private CircuitGenerator circuitGenerator;
	private Diagram diagram;
	private EnvironmentFrame frame;
	
	public ExternalDiagramDrawer(CircuitGenerator circuitGenerator){
		
		this.circuitGenerator = circuitGenerator;
		diagram = new Diagram();
        Environment env = new DiagramEnvironment(diagram);
        EditorPane editor = new EditorPane(new SelectionDrawer(diagram, this), new DefaultToolBox(),false,circuitGenerator);
        
        env.add(editor, "State Diagram Drawer");

        frame = new EnvironmentFrame(env);
        editor.setFrame(frame);

        diagram.setEnvironmentFrame(frame);

        frame.pack();
		
	}
	
    public void show(){
    	
		int width = 600, height = 400;
		        
        width = Math.max(width, frame.getSize().width);
        height = Math.max(height, frame.getSize().height);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);      
        
    }

	/**
	 * @return the circuitGenerator
	 */
	public CircuitGenerator getCircuitGenerator() {
		return circuitGenerator;
	}

	/**
	 * @param circuitGenerator the circuitGenerator to set
	 */
	public void setCircuitGenerator(CircuitGenerator circuitGenerator) {
		this.circuitGenerator = circuitGenerator;
	}

	/**
	 * @return the diagram
	 */
	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * @param diagram the diagram to set
	 */
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}

	/**
	 * @return the frame
	 */
	public EnvironmentFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(EnvironmentFrame frame) {
		this.frame = frame;
	}

}
