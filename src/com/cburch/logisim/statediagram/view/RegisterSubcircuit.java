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
import com.cburch.logisim.std.wiring.Clock;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.Strings;
import com.cburch.logisim.util.StringGetter;

public class RegisterSubcircuit {

	private Project proj;
	private int bitWidth;
	private Circuit registerCircuit;
	private CircuitMutation mutation;
	private int left, right, yCoord, yClock;
	private Location pinInputPort, registerInputPort, registerOutputPort, registerClockPort, pinOutputPort, clockPort;
	
	
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
		addClock();
		addWires();
		buildComponents();
		return registerCircuit;
	}
	
	private void setCoords(){
		this.left = 100;
		this.right = 600;
		this.yCoord = 100;
		this.yClock = 300;
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
		Component clockToRegister = Wire.create(clockPort, registerClockPort);
		this.mutation.add(inputToRegister);
		this.mutation.add(registerToOutput);
		this.mutation.add(clockToRegister);
	}

	private void addRegister() {
		Register factory = new Register();
		configureAttributes(factory);
		Component c = factory.createComponent(Location.create((right + left)/2, yCoord),
				factory.createAttributeSet());
		this.mutation.add(c);
		
		List<EndData> endData = c.getEnds();
		registerOutputPort = endData.get(0).getLocation();
		registerInputPort = endData.get(1).getLocation();
		registerClockPort = endData.get(2).getLocation();
		
	}
	
	private void addClock(){
		Clock factory = Clock.FACTORY;
		AttributeSet attrs = factory.createAttributeSet();
		attrs.setValue(StdAttr.FACING, Direction.NORTH);
		Component clock = factory.createComponent(Location.create(registerClockPort.getX(), yClock), attrs);
		this.mutation.add(clock);
		
		clockPort = clock.getEnd(0).getLocation();
	}


	
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
}
