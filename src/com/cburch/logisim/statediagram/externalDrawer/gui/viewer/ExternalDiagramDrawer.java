package com.cburch.logisim.statediagram.externalDrawer.gui.viewer;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.gui.editor.EditorPane;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.DiagramEnvironment;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.Environment;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.EnvironmentFrame;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.tag.EditorTag;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.tag.PermanentTag;

import java.awt.*;

/**
 * Created by romina on 4/15/14.
 */
public class ExternalDiagramDrawer {
    static public void show(){

        Diagram aut = new Diagram();
        Environment env = new DiagramEnvironment(aut);
        EditorPane editor = new EditorPane(aut);

        env.add(editor, "State Diagram Drawer", new EditorPTag());

        EnvironmentFrame frame = new EnvironmentFrame(env);

        aut.setEnvironmentFrame(frame);

        frame.pack();

        int width = 600, height = 400;

        width = Math.max(width, frame.getSize().width);
        height = Math.max(height, frame.getSize().height);
        frame.setSize(new Dimension(width, height));
        frame.setVisible(true);
    }
    public static class EditorPTag implements EditorTag, PermanentTag {
    };

}