package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cburch.logisim.statediagram.externalDrawer.diagramSaver.*;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

public class SaveTool extends Tool{
	
	DiagramSaver saver;

	public SaveTool(DiagramPane view, DiagramDrawer drawer, DiagramSaver saver) {
		super(view, drawer);
		this.saver=saver;
	}
	

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		return "Save Diagram";
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the arrow tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/resources/ICON/save.gif");
		return new javax.swing.ImageIcon(url);
	}

	/**
	 * Returns the key stroke that will activate this tool.
	 * 
	 * @return the key stroke that will activate this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('x');
	}
	
	@Override
	public void select(EditCanvas view){
		
		//filename chooser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save File");  
		
		//save as a xml file
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML FILES", "xml", "xml");
		fileChooser.setFileFilter(filter);
		 
		//show dialog
		while (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
			
		    File fileToSave = fileChooser.getSelectedFile();
		    if (fileToSave.exists()){
		    	
		    	int dontSave = JOptionPane.showConfirmDialog(view, "The file already exists. Do you want overwrite it?");
		    	if(dontSave==1){
		    		continue;
		    	}
		    	
		    }
		    
		    saver.saveDiagram(view.getDrawer().getDiagram(), fileToSave.getAbsolutePath());
		    break;
		    
		}
	
	}

}
