package com.cburch.logisim.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.std.wiring.Wiring;
import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Tool;

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
		atrSet.setValue(Pin.ATTR_TRISTATE, false);
		at.setAttributeSet(atrSet); 
		at.setState(canvas, 2);
		MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0, 170, 44, 50, 50, 1, false, 1); 
		at.mouseReleased(canvas,canvas.getGraphics() , mouseEvent);
		canvas.completeAction();
		System.out.println("prueba funciona");
	}

}
