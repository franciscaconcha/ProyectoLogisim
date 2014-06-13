package com.cburch.logisim.statediagram.view;

import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentFactory;
import com.cburch.logisim.comp.EndData;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.memory.Register;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.std.wiring.PinAttributes;
import com.cburch.logisim.tools.Strings;

public class SequentialCircuit {
	// n es el número de entradas que tendrá el circuito
	private Project proj;
	private Circuit combinatorial;
	private int bitNumber;
	private Circuit register;

	public SequentialCircuit(Project proj, Circuit combinatorial, int bitNumber) {
		this.proj = proj;
		this.combinatorial = combinatorial;
		this.bitNumber = bitNumber;
		this.register = createRegister();
		Circuit main = proj.getCurrentCircuit();
		addSubcircuitsToMain(main);
	}

	private Circuit createRegister() {
		Circuit registerCircuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(registerCircuit));
		Register source = new Register();
		setAttributes(source);
		Component c = source.createComponent(Location.create(300, 300),
				source.createAttributeSet());
		CircuitMutation mutation = new CircuitMutation(registerCircuit);
		mutation.add(c);
		addComponent(mutation, source);
		createPins(registerCircuit);
		return registerCircuit;
	}
	
	
	private void createPins(Circuit register){
		Pin pinFactory = Pin.FACTORY;
		PinAttributes attrsInput = (PinAttributes) pinFactory.createAttributeSet();
		int y_inicial = 100;
		CircuitMutation mutation = new CircuitMutation(register);
		createPins(pinFactory, 100, y_inicial, attrsInput, mutation); //creamos los inputs
		addComponent(mutation, pinFactory);
		
		
		mutation = new CircuitMutation(register);
		PinAttributes attrsOutput = (PinAttributes) pinFactory.createAttributeSet();
		attrsOutput.setValue(Pin.ATTR_TYPE, true); // provoca que sean outputs en vez de inputs
		attrsOutput.setValue(StdAttr.FACING, Direction.WEST);
		createPins(pinFactory, 500, y_inicial, attrsOutput, mutation); //creamos los outputs
		addComponent(mutation, pinFactory);
	}
	
	
	private void createPins(InstanceFactory factory, int x, int y, AttributeSet attrs, CircuitMutation mutation) {
		for (int i = 0; i < this.bitNumber; i++) {
			Component pin = factory.createComponent(Location.create(x, y + i*40), attrs);
			mutation.add(pin);
		}
	}
	

	private void addSubcircuitsToMain(Circuit main) {

		SubcircuitFactory factoryComb = new SubcircuitFactory(combinatorial);
		Component cc = factoryComb.createComponent(Location.create(200, 200),
				factoryComb.createAttributeSet());

		SubcircuitFactory factoryRegister = new SubcircuitFactory(register);
		Component rc = factoryRegister
				.createComponent(Location.create(200, 500),
						factoryRegister.createAttributeSet());

		CircuitMutation mutation = new CircuitMutation(main);
		mutation.add(rc);
		mutation.add(cc);
		addComponent(mutation, factoryRegister);
	}


	private void setAttributes(Register source) {
		source.setAttributes(new Attribute[] { StdAttr.WIDTH, StdAttr.TRIGGER,
				StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { BitWidth.create(this.bitNumber),
						StdAttr.TRIG_RISING, "", StdAttr.DEFAULT_LABEL_FONT });
	}

	private void addComponent(CircuitMutation mutation, InstanceFactory factory){
		Action action = mutation.toAction(Strings.getter("addComponentAction",
				factory.getDisplayGetter()));
		proj.doAction(action);
	}

	private void getStateInputs() {
		// obtenemos los componentes del circuito (en específico, buscamos los
		// inputs y outputs)
		Set<Component> comps = combinatorial.getNonWires();
		for (Component c : comps) {
			// obtenemos el conjunto de atributos y vemos si es que son
			// atributos de un Pin
			AttributeSet attrs = c.getAttributeSet();
			if (attrs instanceof PinAttributes) {
				// casteamos y obtenemos el nombre
				PinAttributes pinAttrs = (PinAttributes) attrs;
				String label = (String) pinAttrs.getValue(StdAttr.LABEL);
				if (label.matches("^Q\\d{1,3}"))
					System.out.println(label);
			}
		}
	}
}
