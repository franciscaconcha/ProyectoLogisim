/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package com.cburch.logisim.statediagram.externalDrawer.gui.environment;

import com.cburch.logisim.statediagram.externalDrawer.gui.environment.tag.CriticalTag;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.tag.EditorTag;
import com.cburch.logisim.statediagram.externalDrawer.gui.environment.tag.Tag;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.io.Serializable;
import java.util.*;


public abstract class Environment extends JPanel {


    /**
	 * Instantiates a new environment for the given object. This environment is
	 * assumed to have no native file to which to save the object. One should
	 * use the <CODE>setFile</CODE> object if this environment should have
	 * one.
	 * 
	 * @param object
	 *            assumed to be some sort of object that this environment holds;
	 *            subclasses may provide more stringent requirements for this
	 *            kind of object
	 */
	public Environment(Serializable object) {
		theMainObject = object;
		//clearDirty();
		initView();
	}

	/**
	 * Returns the main object for this environment. This is the object that was
	 * passed in for the constructor.
	 * 
	 * @return the main object for this environment
	 */
	public Serializable getObject() {
		return theMainObject;
	}

	private void initView() {
		this.setLayout(new BorderLayout());
		tabbed = new JTabbedPane();
		super.add(tabbed, BorderLayout.CENTER);
	}

	/**
	 * Adds a new component to the environment. Presumably this added component
	 * has some relevance to the current diagram or grammar held by the
	 * environment, though this is not strictly required.
	 * 
	 * @param component
	 *            the component to add, which should be unique for this
	 *            environment
	 * @param name
	 *            the name this component should be labeled with, which is not
	 *            necessarily a unique label
	 * @param tags
	 *            the tags associated with the component, or just a raw <CODE>Tag</CODE>
	 *            implementor if this component has no special tags associated
	 *            with it
	 * @see gui.environment.tag
	 */
	public void add(Component component, String name, Tag tags) {
	//	componentTags.put(component, tags);
		tabbed.addTab(name, component);

	}

	/**
	 * Deactivates or activates editor tagged objects in this environment.
	 * 
	 * @param enabled
	 *            <CODE>true</CODE> if editor tagged objects should be
	 *            enabled, <CODE>false</CODE> if editor tagged objects should
	 *            be disabled
	 */
	public void setEnabledEditorTagged(boolean enabled) {
		for (int i = 0; i < tabbed.getTabCount(); i++) {
			Component c = tabbed.getComponentAt(i);
			if (((Tag) componentTags.get(c)) instanceof EditorTag)
				tabbed.setEnabledAt(i, enabled);
		}
	}


	/**
	 * Programmatically sets the currently active component in this environment.
	 * 
	 * @param component
	 *            the component to make active
	 * @see #getActive
	 */
	public void setActive(Component component) {
		tabbed.setSelectedComponent(component);
		// The change event should be automatically distributed by the
		// model of the tabbed pane
	}

	/**
	 * Returns the currently active component in this environment.
	 * 
	 * @return the currently active component in this environment
	 * @see #setActive
	 */
	public Component getActive() {
		return tabbed.getSelectedComponent();
	}


	/**
	 * Distributes a change event to all listeners.
	 */
	protected void distributeChangeEvent() {
		
		ChangeEvent e = new ChangeEvent(this);
		Iterator it = (new HashSet(changeListeners)).iterator();
		while (it.hasNext())
			((ChangeListener) it.next()).stateChanged(e);
	}

	/**
	 * Removes a component from this environment.
	 * 
	 * @param component
	 *            the component to remove
	 */
	public void remove(Component component) {
		tabbed.remove(component);
		Tag tag = (Tag) componentTags.remove(component);

		// Takes care of the deactivation of EditorTag tagged
		// components in the event that such action is appropriate.
		if (tag instanceof CriticalTag) {
			criticalObjects--;
			if (criticalObjects == 0)
				setEnabledEditorTagged(true);
		}

		distributeChangeEvent();
	}

	/**
	 * Returns an array containing all components.
	 * 
	 * @return an array containing all components.
	 */
	public Component[] getComponents() {
		Component[] comps = new Component[tabbed.getTabCount()];
		for (int i = 0; i < comps.length; i++)
			comps[i] = tabbed.getComponentAt(i);
		return comps;
	}

	/** The encoder for this document. */
	//private Encoder encoder = null;

	/** The mapping of components to their respective tag objects. */
	private HashMap componentTags = new HashMap();

	/** The tabbed pane for this environment. */
	public JTabbedPane tabbed;

	/** The collection of change listeners for this object. */
	private transient HashSet changeListeners = new HashSet();

	/** The object that this environment centers on. */
	private Serializable theMainObject;
	/**
	 * The number of "CriticalTag" tagged components. Hokey but fast.
	 */
	private int criticalObjects = 0;

	/** The dirty bit. */
	private boolean dirty = false;
}
