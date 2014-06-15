package com.cburch.logisim.statediagram.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentUserEvent;
import com.cburch.logisim.comp.EndData;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.PinAttributes;
import com.cburch.logisim.tools.Strings;
import com.cburch.logisim.tools.ToolTipMaker;
import com.cburch.logisim.util.StringGetter;

public class MainSubcircuit {
	private Project proj;
	private Circuit combinatorial, register, main;
	private int bitWidth;
	private CircuitMutation mutation;
	private ArrayList<Location> inputPorts;
	private ArrayList<Location> outputPorts;
	private Component combinatorialSubcircuit, registerSubcircuit;
	
	public MainSubcircuit(Project proj, Circuit combinatorial, Circuit register, int bitWidth){
		this.proj = proj;
		this.combinatorial = combinatorial;
		this.register = register;
		this.bitWidth = bitWidth;
		this.main = proj.getCurrentCircuit();
		this.mutation = new CircuitMutation(main);
	}
	
	public void create(){
		addSubcircuits();
		computeLocations();
		buildComponents();
	}

	private void addSubcircuits() {

		SubcircuitFactory factoryComb = new SubcircuitFactory(combinatorial);
		Component cc = factoryComb.createComponent(Location.create(200, 200),
				factoryComb.createAttributeSet());

		SubcircuitFactory factoryRegister = new SubcircuitFactory(register);
		Component rc = factoryRegister
				.createComponent(Location.create(200, 500),
						factoryRegister.createAttributeSet());

		registerSubcircuit = rc;
		combinatorialSubcircuit = cc;
		mutation.add(rc);
		mutation.add(cc);
	}
	

	private void computeLocations() {
		inputPorts = new ArrayList<Location>();
		outputPorts = new ArrayList<Location>();
		List<EndData> ends = combinatorialSubcircuit.getEnds();
		ToolTipMaker tooltip = (ToolTipMaker) combinatorialSubcircuit;
		for (EndData end : ends){
			Location loc = end.getLocation();
			ComponentUserEvent cue = new ComponentUserEvent(null, loc.getX(), loc.getY()); 
			String label = tooltip.getToolTip(cue);
			if (label.matches("^Q\\d{1,3}"))
				inputPorts.add(loc);
			else if (label.matches("^D\\d{1,3}"))
				outputPorts.add(loc);
		}
	}
	
	private void buildComponents(){
		StringGetter getter = new StringGetter(){
										public String get(){return "MainSubcircuit";}
									};
		Action action = this.mutation.toAction(Strings.getter("addComponentAction",getter));
		this.proj.doAction(action);
	}

}
