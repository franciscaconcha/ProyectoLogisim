package com.cburch.logisim.statediagram.view;

import java.util.List;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.EndData;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.memory.Register;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.Strings;
import com.cburch.logisim.util.StringGetter;

public class RegisterSubcircuit {

	private Project proj;
	private int bitWidth;
	private Circuit registerCircuit;
	private CircuitMutation mutation;
	private int left, right, yCoord;
	private Location pinInputPort, registerInputPort, registerOutputPort, registerClockPort, pinOutputPort;
	
	
	public RegisterSubcircuit(Project aProject, int bitWidth){
		this.proj = aProject;
		this.bitWidth = bitWidth;
		this. registerCircuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(registerCircuit));
		this.mutation = new CircuitMutation(registerCircuit);
	}
	
	public Circuit create(Project proj, int bitWidth) {
		setCoords();
		addInputs();
		addOutputs();
		addRegister();
		addWires();
		buildComponents();
		return registerCircuit;
	}
	
	private void setCoords(){
		this.left = 100;
		this.right = 600;
		this.yCoord = 100;
//		int currentY = top - 40;
//		for (int i = 0; i < this.bitWidth; i++){
//			currentY += 40;
//			inputsLocation.add(Location.create(left, currentY));
//			outputsLocation.add(Location.create(right, currentY));
//		}
//		this.bottom = currentY;
	}
	
	private void addInputs() {
		AttributeSet attrsInput = Pin.FACTORY.createAttributeSet();
		attrsInput.setValue(StdAttr.WIDTH, BitWidth.create(this.bitWidth));
		Location loc = Location.create(left, yCoord);
		Component pin = createPin(attrsInput, loc);
		pinInputPort = pin.getEnd(0).getLocation();
		//createPins(this.inputsLocation, attrsInput); // creamos los inputs
	}

	private void addOutputs() {
		AttributeSet attrsOutput = Pin.FACTORY.createAttributeSet();
		attrsOutput.setValue(Pin.ATTR_TYPE, true); // provoca que sean outputs en vez de inputs
		attrsOutput.setValue(StdAttr.FACING, Direction.WEST);
		attrsOutput.setValue(StdAttr.WIDTH, BitWidth.create(this.bitWidth));
		Location loc = Location.create(right, yCoord);
		Component pin = createPin(attrsOutput, loc);
		pinOutputPort = pin.getEnd(0).getLocation();
	}

	private Component createPin(AttributeSet attrs, Location loc){
		Pin factory = Pin.FACTORY;
		Component pin = factory.createComponent(loc, attrs);
		this.mutation.add(pin);
		return pin;
	}

	private void addWires(){
		Component inputToRegister = Wire.create(pinInputPort, registerInputPort);
		Component registerToOutput = Wire.create(registerOutputPort, pinOutputPort);
		this.mutation.add(inputToRegister);
		this.mutation.add(registerToOutput);
	}

	private void addRegister() {
		Register source = new Register();
		configureAttributes(source);
		Component c = source.createComponent(Location.create((right + left)/2, yCoord),
				source.createAttributeSet());
		this.mutation.add(c);
		
		List<EndData> endData = c.getEnds();
		registerOutputPort = endData.get(0).getLocation();
		registerInputPort = endData.get(1).getLocation();
		registerClockPort = endData.get(2).getLocation();
		
	}

//	private void addSplitters(){
//		SplitterFactory factory = SplitterFactory.instance;
//		AttributeSet leftAttrs = factory.createAttributeSet();
//		leftAttrs.setValue(SplitterAttributes.ATTR_FANOUT, this.bitWidth);
//		leftAttrs.setValue(SplitterAttributes.ATTR_WIDTH, BitWidth.create(this.bitWidth));
//		
//		AttributeSet rightAttrs = (AttributeSet) leftAttrs.clone();
//		leftAttrs.setValue(StdAttr.FACING, Direction.WEST);
//		
//		Component leftSplitter = factory.createComponent(Location.create(left + 100, (bottom + top)/2), leftAttrs);
//		Component rightSplitter = factory.createComponent(Location.create(right - 100, (bottom + top)/2), rightAttrs);
//		
//		mutation.add(leftSplitter);
//		mutation.add(rightSplitter);
//	}
	
	private void configureAttributes(Register source) {
		source.setAttributes(new Attribute[] { StdAttr.WIDTH, StdAttr.TRIGGER,
				StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { BitWidth.create(this.bitWidth), StdAttr.TRIG_RISING,
						"", StdAttr.DEFAULT_LABEL_FONT });
	}

	private void buildComponents(){
		StringGetter getter = new StringGetter(){
										public String get(){return "RegisterSubcircuit";}
									};
		Action action = this.mutation.toAction(Strings.getter("addComponentAction",getter));
		this.proj.doAction(action);
	}
	
	
//	private void createPins(ArrayList<Location> locations, AttributeSet attrs) {
//		Pin factory = Pin.FACTORY;
//		for (Location loc : locations){
//			Component pin = factory.createComponent(loc, attrs);
//			this.mutation.add(pin);	
//		}
//	}
}
