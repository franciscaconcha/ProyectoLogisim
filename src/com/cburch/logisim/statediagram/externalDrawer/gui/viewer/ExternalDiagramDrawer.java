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
	
	static CircuitGenerator circuitGenerator;
	
	public ExternalDiagramDrawer(CircuitGenerator circuitGenerator){
		
		this.circuitGenerator = circuitGenerator;
		
	}
	
    public static void show(){

        Diagram aut = new Diagram();
        Environment env = new DiagramEnvironment(aut);
        EditorPane editor = new EditorPane(new SelectionDrawer(aut), new DefaultToolBox(),false,circuitGenerator);
        
        env.add(editor, "State Diagram Drawer");

        EnvironmentFrame frame = new EnvironmentFrame(env);
        editor.setFrame(frame);

        aut.setEnvironmentFrame(frame);

        frame.pack();
        int width = 600, height = 400;
        
        width = Math.max(width, frame.getSize().width);
        height = Math.max(height, frame.getSize().height);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
    }
 //   public static class EditorPTag implements EditorTag, PermanentTag {
  //  };

}
