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

import javax.swing.*;
import java.awt.*;

/**
 * The <CODE>EnvironmentFrame</CODE> is the general sort of frame for holding
 * an environment.
 * 
 * @author Thomas Finley
 */

public class EnvironmentFrame extends JFrame {
	/**
	 * Instantiates a new <CODE>EnvironmentFrame</CODE>. This does not fill
	 * the environment with anything.
	 * 
	 * @param environment
	 *            the environment that the frame is created for
	 */
	public EnvironmentFrame(Environment environment) {
		this.environment = environment;

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(environment, BorderLayout.CENTER);

		this.setLocation(50, 50);
		
		//resizeWatcher();
	}

	/**
	 * Returns the environment for this frame.
	 * 
	 * @return the environment for this frame
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/** The environment that this frame displays. */
	private Environment environment;

	/** The number of this environment frames. */
	private int myNumber = 0xdeadbeef;

	/** The default title for these frames. */
	private static final String DEFAULT_TITLE = "JFLAP";

}
