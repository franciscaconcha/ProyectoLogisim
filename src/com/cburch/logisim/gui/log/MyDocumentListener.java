package com.cburch.logisim.gui.log;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

class MyDocumentListener implements DocumentListener{
	
		private InputPanel inputPanel;
		private int columns;
		private JButton submit;
	
	public MyDocumentListener(InputPanel inputPanel){
		this.inputPanel=inputPanel;
	}
	
	public MyDocumentListener(InputPanel inputPanel, int columns,
			JButton submit) {
		this.inputPanel=inputPanel;
		this.columns=columns;
		this.submit=submit;
	}

	public void changedUpdate(DocumentEvent e) {
		inputPanel.validateTable(this.columns, this.submit);								    
	}
	public void removeUpdate(DocumentEvent e) {
		inputPanel.validateTable(this.columns, this.submit);
	}
	public void insertUpdate(DocumentEvent e) {
		inputPanel.validateTable(this.columns, this.submit);
	}
}
