package com.cburch.logisim.gui.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.circuit.WireBundle;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.std.wiring.Wiring;
import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.util.GraphicsUtil;

public class ActionItemError2 implements ActionListener {

	private Project proj;

	public ActionItemError2(Project proj) {
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*Wiring w = new Wiring();
		Canvas canvas = proj.getFrame().getCanvas();
		AddTool at = (AddTool) w.getTool("Pin");
		AttributeSet atrSet = at.getAttributeSet();
		//Revisar como alterar el numero de bits de datos
		atrSet.setValue(Pin.ATTR_TRISTATE, false);
		at.setAttributeSet(atrSet); 
		at.setState(canvas, 2);
		
		MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0, 170, 44, 50, 50, 1, false, 1); 
		at.mouseReleased(canvas,canvas.getGraphics() , mouseEvent);
		canvas.completeAction();*/
		
		Set<Wire> wires = proj.getCurrentCircuit().getWires();
		CircuitState state = proj.getCircuitState();
		
		for (Wire w2 : wires) {
			Location s = w2.getE0();
			Location t = w2.getE1();
			System.out.println(s);
			System.out.println(t);
			System.out.println(state.getValue(s).getColor());
			if(state.getValue(s).getColor().equals(new Color(192, 0 ,0))){
				System.out.println("color correcto");
				Wiring w = new Wiring();
				Canvas canvas = proj.getFrame().getCanvas();
				AddTool at = (AddTool) w.getTool("Pin");
				AttributeSet atrSet = at.getAttributeSet();
				//Revisar como alterar el numero de bits de datos
				atrSet.setValue(Pin.ATTR_TRISTATE, false);
				at.setAttributeSet(atrSet); 
				at.setState(canvas, 2);
				
				MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0, s.getX()-100, s.getY(), 50, 50, 1, false, 1); 
				at.mouseReleased(canvas,canvas.getGraphics() , mouseEvent);
				canvas.completeAction();
				
				Location inicio = Location.create(s.getX()-100, s.getY());
				Location fin = Location.create(s.getX()-50, s.getY());
				
				Wire wire = Wire.create(inicio, fin);
				System.out.println(wire);
				//proj.getCurrentCircuit().mutatorAdd(wire);
				
			}
		}
		
		
		
		
	}

}
