/* 
 * **Simplified instance of JFLAP to draw state diagrams into Logisim**

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





package com.cburch.logisim.statediagram.externalDrawer.diagram;

import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * This class represents a single state in an diagram. This class is intended
 * to act as nothing more than a simple placeholder.
 * 
 * @author Thomas Finley
 * @version 1.0
 */

public class StateObject{
	/**
	 * Instantiates a new state.
	 * 
	 * @param id
	 *            the state id, used for generating
	 * @param point
	 *            the point that the center of the state will be at in the
	 *            canvas
	 * @param diagram
	 *            the diagram this belongs to
	 */
	public StateObject(int id, Point point, Diagram diagram) {
		this.point = point;

		this.id = id;
		this.diagram = diagram;
	}


	/**
	 * Returns the point this state is centered on in the canvas.
	 * 
	 * @see #setPoint(Point)
	 * @return the point this state is centered on in the canvas
	 */
	public Point getPoint() {
		return point;
	}

	public void setDiagram(Diagram auto) {
		this.diagram = auto;
	}

	/**
	 * Returns the diagram that this state belongs to.
	 * 
	 * @return the diagram that this state belongs to
	 */
	public Diagram getDiagram() {
		return diagram;
	}

	/**
	 * Sets the point for this state.
	 * 
	 * @param point
	 *            the point this state is moving to
	 * @see #getPoint()
	 */
	public void setPoint(Point point) {
		this.point = point;

		getDiagram()
				.distributeStateEvent(
						new StateEvent(getDiagram(), this, false,
								true, false));
	}
	


	/**
	 * Returns the state ID for this state.
	 * 
	 * @return the ID of the state
	 */
	public int getID() {
		return id;
	}


	/**
	 * Returns a string representation of this object. The string representation
	 * contains the ID and the point of the state. If the ID is <CODE>5</CODE>
	 * and the point object is at <CODE>(50,80)</CODE>, then the string
	 * representation will be </CODE>"q_5 at (50,80)"</CODE>
	 */
	public String toString() {
		return "q_" + Integer.toString(getID()) + " at ("
				+ Integer.toString(getPoint().x) + ","
				+ Integer.toString(getPoint().y) + ")" + " label: "
				+ getLabel();
	}

	/**
	 * Sets the name for this state. A parameter of <CODE>null</CODE> will
	 * reset this to the default.
	 * 
	 * @param name
	 *            the new name for the state
	 */
	public void setName(String name) {
		this.name = name;
		getDiagram()
				.distributeStateEvent(
						new StateEvent(getDiagram(), this, false,
								false, true));
	}

	/**
	 * Returns the simple "name" for this state. By default this will simply be
	 * "qd", where d is the ID number.
	 * 
	 * @return the name for this state
	 */
	public String getName() {
		if (name == null) {
			name = "q" + Integer.toString(getID());
		}
		return name;
	}

	/**
	 * Sets the "label" for a state, an optional description for the state.
	 * 
	 * @param label
	 *            the new descriptive label, or <CODE>null</CODE> if the user
	 *            wishes to specify that there is no label
	 */
	public void setLabel(String label) {
		this.label = label;
		if (label == null) {
			labels = new String[0];
		} else {
			StringTokenizer st = new StringTokenizer(label, "\n");
			ArrayList<String> lines = new ArrayList<String>();
			while (st.hasMoreTokens())
				lines.add(st.nextToken());
			labels = (String[]) lines.toArray(new String[0]);
		}
		getDiagram()
				.distributeStateEvent(
						new StateEvent(getDiagram(), this, false,
								false, true));
	}

	/**
	 * Returns the label for the state.
	 * 
	 * @return the descriptive label of the state, or <CODE>null</CODE> if
	 *         this state has no label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns the label for the state, broken across newlines if there are
	 * newlines in it.
	 * 
	 * @return an array of all label elements, or an empty array if this state
	 *         has no labels
	 */
	public String[] getLabels() {
		return labels;
	}

    public int specialHash(){

         return point.hashCode();
    }
    
    public StateObject copy(){
    	
    	Point newPoint = new Point();
    	newPoint.setLocation(point.getX(), point.getY());
    	return new StateObject(this.id, newPoint, this.diagram);

    }



	/** The point where this state is to be drawn. */
	private Point point;

	/** The state ID. */
	int id;

	/** The name of the state. */
	String name = null;

	/** The diagram this state belongs to. */
	private Diagram diagram = null;

	/** The label for the state. */
	private String label;

	/** If there are multiple labels, return those. */
	private String[] labels = new String[0];
	
	private boolean selected = false;

	public void setSelect(boolean select) {
		selected = select;
	}
	
	public boolean isSelected(){
		return selected;
	}

}