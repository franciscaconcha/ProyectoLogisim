package com.cburch.logisim.statediagram.view;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.proj.Project;


public class SequentialCircuit {
	// n es el número de entradas que tendrá el circuito
//	private Project proj;
//	private Circuit combinatorial;
//	private int bitWidth;
//	private Circuit register;

	public SequentialCircuit(Project proj, Circuit combinatorial, int bitWidth) {
		RegisterSubcircuit rs = new RegisterSubcircuit(proj, bitWidth);
		Circuit register = rs.create(proj, bitWidth);
		
		MainSubcircuit ms = new MainSubcircuit(proj, combinatorial, register, bitWidth);
		ms.create();
		//addSubcircuitsToMain(main);
	}	

//	private void addSubcircuitsToMain(Circuit main) {
//
//		SubcircuitFactory factoryComb = new SubcircuitFactory(combinatorial);
//		Component cc = factoryComb.createComponent(Location.create(200, 200),
//				factoryComb.createAttributeSet());
//
//		SubcircuitFactory factoryRegister = new SubcircuitFactory(register);
//		Component rc = factoryRegister
//				.createComponent(Location.create(200, 500),
//						factoryRegister.createAttributeSet());
//
//		CircuitMutation mutation = new CircuitMutation(main);
//		mutation.add(rc);
//		mutation.add(cc);
//		addComponent(mutation, factoryRegister);
//	}


//	private void addComponent(CircuitMutation mutation, ComponentFactory factory){
//		Action action = mutation.toAction(Strings.getter("addComponentAction",
//				factory.getDisplayGetter()));
//		proj.doAction(action);
//	}

//	private void getStateInputs() {
//		// obtenemos los componentes del circuito (en específico, buscamos los
//		// inputs y outputs)
//		Set<Component> comps = combinatorial.getNonWires();
//		for (Component c : comps) {
//			// obtenemos el conjunto de atributos y vemos si es que son
//			// atributos de un Pin
//			AttributeSet attrs = c.getAttributeSet();
//			if (attrs instanceof PinAttributes) {
//				// casteamos y obtenemos el nombre
//				PinAttributes pinAttrs = (PinAttributes) attrs;
//				String label = (String) pinAttrs.getValue(StdAttr.LABEL);
//				if (label.matches("^Q\\d{1,3}"))
//					System.out.println(label);
//			}
//		}
//	}
}
