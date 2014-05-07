package com.cburch.logisim.gui.main;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.gates.GateAttributes;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.SetAttributeAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ActionItemError implements ActionListener {

	private int width;
	private Project project;
	private ArrayList<Location> locations;
	private Circuit circuit;

	public ActionItemError(ArrayList<Location> locations, int width,
			Project project) {
		this.locations = locations;
		this.width = width;
		this.project = project;
		this.circuit = project.getCurrentCircuit();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SetAttributeAction act = new SetAttributeAction(circuit,
				Strings.getter("selectionAttributeAction"));
		for (Location location : locations) {
			for (Component comp : circuit.getComponents(location)) {
				if (comp instanceof Wire) {
					if (!(comp.getFactory() instanceof Pin)) {
						//act.set(comp, GateAttributes.ATTR_INPUTS, 15);
					}
					act.set(comp, StdAttr.WIDTH, BitWidth.create(width));
				}
			}
		}
		project.doAction(act);
	}
}
