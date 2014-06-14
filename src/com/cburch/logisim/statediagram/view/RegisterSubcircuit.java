package com.cburch.logisim.statediagram.view;

import java.util.ArrayList;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.SplitterAttributes;
import com.cburch.logisim.circuit.SplitterFactory;
import com.cburch.logisim.comp.Component;
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
	private ArrayList<Location> inputsLocation;
	private ArrayList<Location> outputsLocation;
	private int left, right, top, bottom;
	
	
	public RegisterSubcircuit(Project aProject, int bitWidth){
		this.proj = aProject;
		this.bitWidth = bitWidth;
		this. registerCircuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(registerCircuit));
		this.mutation = new CircuitMutation(registerCircuit);
	}
	
	public Circuit create(Project proj, int bitWidth) {
		calculateLocations();
		addInputs();
		addOutputs();
		addRegister();
		addSplitters();
		buildComponents();
		return registerCircuit;
	}
	
	private void calculateLocations(){
		this.inputsLocation = new ArrayList<Location>();
		this.outputsLocation = new ArrayList<Location>();
		this.left = 100;
		this.right = 600;
		this.top = 100;
		int currentY = top - 40;
		for (int i = 0; i < this.bitWidth; i++){
			currentY += 40;
			inputsLocation.add(Location.create(left, currentY));
			outputsLocation.add(Location.create(right, currentY));
		}
		this.bottom = currentY;
	}
	
	private void addRegister() {
		Register source = new Register();
		configureAttributes(source);
		Component c = source.createComponent(Location.create((right + left)/2, (bottom + top)/2),
				source.createAttributeSet());
		this.mutation.add(c);
	}

	private void addSplitters(){
		SplitterFactory factory = SplitterFactory.instance;
		AttributeSet leftAttrs = factory.createAttributeSet();
		leftAttrs.setValue(SplitterAttributes.ATTR_FANOUT, this.bitWidth);
		leftAttrs.setValue(SplitterAttributes.ATTR_WIDTH, BitWidth.create(this.bitWidth));
		
		AttributeSet rightAttrs = (AttributeSet) leftAttrs.clone();
		leftAttrs.setValue(StdAttr.FACING, Direction.WEST);
		
		Component leftSplitter = factory.createComponent(Location.create(left + 100, (bottom + top)/2), leftAttrs);
		Component rightSplitter = factory.createComponent(Location.create(right - 100, (bottom + top)/2), rightAttrs);
		
		mutation.add(leftSplitter);
		mutation.add(rightSplitter);
	}
	
	private void configureAttributes(Register source) {
		source.setAttributes(new Attribute[] { StdAttr.WIDTH, StdAttr.TRIGGER,
				StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { BitWidth.create(this.bitWidth), StdAttr.TRIG_RISING,
						"", StdAttr.DEFAULT_LABEL_FONT });
	}

	private void addInputs() {
		AttributeSet attrsInput = Pin.FACTORY.createAttributeSet();
		createPins(this.inputsLocation, attrsInput); // creamos los inputs
	}

	private void addOutputs() {
		AttributeSet attrsOutput = Pin.FACTORY.createAttributeSet();
		attrsOutput.setValue(Pin.ATTR_TYPE, true); // provoca que sean outputs en vez de inputs
		attrsOutput.setValue(StdAttr.FACING, Direction.WEST);
		createPins(this.outputsLocation, attrsOutput); // creamos los outputs
	}

	private void buildComponents(){
		StringGetter getter = new StringGetter(){
										public String get(){return "RegisterSubcircuit";}
									};
		Action action = this.mutation.toAction(Strings.getter("addComponentAction",getter));
		this.proj.doAction(action);
	}

	private void createPins(ArrayList<Location> locations, AttributeSet attrs) {
		Pin factory = Pin.FACTORY;
		for (Location loc : locations){
			Component pin = factory.createComponent(loc, attrs);
			this.mutation.add(pin);	
		}
	}
}
