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




package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
* @author Henry Qin and Jonathan Su
*/

public class AutomatonSizeSlider extends JSlider{
    //Set up animation parameters.
    static final int AUTOMATON_SIZE_MIN = 1;
    static final int AUTOMATON_SIZE_MAX = 800;
    static final int AUTOMATON_SIZE_INIT = 220;
    static final String AUTOMATON_SIZE_TITLE = "Diagram Size";
    
	/** The view we receive events from. */
	private DiagramPane view;
	
	/**
	 * Constructs the AutomatonSizeSlider
	 * @param view
	 * @param drawer
	 */
	public AutomatonSizeSlider(DiagramPane view, DiagramDrawer drawer) {
		super(AUTOMATON_SIZE_MIN, AUTOMATON_SIZE_MAX, AUTOMATON_SIZE_INIT); 
		this.view = view;
	    this.addChangeListener(new SliderListener());
	    setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), AUTOMATON_SIZE_TITLE));
	}


      class SliderListener implements ChangeListener {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider)e.getSource();
                    double pass = source.getValue()*1./AUTOMATON_SIZE_INIT;
                    view.setScale(pass);
                    view.requestTransform();
            }
      }

}
