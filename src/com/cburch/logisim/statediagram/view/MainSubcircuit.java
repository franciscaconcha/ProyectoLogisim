package com.cburch.logisim.statediagram.view;

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
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Clock;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.Strings;
import com.cburch.logisim.tools.ToolTipMaker;
import com.cburch.logisim.util.StringGetter;

public class MainSubcircuit {
	private Project proj;
	private Circuit combinatorial, register, main;
	private int bitWidth;
	private CircuitMutation mutation;
	private Location[] inputCodificationCombinatorialPorts;
	private Location[] outputCodificationCombinatorialPorts;
	private Location[] inputLogicalCombinatorialPorts;
	private Location[] outputLogicalCombinatorialPorts;
	private Location[] userInputs;
	private Location[] userOutputs;
	private Location inputRegister;
	private Location outputRegister;
	private Location clockRegister;
	private Location clockPort;
	private Component combinatorialSubcircuit, registerSubcircuit;
	private int left, right, top, bottom;
	private Splitter leftSplitter, rightSplitter;
	private static int MAX_CODIFICATION_BITS = 16;
	private static int MAX_LOGICAL_BITS = 32;

	public MainSubcircuit(Project proj, Circuit combinatorial,
			Circuit register, int bitWidth) {
		this.proj = proj;
		this.combinatorial = combinatorial;
		this.register = register;
		this.bitWidth = bitWidth;
		this.main = mainSubcircuit();
		this.mutation = new CircuitMutation(main);
		this.left = 300;
		this.right = 700;
		this.top = 200;
		this.bottom = 500;
	}

	public Circuit mainSubcircuit(){
		Circuit main = new Circuit("SequentialCircuit-Main");
		proj.doAction(LogisimFileActions.addCircuit(main));
		return main;
	}
	
	public void create() {
		addSubcircuits();
		computeCombinatorialLocations();
		computeRegisterLocations();
		addSplitters();
		addClock();
		addInputPins();
		addOutputPins();
		addWires();
		buildComponents();
		proj.setCurrentCircuit(main);
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
		inputCodificationCombinatorialPorts = new Location[MAX_CODIFICATION_BITS]; // es raro que tengamos más de 2^16 estados.
		outputCodificationCombinatorialPorts = new Location[MAX_CODIFICATION_BITS];
		inputLogicalCombinatorialPorts = new Location[MAX_LOGICAL_BITS];
		outputLogicalCombinatorialPorts = new Location[MAX_LOGICAL_BITS];
		List<EndData> ends = combinatorialSubcircuit.getEnds();
		ToolTipMaker tooltip = (ToolTipMaker) combinatorialSubcircuit;
		for (EndData end : ends) {
			Location loc = end.getLocation();
			ComponentUserEvent cue = new ComponentUserEvent(null, loc.getX(),
					loc.getY());
			String label = tooltip.getToolTip(cue);
			if (label.matches("^Q\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				inputCodificationCombinatorialPorts[labelDigit] = loc;
			} else if (label.matches("^D\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				outputCodificationCombinatorialPorts[labelDigit] = loc;
			} else if (label.matches("^x\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				inputLogicalCombinatorialPorts[labelDigit] = loc;
			} else if (label.matches("^z\\d{1,3}")) {
				int labelDigit = Integer.parseInt(label.substring(1));
				outputLogicalCombinatorialPorts[labelDigit] = loc;
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
				Location.create(right, top - 10 * (this.bitWidth + 1)),
				rightAttrs); // nos aseguramos que queden a la misma altura

		mutation.add(leftSplitter);
		mutation.add(rightSplitter);

		this.leftSplitter = (Splitter) leftSplitter;
		this.rightSplitter = (Splitter) rightSplitter;

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

	private void addInputPins() {
		userInputs = new Location[MAX_LOGICAL_BITS];
		int x = (left - 200 > 0) ? left - 200 : 0;
		AttributeSet attrs = Pin.FACTORY.createAttributeSet();
		addPins(inputLogicalCombinatorialPorts, x, "x", attrs, userInputs);
	}

	private void addOutputPins() {
		userOutputs = new Location[MAX_LOGICAL_BITS];
		int x = right + 200;
		AttributeSet attrs = Pin.FACTORY.createAttributeSet();
		attrs.setValue(Pin.ATTR_TYPE, true); // provoca que sean outputs en vez de inputs
		attrs.setValue(StdAttr.FACING, Direction.WEST);
		addPins(outputLogicalCombinatorialPorts, x, "z", attrs, userOutputs);
	}

	private void addPins(Location[] locations, int x, String name,
			AttributeSet attrs, Location[] endPorts) {
		Location aux = locations[0];
		if (aux == null)
			return;
		int y = aux.getY();
		for (int i = MAX_LOGICAL_BITS - 1; i >= 0; i--) {
			if (locations[i] == null)
				continue;
			Location loc = Location.create(x, y + i * 40);
			AttributeSet newAttrs = (AttributeSet) attrs.clone();
			newAttrs.setValue(StdAttr.LABEL, name + i);
			Component pin = Pin.FACTORY.createComponent(loc, newAttrs);
			endPorts[i] = pin.getEnd(0).getLocation();
			mutation.add(pin);
		}
	}

	private void addWires() {
		wireFromSplitterToRegister(leftSplitter, outputRegister);
		wireFromSplitterToRegister(rightSplitter, inputRegister);
		wireFromClockToRegister();
		wiresFromSplitterToCombinatorial(leftSplitter,
				inputCodificationCombinatorialPorts);
		wiresFromSplitterToCombinatorial(rightSplitter,
				outputCodificationCombinatorialPorts);
		wiresFromPinsToCombinatorial(userInputs, inputLogicalCombinatorialPorts);
		wiresFromPinsToCombinatorial(userOutputs, outputLogicalCombinatorialPorts);
	}

	private void wireFromSplitterToRegister(Splitter splitter,
			Location registerLoc) {
		Location splitterLoc = splitter.getEndLocation(0); // la posición 0 siempre corresponde al puerto de salida que agrupa en el splitter
		createWires(splitterLoc, registerLoc, 0);
	}

	private void wireFromClockToRegister() {
		createWires(clockRegister, clockPort, 0);
	}

	private void wiresFromSplitterToCombinatorial(Splitter splitter,
			Location[] locations) {
		for (int i = 0; i < locations.length; i++) {
			if (locations[i] == null)
				break;
			Location combPort = locations[i];
			Location splitterPort = splitter.getEndLocation(i + 1); // posición 0 corresponde al otro extremo del splitter, por eso i + 1
			createWires(combPort, splitterPort, -10 * (i + 1));
		}
	}

	private void wiresFromPinsToCombinatorial(Location[] pinPorts, Location[] combinatorialPorts) {
		for (int i = MAX_LOGICAL_BITS - 1; i >=0; i--){
			if (combinatorialPorts[i] == null)
				continue;
			createWires(pinPorts[i], combinatorialPorts[i], -10 * (i+1));
		}
	}

	private void createWires(Location loc1, Location loc2, int dx) {
		if (loc1.getX() == loc2.getX() || loc1.getY() == loc2.getY()) { // si es que están en la misma recta, podemos crear un sólo cable
			Wire wire = Wire.create(loc1, loc2);
			mutation.add(wire);
		} else if (dx == 0) {
			Location intermediate = Location.create(loc1.getX(), loc2.getY());
			Wire w1 = Wire.create(loc1, intermediate);
			Wire w2 = Wire.create(intermediate, loc2);
			mutation.add(w1);
			mutation.add(w2);
		} else {
			int aux = Math.max(loc1.getX(), loc2.getX()) + dx;
			Location intermediate1 = Location.create(aux, loc1.getY());
			Location intermediate2 = Location.create(aux, loc2.getY());
			Wire w1 = Wire.create(loc1, intermediate1);
			Wire w2 = Wire.create(intermediate1, intermediate2);
			Wire w3 = Wire.create(intermediate2, loc2);
			mutation.add(w1);
			mutation.add(w2);
			mutation.add(w3);
		}
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
