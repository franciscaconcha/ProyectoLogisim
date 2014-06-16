package com.cburch.logisim.statediagram.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.Splitter;
import com.cburch.logisim.circuit.SplitterAttributes;
import com.cburch.logisim.circuit.SplitterFactory;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentUserEvent;
import com.cburch.logisim.comp.EndData;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Clock;
import com.cburch.logisim.tools.Strings;
import com.cburch.logisim.tools.ToolTipMaker;
import com.cburch.logisim.util.StringGetter;

public class MainSubcircuit {
	private Project proj;
	private Circuit combinatorial, register, main;
	private int bitWidth;
	private CircuitMutation mutation;
	private Location[] inputCombinatorialPorts;
	private Location[] outputCombinatorialPorts;
	private Location inputRegister;
	private Location outputRegister;
	private Location clockRegister;
	private Location clockPort;
	private Component combinatorialSubcircuit, registerSubcircuit;
	private int left, right, top, bottom;
	private Splitter leftSplitter, rightSplitter;

	public MainSubcircuit(Project proj, Circuit combinatorial,
			Circuit register, int bitWidth) {
		this.proj = proj;
		this.combinatorial = combinatorial;
		this.register = register;
		this.bitWidth = bitWidth;
		this.main = proj.getCurrentCircuit();
		this.mutation = new CircuitMutation(main);
		this.left = 100;
		this.right = 500;
		this.top = 200;
		this.bottom = 500;
	}

	public void create() {
		addSubcircuits();
		computeCombinatorialLocations();
		computeRegisterLocations();
		addSplitters();
		addClock();
		addWires();
		buildComponents();
	}

	private void addSubcircuits() {

		SubcircuitFactory factoryComb = new SubcircuitFactory(combinatorial);
		Component cc = factoryComb.createComponent(
				Location.create((left + right) / 2, top),
				factoryComb.createAttributeSet());

		SubcircuitFactory factoryRegister = new SubcircuitFactory(register);
		Component rc = factoryRegister.createComponent(
				Location.create((left + right) / 2, bottom),
				factoryRegister.createAttributeSet());

		registerSubcircuit = rc;
		combinatorialSubcircuit = cc;
		mutation.add(rc);
		mutation.add(cc);
	}

	private void computeCombinatorialLocations() {
		inputCombinatorialPorts = new Location[16]; // es raro que tengamos más de 2^16 estados.
		outputCombinatorialPorts = new Location[16];
		List<EndData> ends = combinatorialSubcircuit.getEnds();
		ToolTipMaker tooltip = (ToolTipMaker) combinatorialSubcircuit;
		for (EndData end : ends) {
			Location loc = end.getLocation();
			ComponentUserEvent cue = new ComponentUserEvent(null, loc.getX(),
					loc.getY());
			String label = tooltip.getToolTip(cue);
			if (label.matches("^Q\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				inputCombinatorialPorts[labelDigit] = loc;
			} else if (label.matches("^D\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				outputCombinatorialPorts[labelDigit] = loc;
			}
		}
	}

	private void computeRegisterLocations() {
		List<EndData> ends = registerSubcircuit.getEnds();
		ToolTipMaker tooltip = (ToolTipMaker) registerSubcircuit;
		for (EndData end : ends) {
			Location loc = end.getLocation();
			ComponentUserEvent cue = new ComponentUserEvent(null, loc.getX(),
					loc.getY());
			String label = tooltip.getToolTip(cue);
			if (label.equals("Input"))
				inputRegister = loc;
			else if (label.equals("Output"))
				outputRegister = loc;
			else if (label.equals("Clock"))
				clockRegister = loc;
		}
	}

	private void addSplitters() {
		SplitterFactory factory = SplitterFactory.instance;
		AttributeSet leftAttrs = factory.createAttributeSet();
		leftAttrs.setValue(SplitterAttributes.ATTR_FANOUT, bitWidth);
		leftAttrs.setValue(SplitterAttributes.ATTR_WIDTH,
				BitWidth.create(bitWidth));

		AttributeSet rightAttrs = (AttributeSet) leftAttrs.clone();
		rightAttrs.setValue(StdAttr.FACING, Direction.WEST);

		Component leftSplitter = factory.createComponent(
				Location.create(left, top), leftAttrs);
		Component rightSplitter = factory.createComponent(
				Location.create(right, top), rightAttrs);

		mutation.add(leftSplitter);
		mutation.add(rightSplitter);

		this.leftSplitter = (Splitter) leftSplitter;
		this.rightSplitter = (Splitter) rightSplitter;

	}

	private void addWires() {
		wireFromSplitterToRegister(leftSplitter, outputRegister);
		wireFromSplitterToRegister(rightSplitter, inputRegister);
		wireFromClockToRegister();
		wireFromSplitterToCombinatorial(leftSplitter, inputCombinatorialPorts);
		wireFromSplitterToCombinatorial(rightSplitter, outputCombinatorialPorts);
	}

	private void wireFromSplitterToRegister(Splitter splitter,
			Location registerLoc) {
		Location splitterLoc = splitter.getEndLocation(0); // la posición 0 siempre corresponde al puerto de salida que agrupa en el splitter
		Location aux = Location.create(splitterLoc.getX(), registerLoc.getY());
		Wire vertical = Wire.create(splitterLoc, aux);
		Wire horizontal = Wire.create(aux, registerLoc);
		mutation.add(vertical);
		mutation.add(horizontal);
	}

	private void wireFromClockToRegister() {
		if (clockPort.getX() == clockRegister.getX()
				|| clockPort.getY() == clockRegister.getY()) { // verificamos que estén en una misma recta; de todas formas, por construcción debiese
																// estar correcto
			Wire wire = Wire.create(clockPort, clockRegister);
			mutation.add(wire);
		}
	}
	
	private void wireFromSplitterToCombinatorial(Splitter splitter, Location[] locations){
		for (int i = 0; i < locations.length; i++){
			if (locations[i] == null)
				break;
			Location combPort = locations[i];
			Location splitterPort = splitter.getEndLocation(i + 1); // posición 0 corresponde al otro extremo del splitter
			int mid = Math.max(combPort.getX(), splitterPort.getX()) - 10*(i+1);
			Location aux1 = Location.create(mid, splitterPort.getY());
			Location aux2 = Location.create(mid, combPort.getY());
			Wire hor1 = Wire.create(splitterPort, aux1);
			Wire vert = Wire.create(aux1, aux2);
			Wire hor2 = Wire.create(aux2, combPort);
			mutation.add(hor1);
			mutation.add(vert);
			mutation.add(hor2);
		}
	}
	

	private void addClock() {
		Clock factory = Clock.FACTORY;
		AttributeSet attrs = factory.createAttributeSet();
		attrs.setValue(StdAttr.FACING, Direction.NORTH);
		attrs.setValue(StdAttr.LABEL, "Clock");
		Component clockPin = factory.createComponent(Location.create(
				clockRegister.getX(), clockRegister.getY() + 50), attrs);
		this.mutation.add(clockPin);

		clockPort = clockPin.getEnd(0).getLocation();

	}

	private void buildComponents() {
		StringGetter getter = new StringGetter() {
			public String get() {
				return "MainSubcircuit";
			}
		};
		Action action = this.mutation.toAction(Strings.getter(
				"addComponentAction", getter));
		this.proj.doAction(action);
	}

}
