package com.cburch.logisim.statediagram.view;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.proj.Project;

public class SequentialCircuit {
	// n es el número de entradas que tendrá el circuito
	public SequentialCircuit(Project proj, int n){
		Circuit circuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(circuit));
	}
}
