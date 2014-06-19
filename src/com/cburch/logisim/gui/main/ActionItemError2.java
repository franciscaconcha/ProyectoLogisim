package com.cburch.logisim.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Wiring;
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
		Tool at = w.getTool("Pin");
		MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0, 10, 10, 0, 0, 0, false, 0); 
		at.mousePressed(canvas,canvas.getGraphics() , mouseEvent);
		canvas.completeAction();
		System.out.println("prueba funciona");
	}

}
