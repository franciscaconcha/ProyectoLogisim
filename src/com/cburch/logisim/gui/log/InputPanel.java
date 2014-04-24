package com.cburch.logisim.gui.log;

import java.awt.LayoutManager;

/* Nueva tab para ingreso de datos, @José Garrido */

public class InputPanel extends LogPanel {

	public InputPanel(LogFrame frame) {
		super(frame);
		// TODO Auto-generated constructor stub
	}

	public InputPanel(LogFrame frame, LayoutManager manager) {
		super(frame, manager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTitle() {
		return Strings.get("inputTab");
	}

	@Override
	public String getHelpText() {
		return Strings.get("inputTabHelp");
	}

	@Override
	public void localeChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void modelChanged(Model oldModel, Model newModel) {
		// TODO Auto-generated method stub

	}

}
