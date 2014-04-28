package com.cburch.logisim.gui.log;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


/* Nueva tab para ingreso de datos, @José Garrido */

public class InputPanel extends LogPanel {
   private int numberOfEntries;
   private ArrayList<JTextField> entradas= new ArrayList<JTextField>();
   final JPanel panelEntradas = new JPanel();
   public int getNumberOfEntries(){
	   return numberOfEntries;
   }
   public InputPanel(LogFrame frame){
	 
	   this(frame,3,1);
   }
	public InputPanel(LogFrame frame,int nEntradas, int relojes) {
		super(frame);
		numberOfEntries=  nEntradas+relojes;
		this.setLayout(new GridLayout(0,1));
		
		JPanel titulos = new JPanel();
		titulos.setLayout(new GridLayout(0, numberOfEntries));
		for (int i = 0; i < nEntradas ; i++) {
            titulos.add(new JLabel("Entrada " + String.valueOf(i), JLabel.CENTER));
        }
        
        	for (int i = 0; i < relojes ; i++) {
                titulos.add(new JLabel("Ticks Reloj " + String.valueOf(i), JLabel.CENTER));
            }
        this.add(titulos, BorderLayout.NORTH);
		final JPanel panelEntradas = new JPanel();
		panelEntradas.setLayout(new GridLayout(0, numberOfEntries));
        for (int i = 0; i < numberOfEntries; i++) {
        	entradas.add(new JTextField());
            panelEntradas.add(entradas.get(i));
        }
        JScrollPane panelEntradasScroll = new JScrollPane(panelEntradas);
        this.add(panelEntradasScroll,BorderLayout.CENTER);
        
        
        JPanel botones = new JPanel();
        botones.add(new JButton(new AbstractAction("Agregar Entrada") {

            @Override
            public void actionPerformed(ActionEvent e) {
            	
                
                for (int i = 0; i < InputPanel.this.getNumberOfEntries(); i++) {
                JTextField p=new JTextField();
                entradas.add(p);
                panelEntradas.add(p);}
                InputPanel.this.validate();
            }
        }));
   
        this.add(botones, BorderLayout.SOUTH);
        
		
	}

	public InputPanel(LogFrame frame, LayoutManager manager) {
		super(frame, manager);
		
	}

	@Override
	public String getTitle() {
		return Strings.get("inputTab");
	}

	@Override
	public String getHelpText() {
		return Strings.get("inputTabHelp");
	}

	@Override
	public void localeChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelChanged(Model oldModel, Model newModel) {
		// TODO Auto-generated method stub

	}
	

	
	

}
