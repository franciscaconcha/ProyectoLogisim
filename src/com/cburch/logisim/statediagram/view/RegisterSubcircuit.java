package com.cburch.logisim.statediagram.view;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
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
	
	public RegisterSubcircuit(Project aProject, int bitWidth){
		this.proj = aProject;
		this.bitWidth = bitWidth;
		this. registerCircuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(registerCircuit));
		this.mutation = new CircuitMutation(registerCircuit);
	}
	public Circuit create(Project proj, int bitWidth) {
		addRegister();
		addInputs();
		addOutputs();
		addComponents();
		return registerCircuit;
	}

	private void addRegister() {
		Register source = new Register();
		configureAttributes(source);
		Component c = source.createComponent(Location.create(300, 300),
				source.createAttributeSet());
		this.mutation.add(c);
	}

	private void configureAttributes(Register source) {
		source.setAttributes(new Attribute[] { StdAttr.WIDTH, StdAttr.TRIGGER,
				StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { BitWidth.create(this.bitWidth), StdAttr.TRIG_RISING,
						"", StdAttr.DEFAULT_LABEL_FONT });
	}

	private void addInputs() {
		AttributeSet attrsInput = Pin.FACTORY.createAttributeSet();
		int x = 100;
		int y = 100;
		createPins(x, y, attrsInput); // creamos los inputs
	}

	private void addOutputs() {
		AttributeSet attrsOutput = Pin.FACTORY.createAttributeSet();
		attrsOutput.setValue(Pin.ATTR_TYPE, true); // provoca que sean outputs en vez de inputs
		attrsOutput.setValue(StdAttr.FACING, Direction.WEST);
		int x = 500;
		int y = 100;
		createPins(x, y, attrsOutput); // creamos los outputs
	}

	private void addComponents(){
		StringGetter getter = new StringGetter(){
										public String get(){return "RegisterSubcircuit";}
									};
		Action action = this.mutation.toAction(Strings.getter("addComponentAction",getter));
		this.proj.doAction(action);
	}

	private void createPins(int x, int y, AttributeSet attrs) {
		Pin factory = Pin.FACTORY;
		for (int i = 0; i < this.bitWidth; i++) {
			Component pin = factory.createComponent(
					Location.create(x, y + i * 40), attrs);
			this.mutation.add(pin);
		}
	}
}
