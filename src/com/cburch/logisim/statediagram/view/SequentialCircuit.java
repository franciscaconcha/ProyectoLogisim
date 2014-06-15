package com.cburch.logisim.statediagram.view;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.proj.Project;


public class SequentialCircuit {
	
	public SequentialCircuit(Project proj, Circuit combinatorial, int bitWidth) {
		RegisterSubcircuit rs = new RegisterSubcircuit(proj, bitWidth);
		Circuit register = rs.create(proj, bitWidth);		
		MainSubcircuit ms = new MainSubcircuit(proj, combinatorial, register, bitWidth);
		ms.create();
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


}
