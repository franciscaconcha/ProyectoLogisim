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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.cburch.logisim.circuit.Simulator;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.util.GraphicsUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InputPanel extends LogPanel {
	private int modified=0;

	private static final Font BODY_FONT = new Font("Serif", Font.PLAIN, 14);
	private  ArrayList<Integer> selectedIndex=new ArrayList<Integer>();
	private MyListener myListener = new MyListener();
	final JPanel entriesPanel = new JPanel();
	private ArrayList<JTextField> entries= new ArrayList<JTextField>();
	private ArrayList<Integer> bitWidth= new ArrayList<Integer>();


	private class MyListener implements ModelListener {
		public void selectionChanged(ModelEvent event) {
			computePreferredSize();
		}
		public void entryAdded(ModelEvent event, Value[] values) { 
			repaint();
		}

		public void filePropertyChanged(ModelEvent event) { }
	}
	
	Project project;
	Simulator simulator;
	
	public InputPanel(LogFrame frame) {
		super(frame);
		modelChanged(null, getModel());
		project = getProject();
		simulator = project.getSimulator();
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
	
	private void simularTicks() {
		for (int i=0;i<4;i++){
			simulator.tick();
		}		
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension sz = getSize();
		Model model = getModel();
		Selection sel = model.getSelection();
		final int columns = selectedIndex.size();
		if (columns == 0) {
			g.setFont(BODY_FONT);
			GraphicsUtil.drawCenteredText(g, Strings.get("tableEmptyMessage"), sz.width / 2, sz.height / 2);
			return;
		}
		if(modified==0){
			this.removeAll();
			bitWidth.clear();
			final JButton submit = new JButton(new AbstractAction(Strings.get("inputSimulate")){
				public void actionPerformed(ActionEvent e) {
					simularTicks();						
				}

				/*debiese recibir la tabla? */
				
			});
			this.setLayout(new GridLayout(0,1));
			JPanel titles = new JPanel();
			titles.setLayout(new GridLayout(0, columns));		
			ArrayList<JLabel> componentes = new ArrayList<JLabel>();

			for (int i = 0; i < columns; i++) {
				if(sel.get(i).toString().startsWith(Strings.get("input"))){
					BitWidth w = sel.get(i).getComponent().getEnd(0).getWidth();
					int width = w.getWidth();
					bitWidth.add(width);
					componentes.add(new JLabel(sel.get(selectedIndex.get(i)).toShortString()+" ("+Integer.toString(width)+" bits)"));
				}
				else if(sel.get(i).toString().startsWith(Strings.get("clock"))){
					bitWidth.add(0);
					componentes.add(new JLabel(sel.get(selectedIndex.get(i)).toShortString()));
					
				}
				else{
					componentes.add(new JLabel(sel.get(selectedIndex.get(i)).toShortString()));
				}
			}
			for(int k=0 ; k<componentes.size() ; k++){
				titles.add(componentes.get(k));
			}
			this.add(titles, BorderLayout.NORTH);
			final JPanel entriesPanel = new JPanel();
			entriesPanel.setLayout(new GridLayout(0, columns));
			for (int i = 0; i < columns; i++) {
				JTextField newTextField=new JTextField();
				newTextField.getDocument().addDocumentListener(new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						validateTable();								    
					}
					public void removeUpdate(DocumentEvent e) {
						validateTable();
					}
					public void insertUpdate(DocumentEvent e) {
						validateTable();
					}
							
					public void validateTable(){
						boolean isInvalid=false;
						int i=0;
						for (JTextField entrie : entries) {
							if(i==columns){ i=0;}
							int width = bitWidth.get(i);
							Pattern pattern = Pattern.compile("(1|0){"+width+"}");
							if(width == 0){
								pattern = Pattern.compile("[0-9]+");
							}
							Matcher mat = pattern.matcher(entrie.getText());
							if(!mat.matches()){
								entrie.setForeground(Color.red);
								isInvalid=true;
							}
							else{
								entrie.setForeground(Color.black);
							}
							i++;
						}
						if(!isInvalid){
							submit.setEnabled(true);
						}
						else{
							submit.setEnabled(false);
							isInvalid=true;
						}
					}
				});
				entries.add(newTextField);
				entriesPanel.add(entries.get(i));
			}
			JScrollPane scrollEntriesPanel = new JScrollPane(entriesPanel);
			this.add(scrollEntriesPanel,BorderLayout.CENTER);


			JPanel buttons = new JPanel();
			buttons.add(new JButton(new AbstractAction(Strings.get("inputAddButton")) {
				@Override
				public void actionPerformed(ActionEvent e) {

					for (int i = 0; i < selectedIndex.size(); i++) {
						JTextField newTextField=new JTextField();
						newTextField.getDocument().addDocumentListener(new DocumentListener() {
							public void changedUpdate(DocumentEvent e) {
								validateTable();								    
							}
							public void removeUpdate(DocumentEvent e) {
								validateTable();
							}
							public void insertUpdate(DocumentEvent e) {
								validateTable();
							}

							public void validateTable(){
								boolean isInvalid=false;
								int i=0;
								for (JTextField entrie : entries) {
									if(i==columns){ i=0;}
									int width = bitWidth.get(i);
									Pattern pattern = Pattern.compile("(1|0){"+width+"}");
									if(width == 0){
										pattern = Pattern.compile("[0-9]+");
									}
									Matcher mat = pattern.matcher(entrie.getText());
									if(!mat.matches()){
										entrie.setForeground(Color.red);
										isInvalid=true;
									}
									else{
										entrie.setForeground(Color.black);
									}
									i++;
								}
								if(!isInvalid){
									submit.setEnabled(true);
								}
								else{
									submit.setEnabled(false);
									isInvalid=true;
									
								}
							}
						});
						entries.add(newTextField);
						entriesPanel.add(newTextField);}
				}
			}));



			submit.setEnabled(false);
			buttons.add(submit);


			this.add(buttons, BorderLayout.SOUTH);
			modified=1;
		}
	}

	private void computePreferredSize() {
		Model model = getModel();
		Selection sel = model.getSelection();
		selectedIndex = new ArrayList<Integer>();
		for(int j=0; j<sel.size();j++){			
			if(sel.get(j).toString().startsWith(Strings.get("input")) || sel.get(j).toString().startsWith(Strings.get("clock"))){
				selectedIndex.add(j);
			}
		}
		modified=0;
		revalidate();
		repaint();
	}
}
