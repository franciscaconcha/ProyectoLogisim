package com.cburch.logisim.statediagram.view;

import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.PinAttributes;

public class SequentialCircuit {
	// n es el número de entradas que tendrá el circuito
	public SequentialCircuit(Project proj, int n){                                            
		Circuit circuit = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(circuit));
		// obtenemos los componentes del circuito (en específico, buscamos los inputs y outputs)
		Set<Component> comps = proj.getCircuitState().getCircuit().getNonWires();
		for (Component c : comps){
			// obtenemos el conjunto de atributos y vemos si es que son atributos de un Pin
			AttributeSet attrs = c.getAttributeSet();
			if (attrs instanceof PinAttributes){
				// casteamos y obtenemos el nombre
				PinAttributes pinAttrs = (PinAttributes) attrs;
				String label = (String) pinAttrs.getValue(StdAttr.LABEL);
				System.out.println(label);
			} 
		}
	}
}
