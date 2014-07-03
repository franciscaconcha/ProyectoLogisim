package com.cburch.logisim.gui.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import com.cburch.logisim.circuit.CircuitAction;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Action;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.std.wiring.Wiring;
import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.util.StringGetter;

public class PinErrorListener implements ActionListener {

	private Project proj;

	public PinErrorListener(Project proj) {
		this.proj = proj;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Set<Wire> wires = proj.getCurrentCircuit().getWires();
		CircuitState state = proj.getCircuitState();
		ArrayList<Wire> wiresMutator = new ArrayList<Wire>();
		Canvas canvas = proj.getFrame().getCanvas();
		for (Wire wire : wires) {
			Location s = wire.getE0();
			Location t = wire.getE1();
			if (state.getValue(s).getColor().equals(new Color(192, 0, 0))) {
				Wiring w = new Wiring();
				
				AddTool at = (AddTool) w.getTool("Pin");
				AttributeSet atrSet = at.getAttributeSet();
				// Revisar como alterar el numero de bits de datos
				atrSet.setValue(Pin.ATTR_TRISTATE, false);
				at.setAttributeSet(atrSet);
				at.setState(canvas, 2);

				MouseEvent mouseEvent = new MouseEvent(canvas, 0, 0, 0,
						s.getX() - 100, s.getY(), 50, 50, 1, false, 1);
				at.mouseReleased(canvas, canvas.getGraphics(), mouseEvent);
				canvas.completeAction();

				Location inicio = Location.create(s.getX() - 100, s.getY());
				Location fin = Location.create(s.getX() - 50, s.getY());

				Wire newWire = Wire.create(inicio, fin);
				
				wiresMutator.add(newWire);
				
			}

		}
		CircuitMutation mutation = new CircuitMutation(
				canvas.getCircuit());
		mutation.addAll(wiresMutator);
		StringGetter desc;
		if (wiresMutator.size() == 1)
			desc = Strings.getter("addWireAction");
		else
			desc = Strings.getter("addWiresAction");
		Action act = mutation.toAction(desc);
		canvas.getProject().doAction(act);
		canvas.completeAction();

	}

}
