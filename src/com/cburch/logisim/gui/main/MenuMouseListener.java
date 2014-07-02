package com.cburch.logisim.gui.main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.tools.SetAttributeAction;

public class MenuMouseListener implements MouseListener {

	private Circuit circuit;
	private ArrayList<Location> locations;
	private Project proj;
	private int width=0;

	public MenuMouseListener( ArrayList<Location> locations, Canvas canvas ) {
		this.locations = locations;
		this.circuit = canvas.getCircuit();
		this.proj = canvas.getProject();
	}
	
	public MenuMouseListener( ArrayList<Location> locations, Canvas canvas, int width ) {
		this.locations = locations;
		this.circuit = canvas.getCircuit();
		this.proj = canvas.getProject();
		this.width = width;
	}

	@Override
	public void mouseClicked( MouseEvent arg0 ) {
		SetAttributeAction act = new SetAttributeAction(circuit, Strings.getter("selectionAttributeAction"));
		for (Location location : locations) {
			for (Component comp : circuit.getComponents(location)) {
				if (!(comp instanceof Wire) && this.width!=0) {
					act.set(comp, StdAttr.WIDTH, BitWidth.create(width));
				}/*
				else{
					((Wire) comp).widthErrorColor = new Color(255, 123, 0);
				}*/
			}
		}
		proj.doAction(act);
	}

	
	@Override
	public void mouseEntered( MouseEvent arg0 ) {
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = Color.BLUE;
			}
		}
	}

	@Override
	public void mouseExited( MouseEvent arg0 ) {
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = new Color(255, 123, 0);
			}
		}
	}

	@Override
	public void mousePressed( MouseEvent arg0 ) {
	}

	@Override
	public void mouseReleased( MouseEvent arg0 ) {
		SetAttributeAction act = new SetAttributeAction(circuit, Strings.getter("selectionAttributeAction"));
		for (Location location : locations) {
			for (Component comp : circuit.getComponents(location)) {
				if (!(comp instanceof Wire)) {
					if(this.width!=0) act.set(comp, StdAttr.WIDTH, BitWidth.create(width));
				}/*
				else{
					((Wire) comp).widthErrorColor = new Color(255, 123, 0);
				}*/
			}
		}
		proj.doAction(act);
	}

}
