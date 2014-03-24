/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.main;

import java.util.List;

import com.cburch.draw.toolbar.AbstractToolbarModel;
import com.cburch.draw.toolbar.ToolbarItem;
import com.cburch.logisim.gui.menu.LogisimMenuBar;
import com.cburch.logisim.util.UnmodifiableList;

class ToolboxToolbarModel extends AbstractToolbarModel
		implements MenuListener.EnabledListener {
	private LogisimToolbarItem itemAdd;
	private LogisimToolbarItem itemUp;
	private LogisimToolbarItem itemDown;
	private LogisimToolbarItem itemDelete;
	private List<ToolbarItem> items;
	
	public ToolboxToolbarModel(MenuListener menu) {
		itemAdd = new LogisimToolbarItem(menu, "projadd.gif", LogisimMenuBar.ADD_CIRCUIT,
				Strings.getter("projectAddCircuitTip"));
		itemUp = new LogisimToolbarItem(menu, "projup.gif", LogisimMenuBar.MOVE_CIRCUIT_UP,
				Strings.getter("projectMoveCircuitUpTip"));
		itemDown = new LogisimToolbarItem(menu, "projdown.gif", LogisimMenuBar.MOVE_CIRCUIT_DOWN,
				Strings.getter("projectMoveCircuitDownTip"));
		itemDelete = new LogisimToolbarItem(menu, "projdel.gif", LogisimMenuBar.REMOVE_CIRCUIT,
				Strings.getter("projectRemoveCircuitTip"));
		
		items = UnmodifiableList.create(new ToolbarItem[] {
				itemAdd,
				itemUp,
				itemDown,
				itemDelete,
			});
		
		menu.addEnabledListener(this);
	}

	@Override
	public List<ToolbarItem> getItems() {
		return items;
	}
	
	@Override
	public boolean isSelected(ToolbarItem item) {
		return false;
	}

	@Override
	public void itemSelected(ToolbarItem item) {
		if (item instanceof LogisimToolbarItem) {
			((LogisimToolbarItem) item).doAction();
		}
	}

	//
	// EnabledListener methods
	//
	public void menuEnableChanged(MenuListener source) {
		fireToolbarAppearanceChanged();
	}
}
