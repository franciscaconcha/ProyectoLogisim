package com.cburch.logisim.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.std.wiring.Wiring;
import com.cburch.logisim.tools.AddTool;

public class ActionItemError2 implements ActionListener {

	private Project proj;

	public ActionItemError2(Project proj) {
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Wiring w = new Wiring();
		Canvas canvas = proj.getFrame().getCanvas();
		AddTool at = (AddTool) w.getTool("Pin");
		AttributeSet atrSet = at.getAttributeSet();
		//Revisar como alterar el numero de bits de datos
		atrSet.setValue(Pin.ATTR_TRISTATE, false);
		at.setAttributeSet(atrSet); 
		at.setState(canvas, 2);
		//Definir si es variable la posici�n
		//Como identificar la posicon
		
		MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0, 170, 44, 50, 50, 1, false, 1); 
		at.mouseReleased(canvas,canvas.getGraphics() , mouseEvent);
		canvas.completeAction();
		
		CircuitState state = proj.getCircuitState();
		
	}

}
