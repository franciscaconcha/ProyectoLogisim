package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagramSaver.*;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

public class OpenTool extends Tool{
	
	DiagramSaver saver;

	public OpenTool(DiagramPane view, DiagramDrawer drawer, DiagramSaver saver) {
		super(view, drawer);
		this.saver = saver;
	}
	

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		return "Open Diagram";
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the arrow tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/resources/ICON/open.gif");
		return new javax.swing.ImageIcon(url);
	}
	/**
	 * Returns the key stroke that will activate this tool.
	 * 
	 * @return the key stroke that will activate this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('o');
	}
	
	@Override
	public void select(EditCanvas view){
		
		//filename chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Open File");  
		
		//open a xml file
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", "xml", "xml");
		fileChooser.setFileFilter(filter);
		 
		//show dialog
		while (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
			
		    File fileToSave = fileChooser.getSelectedFile();

		    if(fileToSave.exists()){
		    	
			    Diagram newDiagram = saver.getSavedDiagram(fileToSave.getAbsolutePath());
			    getDiagram().transform(newDiagram);
			    
			    getView().repaint();
			    
			    break;
			    
		    }else{
		    	JOptionPane.showMessageDialog(new JFrame(), "The file does not exist. Choose a correct file."
		                ,"Error", JOptionPane.PLAIN_MESSAGE);
		    }
		    
		}
	}
}
