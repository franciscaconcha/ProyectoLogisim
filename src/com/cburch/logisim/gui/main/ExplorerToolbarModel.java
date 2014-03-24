/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.main;

import java.util.List;

import com.cburch.draw.toolbar.AbstractToolbarModel;
import com.cburch.draw.toolbar.ToolbarItem;
import com.cburch.draw.toolbar.ToolbarSeparator;
import com.cburch.logisim.gui.menu.LogisimMenuBar;
import com.cburch.logisim.util.UnmodifiableList;

class ExplorerToolbarModel extends AbstractToolbarModel
		implements MenuListener.EnabledListener {
	private Frame frame;
	private LogisimToolbarItem itemToolbox;
	private LogisimToolbarItem itemSimulation;
	private LogisimToolbarItem itemLayout;
	private LogisimToolbarItem itemAppearance;
	private List<ToolbarItem> items;
	
	public ExplorerToolbarModel(Frame frame, MenuListener menu) {
		this.frame = frame;
		
		itemToolbox = new LogisimToolbarItem(menu, "projtool.gif",
				LogisimMenuBar.VIEW_TOOLBOX, Strings.getter("projectViewToolboxTip"));
		itemSimulation = new LogisimToolbarItem(menu, "projsim.gif",
				LogisimMenuBar.VIEW_SIMULATION, Strings.getter("projectViewSimulationTip"));
		itemLayout = new LogisimToolbarItem(menu, "projlayo.gif",
				LogisimMenuBar.EDIT_LAYOUT, Strings.getter("projectEditLayoutTip"));
		itemAppearance = new LogisimToolbarItem(menu, "projapp.gif",
				LogisimMenuBar.EDIT_APPEARANCE, Strings.getter("projectEditAppearanceTip"));
		
		items = UnmodifiableList.create(new ToolbarItem[] {
				itemToolbox,
				itemSimulation,
				new ToolbarSeparator(4),
				itemLayout,
				itemAppearance,
			});
		
		menu.addEnabledListener(this);
	}

	@Override
	public List<ToolbarItem> getItems() {
		return items;
	}
	
	@Override
	public boolean isSelected(ToolbarItem item) {
		if (item == itemLayout) {
			return frame.getEditorView().equals(Frame.EDIT_LAYOUT);
		} else if (item == itemAppearance) {
			return frame.getEditorView().equals(Frame.EDIT_APPEARANCE);
		} else if (item == itemToolbox) {
			return frame.getExplorerView().equals(Frame.VIEW_TOOLBOX);
		} else if (item == itemSimulation) {
			return frame.getExplorerView().equals(Frame.VIEW_SIMULATION);
		} else {
			return false;
		}
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
