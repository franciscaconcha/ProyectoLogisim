/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.util.GraphicsUtil;
import java.util.ArrayList;

class InputPanel extends LogPanel {
	private int modified=0;

	private static final Font BODY_FONT = new Font("Serif", Font.PLAIN, 14);
	private  ArrayList<Integer> selectedIndex=new ArrayList<Integer>();
	private MyListener myListener = new MyListener();
	final JPanel entriesPanel = new JPanel();
	private ArrayList<JTextField> entries= new ArrayList<JTextField>();
	
	
	private class MyListener implements ModelListener {
		public void selectionChanged(ModelEvent event) {
			computePreferredSize();
		}
		public void entryAdded(ModelEvent event, Value[] values) { 
			repaint();
		}
		
		public void filePropertyChanged(ModelEvent event) { }
	}
	
	public InputPanel(LogFrame frame) {
		super(frame);
		modelChanged(null, getModel());
	}
	
	@Override
	public String getTitle() {
		return Strings.get("InputTab");
	}
	
	@Override
	public String getHelpText() {
		return Strings.get("InputHelp");
	}
	
	@Override
	public void localeChanged() {
		computePreferredSize();
		repaint();
	}
	
	@Override
	public void modelChanged(Model oldModel, Model newModel) {
		if (oldModel != null) oldModel.removeModelListener(myListener);
		if (newModel != null) newModel.addModelListener(myListener);
	}	

	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension sz = getSize();
		
		Model model = getModel();
		Selection sel = model.getSelection();
		//if (model == null) return;
		int columns = selectedIndex.size();
		if (columns == 0) {
			g.setFont(BODY_FONT);
			GraphicsUtil.drawCenteredText(g, Strings.get("tableEmptyMessage"), sz.width / 2, sz.height / 2);
			return;
		}
		if(modified==0){
			this.setLayout(new GridLayout(0,1));
			JPanel titles = new JPanel();
			titles.setLayout(new GridLayout(0, columns));		

			for (int i = 0; i < columns; i++) {
				titles.add(new JLabel(sel.get(selectedIndex.get(i)).toShortString()), JLabel.CENTER);
			}
			this.add(titles, BorderLayout.NORTH);
			final JPanel entriesPanel = new JPanel();
			entriesPanel.setLayout(new GridLayout(0, columns));
			for (int i = 0; i < columns; i++) {
				entries.add(new JTextField());
				entriesPanel.add(entries.get(i));
			}
			JScrollPane scrollEntriesPanel = new JScrollPane(entriesPanel);
			this.add(scrollEntriesPanel,BorderLayout.CENTER);


			JPanel buttons = new JPanel();
			buttons.add(new JButton(new AbstractAction("Agregar Entrada") {
				@Override
				public void actionPerformed(ActionEvent e) {


					for (int i = 0; i < selectedIndex.size(); i++) {
						JTextField p=new JTextField();
						entries.add(p);
						entriesPanel.add(p);}
					InputPanel.this.validate();
				}
			}));

			this.add(buttons, BorderLayout.SOUTH);
			modified=1;
		}
	}
	
	private void computePreferredSize() {
		Model model = getModel();
		Selection sel = model.getSelection();
		int columns = 0;
		selectedIndex = new ArrayList<Integer>();
		for(int j=0; j<sel.size();j++){			
			if(sel.get(j).toString().startsWith("Input") || sel.get(j).toString().startsWith("Clock")){
				columns++;
				selectedIndex.add(j);
			}
		}
		/*if (columns == 0) {
			setPreferredSize(new Dimension(0, 0));
			return;
		}
		
		Graphics g = getGraphics();
		if (g == null) {
			cellHeight = 16;
			cellWidth = 24;
		} else {
			FontMetrics fm = g.getFontMetrics(HEAD_FONT);
			cellHeight = fm.getHeight();
			cellWidth = 24;
			for (int i = 0; i < columns; i++) {
				String header = sel.get(selectedIndex.get(i)).toShortString();
				cellWidth = Math.max(cellWidth, fm.stringWidth(header));
			}
		}
		
		tableWidth = (cellWidth + COLUMN_SEP) * columns - COLUMN_SEP;
		tableHeight = cellHeight * + HEADER_SEP;
		setPreferredSize(new Dimension(tableWidth, tableHeight));*/
		revalidate();
		repaint();
	}
}
