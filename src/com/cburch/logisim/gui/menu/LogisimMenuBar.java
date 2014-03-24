/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Simulator;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.util.LocaleListener;
import com.cburch.logisim.util.LocaleManager;
import com.cburch.logisim.util.WindowMenu;

public class LogisimMenuBar extends JMenuBar {
	public static final LogisimMenuItem PRINT = new LogisimMenuItem("Print");
	public static final LogisimMenuItem EXPORT_IMAGE = new LogisimMenuItem("ExportImage");
	public static final LogisimMenuItem CUT = new LogisimMenuItem("Cut");
	public static final LogisimMenuItem COPY = new LogisimMenuItem("Copy");
	public static final LogisimMenuItem PASTE = new LogisimMenuItem("Paste");
	public static final LogisimMenuItem DELETE = new LogisimMenuItem("Delete");
	public static final LogisimMenuItem DUPLICATE = new LogisimMenuItem("Duplicate");
	public static final LogisimMenuItem SELECT_ALL = new LogisimMenuItem("SelectAll");
	public static final LogisimMenuItem RAISE = new LogisimMenuItem("Raise");
	public static final LogisimMenuItem LOWER = new LogisimMenuItem("Lower");
	public static final LogisimMenuItem RAISE_TOP = new LogisimMenuItem("RaiseTop");
	public static final LogisimMenuItem LOWER_BOTTOM = new LogisimMenuItem("LowerBottom");
	public static final LogisimMenuItem ADD_CONTROL = new LogisimMenuItem("AddControl");
	public static final LogisimMenuItem REMOVE_CONTROL = new LogisimMenuItem("RemoveControl");
	
	public static final LogisimMenuItem ADD_CIRCUIT = new LogisimMenuItem("AddCircuit");
	public static final LogisimMenuItem MOVE_CIRCUIT_UP = new LogisimMenuItem("MoveCircuitUp");
	public static final LogisimMenuItem MOVE_CIRCUIT_DOWN = new LogisimMenuItem("MoveCircuitDown");
	public static final LogisimMenuItem SET_MAIN_CIRCUIT = new LogisimMenuItem("SetMainCircuit");
	public static final LogisimMenuItem REMOVE_CIRCUIT = new LogisimMenuItem("RemoveCircuit");
	public static final LogisimMenuItem EDIT_LAYOUT = new LogisimMenuItem("EditLayout");
	public static final LogisimMenuItem EDIT_APPEARANCE = new LogisimMenuItem("EditAppearance");
	public static final LogisimMenuItem VIEW_TOOLBOX = new LogisimMenuItem("ViewToolbox");
	public static final LogisimMenuItem VIEW_SIMULATION = new LogisimMenuItem("ViewSimulation");
	public static final LogisimMenuItem REVERT_APPEARANCE = new LogisimMenuItem("RevertAppearance");
	public static final LogisimMenuItem ANALYZE_CIRCUIT = new LogisimMenuItem("AnalyzeCircuit");
	public static final LogisimMenuItem CIRCUIT_STATS = new LogisimMenuItem("GetCircuitStatistics");
	
	public static final LogisimMenuItem SIMULATE_ENABLE = new LogisimMenuItem("SimulateEnable");
	public static final LogisimMenuItem SIMULATE_STEP = new LogisimMenuItem("SimulateStep");
	public static final LogisimMenuItem TICK_ENABLE = new LogisimMenuItem("TickEnable");
	public static final LogisimMenuItem TICK_STEP = new LogisimMenuItem("TickStep");

	private class MyListener implements LocaleListener {
		public void localeChanged() {
			file.localeChanged();
			edit.localeChanged();
			project.localeChanged();
			simulate.localeChanged();
			help.localeChanged();
		}
	}
	
	private JFrame parent;
	private MyListener listener;
	private Project proj;
	private SimulateListener simulateListener = null;
	private HashMap<LogisimMenuItem,MenuItem> menuItems
		= new HashMap<LogisimMenuItem,MenuItem>();
	private ArrayList<ChangeListener> enableListeners;
	
	private MenuFile file;
	private MenuEdit edit;
	private MenuProject project;
	private MenuSimulate simulate;
	private MenuHelp help;
	
	public LogisimMenuBar(JFrame parent, Project proj) {
		this.parent = parent;
		this.listener = new MyListener();
		this.proj = proj;
		this.enableListeners = new ArrayList<ChangeListener>();
		
		add(file = new MenuFile(this));
		add(edit = new MenuEdit(this));
		add(project = new MenuProject(this));
		add(simulate = new MenuSimulate(this));
		add(new WindowMenu(parent));
		add(help = new MenuHelp(this));
		
		LocaleManager.addLocaleListener(listener);
		listener.localeChanged();
	}
	
	public void setEnabled(LogisimMenuItem which, boolean value) {
		MenuItem item = menuItems.get(which);
		if (item != null) item.setEnabled(value);
	}
	
	public void addActionListener(LogisimMenuItem which, ActionListener l) {
		MenuItem item = menuItems.get(which);
		if (item != null) item.addActionListener(l);
	}
	
	public void removeActionListener(LogisimMenuItem which, ActionListener l) {
		MenuItem item = menuItems.get(which);
		if (item != null) item.removeActionListener(l);
	}
	
	public void addEnableListener(ChangeListener l) {
		enableListeners.add(l);
	}
	
	public void removeEnableListener(ChangeListener l) {
		enableListeners.remove(l);
	}
	
	void fireEnableChanged() {
		ChangeEvent e = new ChangeEvent(this);
		for (ChangeListener listener : enableListeners) {
			listener.stateChanged(e);
		}
	}
	
	public void setSimulateListener(SimulateListener l) {
		simulateListener = l;
	}
	
	public void setCircuitState(Simulator sim, CircuitState state) {
		simulate.setCurrentState(sim, state);
	}

	public Project getProject() {
		return proj;
	}
	
	JFrame getParentWindow() {
		return parent;
	}
	
	void registerItem(LogisimMenuItem which, MenuItem item) {
		menuItems.put(which, item);
	}
	
	void fireStateChanged(Simulator sim, CircuitState state) {
		if (simulateListener != null) {
			simulateListener.stateChangeRequested(sim, state);
		}
	}
	
	public void doAction(LogisimMenuItem which) {
		MenuItem item = menuItems.get(which);
		item.actionPerformed(new ActionEvent(item, ActionEvent.ACTION_PERFORMED,
				which.toString()));
	}
	
	public boolean isEnabled(LogisimMenuItem item) {
		MenuItem menuItem = menuItems.get(item);
		return menuItem != null && menuItem.isEnabled();
	}
}
