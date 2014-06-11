package com.cburch.logisim.statediagram.view;

import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.SubcircuitFactory;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.PinAttributes;
import com.cburch.logisim.tools.Strings;

public class SequentialCircuit {
	// n es el número de entradas que tendrá el circuito
	public SequentialCircuit(Project proj, Circuit combinatorial){
		Circuit main = proj.getCurrentCircuit();	
		
		Circuit register = new Circuit("Register");
		proj.doAction(LogisimFileActions.addCircuit(register));
		
		SubcircuitFactory factoryComb = new SubcircuitFactory(combinatorial);
		Component cc = factoryComb.createComponent(Location.create(200, 200), factoryComb.createAttributeSet());
		
		SubcircuitFactory factoryRegister = new SubcircuitFactory(register);
		Component rc = factoryRegister.createComponent(Location.create(200, 500), factoryRegister.createAttributeSet());
		
		CircuitMutation mutation = new CircuitMutation(main);
		mutation.add(rc);
		mutation.add(cc);
		Action action = mutation.toAction(Strings.getter("addComponentAction", factoryRegister.getDisplayGetter()));
		proj.doAction(action);
		
		// obtenemos los componentes del circuito (en específico, buscamos los inputs y outputs)
		Set<Component> comps = combinatorial.getNonWires();
		
		
		for (Component c : comps){
			// obtenemos el conjunto de atributos y vemos si es que son atributos de un Pin
			AttributeSet attrs = c.getAttributeSet();
			if (attrs instanceof PinAttributes){
				// casteamos y obtenemos el nombre
				PinAttributes pinAttrs = (PinAttributes) attrs;
				String label = (String) pinAttrs.getValue(StdAttr.LABEL);
				if (label.matches("^Q\\d{1,3}"))
					System.out.println(label);
			} 
		}
	}
}
