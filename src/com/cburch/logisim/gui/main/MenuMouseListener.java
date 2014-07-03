package com.cburch.logisim.gui.main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.circuit.WireSet;
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
		this.circuit = canvas.getCircuit();
		this.proj = canvas.getProject();
		this.locations = locations;	
	}
	
	public MenuMouseListener( ArrayList<Location> locations, Canvas canvas, int width ) {
		this.circuit = canvas.getCircuit();
		this.proj = canvas.getProject();
		this.width = width;
		this.locations = getAllLocations(locations);		
	}
		

	private ArrayList<Location> getAllLocations(ArrayList<Location> locations) {
		ArrayList<Location> locs = new ArrayList<Location>();
		for (Location location : locations) {
			for( Wire wire : circuit.getWires(location ) ) {
				WireSet w = circuit.getWireSet(wire);
				for (Wire wire2 : w.getWires()){
					if(!locations.contains(wire2.getEnd0()) && !locations.contains(wire2.getEnd1())){
						locs.add(wire2.getEnd0());
						locs.add(wire2.getEnd1());
					}
				}
			}
		}
		locations.addAll(locs);
		return locations;
	}

	@Override
	public void mouseClicked( MouseEvent arg0 ) {
		SetAttributeAction act = new SetAttributeAction(circuit, Strings.getter("selectionAttributeAction"));
		for (Location location : locations) {
			for (Component comp : circuit.getComponents(location)) {
				if (!(comp instanceof Wire) && this.width!=0) {
					act.set(comp, StdAttr.WIDTH, BitWidth.create(width));
				}
				else{
					((Wire) comp).widthErrorColor = new Color(255, 123, 0);
				}
			}
		}
		proj.doAction(act);
	}

	
	@Override
	public void mouseEntered( MouseEvent arg0 ) {
		if(width==0) return;
		SetAttributeAction act = new SetAttributeAction(circuit, Strings.getter("selectionAttributeAction"));
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = Color.BLUE;
			}
		}
		proj.doAction(act);
	}

	@Override
	public void mouseExited( MouseEvent arg0 ) {
		if(width==0) return;
		SetAttributeAction act = new SetAttributeAction(circuit, Strings.getter("selectionAttributeAction"));
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = new Color(255, 123, 0);
			}
		}
		proj.doAction(act);
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
				}
				else{
					((Wire) comp).widthErrorColor = new Color(255, 123, 0);
				}
			}
		}
		proj.doAction(act);
	}

}
