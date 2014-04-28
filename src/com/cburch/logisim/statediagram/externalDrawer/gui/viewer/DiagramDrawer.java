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





package com.cburch.logisim.statediagram.externalDrawer.gui.viewer;

import com.cburch.logisim.statediagram.Transition;
import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateListener;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionListener;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;

/**
 * This is the very basic class of an Diagram drawer. It has facilities to
 * draw the Diagram. Subclasses may be derived to have finer control over how
 * things are drawn.
 * 
 * @author Thomas Finley
 * @version 1.0
 */

public class DiagramDrawer {
	/**
	 * Instantiates an object to draw an diagram.
	 * 
	 * @param diagram
	 *            the diagram to handle
	 */
	public DiagramDrawer(Diagram diagram) {
		this.diagram = diagram;
		DrawerListener listener = new DrawerListener();
		getDiagram().addStateListener(listener);
		getDiagram().addTransitionListener(listener);

	}

	/**
	 * Retrieves the <CODE>Diagram</CODE> handled by this drawer.
	 * 
	 * @return the <CODE>Diagram</CODE> handled by this drawer
	 */
	public Diagram getDiagram() {
		return diagram;
	}

    //naive optimization for drawing
    ArrayList<StateObject> hs = new ArrayList<StateObject>();
    HashSet<Point> lhs = new HashSet<Point>();
    int specHash = Integer.MIN_VALUE;
    //


	/**
	 * Draws our diagram.
	 * 
	 * @param g2
	 *            the Graphics object to draw the diagram on
	 */
	public void drawDiagram(Graphics g2) {
		if (!valid)
			refreshArrowMap();

		Graphics2D g = (Graphics2D) g2.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(g.getFont().deriveFont(12.0f));

		// Draw transitions between states.
		g.setColor(Color.black);
		drawTransitions(g);
        
        StateObject[] states = diagram.getStates();
        for (int i = 0; i < states.length; i++){
            drawState(g, states[i]);
        }
		
		
		this.drawSelectionBox(g);
		g.dispose();
	}

	
	/**
	 * Returns the bounds for an individual state.
	 * 
	 * @param state
	 *            the state to get the bounds for
	 * @return the rectangle that the state needs to be in to completely enclose
	 *         itself
	 */
	public Rectangle getBounds(StateObject state) {

		int radius = statedrawer.getRadius(); //getScaleX and Y should be same
		
		
		Point p = state.getPoint();
		
		
		
		int yAdd = state.getLabels().length * 15;

		return new Rectangle(p.x - radius, p.y - radius, radius * 2, radius * 2
				+ yAdd);
	}


	/**
	 * Returns the bounds that the diagram is drawn in.
	 * 
	 * @return the bounds that the diagram is drawn in, or <CODE>null</CODE>
	 *         if there is nothing to draw, i.e., the diagram has no states
	 */
	public Rectangle getBounds() {
		if (validBounds){
//			System.out.println("Using cache");
			return cachedBounds;
		}
		if (!valid)
			refreshArrowMap();
		StateObject[] states = getDiagram().getStates();
		if (states.length == 0)
			return null;
		Rectangle rect = getBounds(states[0]);
		for (int i = 1; i < states.length; i++)
			rect.add(getBounds(states[i]));

		Iterator it = arrowToTransitionMap.keySet().iterator();
		while (it.hasNext()) {
			CurvedArrow arrow = (CurvedArrow) it.next();
			Rectangle2D arrowBounds = arrow.getBounds();
			rect.add(arrowBounds);
		}
		validBounds = true;
//		return cachedBounds = rect;
		return cachedBounds = curTransform.createTransformedShape(rect).getBounds();
	}

	/**
	 * Draws a state on the diagram.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 * @param state
	 *            the state to draw
	 */
	protected void drawState(Graphics g, StateObject state) {
		statedrawer.drawState(g, getDiagram(), state);
		if (drawLabels) {
			statedrawer.drawStateLabel(g, state, state.getPoint(),
					StateDrawer.STATE_COLOR);
		}
	}

	/**
	 * Draws the transitions of the diagram.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	protected void drawTransitions(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		Set arrows = arrowToTransitionMap.keySet();
		Iterator it = arrows.iterator();
		while (it.hasNext()) {
			CurvedArrow arrow = (CurvedArrow) it.next();
            if (arrow.myTransition.isSelected){
                arrow.drawHighlight(g2);
                arrow.drawControlPoint(g2);
            }
            else 
                arrow.draw(g2);
		}
	}
	/**
	 * draws selection box
	 */
	protected void drawSelectionBox(Graphics g){
		g.drawRect(mySelectionBounds.x, mySelectionBounds.y, mySelectionBounds.width, mySelectionBounds.height);
	}

	/**
	 * Refreshes the <CODE>arrowToTransitionMap</CODE> structure.
	 */
	private void refreshArrowMap() {
		if (diagram == null) {
			//System.out.println("Diagram is null, how?");
			return;
		}
		StateObject[] states = diagram.getStates();
		arrowToTransitionMap.clear(); // Remove old entries.
		transitionToArrowMap.clear(); // Remove old entries.

		for (int i = 0; i < states.length; i++) {
			// This is some code that handles interstate (heh) transitions.
			for (int j = i + 1; j < states.length; j++) {
				// We want all transitions.
				TransitionObject[] itoj = diagram.getTransitionsFromStateToState(
						states[i], states[j]);
				TransitionObject[] jtoi = diagram.getTransitionsFromStateToState(
						states[j], states[i]);
				float top = jtoi.length > 0 ? 0.5f : 0.0f;
				float bottom = itoj.length > 0 ? 0.5f : 0.0f;

				if (itoj.length + jtoi.length == 0)
					continue;

				
				// Get where points should appear to emanate from.
				double angle = angle(states[i], states[j]);
				Point fromI = pointOnState(states[i], angle - ANGLE);
				Point fromJ = pointOnState(states[j], angle + Math.PI + ANGLE);
				for (int n = 0; n < itoj.length; n++) {
					if(curveTransitionMap.containsKey(itoj[n])){
						top = curveTransitionMap.get(itoj[n]);
					}
					float curvy = top+n;
					CurvedArrow arrow = n == 0 ? new CurvedArrow(fromI, fromJ,
							curvy, itoj[n]) : new InvisibleCurvedArrow(fromI, fromJ,
							curvy, itoj[n]);


					arrow.setLabel(itoj[n].getDescription());

					arrowToTransitionMap.put(arrow, itoj[n]);
					transitionToArrowMap.put(itoj[n], arrow);
				}
				fromI = pointOnState(states[i], angle + ANGLE);
				fromJ = pointOnState(states[j], angle + Math.PI - ANGLE);
				for (int n = 0; n < jtoi.length; n++) {
					if(curveTransitionMap.containsKey(jtoi[n])){
						bottom = curveTransitionMap.get(jtoi[n]);
					}
					float curvy = bottom+n;
					CurvedArrow arrow = n == 0 ? new CurvedArrow(fromJ, fromI,
							curvy, jtoi[n]) : new InvisibleCurvedArrow(fromJ, fromI,
							curvy, jtoi[n]);
					String label = jtoi[n].getDescription();


					arrow.setLabel(label);
					arrowToTransitionMap.put(arrow, jtoi[n]);
					transitionToArrowMap.put(jtoi[n], arrow);
				}
			}
			// Now handle transitions between a single state.
			TransitionObject[] trans = diagram.getTransitionsFromStateToState(
					states[i], states[i]);
			if (trans.length == 0)
				continue;
			Point from = pointOnState(states[i], -Math.PI * 0.333);
			Point to = pointOnState(states[i], -Math.PI * 0.667);
			for (int n = 0; n < trans.length; n++) {
				if(selfTransitionMap.containsKey(trans[n])){
					//EDebug.print(selfTransitionMap);
					Point storedfrom = pointOnState(states[i], (selfTransitionMap.get(trans[n])+Math.PI*.166));
					Point storedto = pointOnState(states[i], (selfTransitionMap.get(trans[n])-Math.PI*.166));
					CurvedArrow arrow = n == 0 ? new CurvedArrow(storedfrom, storedto, -2.0f, trans[n])
					: new InvisibleCurvedArrow(storedfrom, storedto, -2.0f - n, trans[n]);


					arrow.setLabel(trans[n].getDescription());
					arrowToTransitionMap.put(arrow, trans[n]);
					transitionToArrowMap.put(trans[n], arrow);
				}else{
					//EDebug.print(selfTransitionMap);
					selfTransitionMap.put(trans[n], -Math.PI*.5);
					CurvedArrow arrow = n == 0 ? new CurvedArrow(from, to, -2.0f, trans[n])
						: new InvisibleCurvedArrow(from, to, -2.0f - n, trans[n]);

                    //INSERTED for TransitionGUI
                    arrow.myTransition = trans[n];
                    //END INSERTED for TransitionGUI
                    //MERLIN MERLIN MERLIN MERLIN MERLIN//


					arrow.setLabel(trans[n].getDescription());
					arrowToTransitionMap.put(arrow, trans[n]);
					transitionToArrowMap.put(trans[n], arrow);
				}
			}
		}
		valid = true;
	}


	/**
	 * What is the angle on state1 of the point closest to state2?
	 * 
	 * @param state1
	 *            the first state
	 * @param state2
	 *            the second state
	 * @return the angle on state1 of the point closest to state2
	 */
	private double angle(StateObject state1, StateObject state2) {
		Point p1 = state1.getPoint();
		Point p2 = state2.getPoint();
		double x = (double) (p2.x - p1.x);
		double y = (double) (p2.y - p1.y);
		return Math.atan2(y, x);
	}

	/**
	 * Given a state and an angle, if we treat the state as a circle, what point
	 * does that angle represent?
	 * 
	 * @param state
	 *            the state
	 * @param angle
	 *            the angle on the state
	 * @return the point on the outside of the state with this angle
	 */
	public Point pointOnState(StateObject state, double angle) {
		Point point = new Point(state.getPoint());
		double x = Math.cos(angle) * (double) StateDrawer.STATE_RADIUS;
		double y = Math.sin(angle) * (double) StateDrawer.STATE_RADIUS;
		point.translate((int) x, (int) y);
		return point;
	}

	/**
	 * Informs the drawer that states in the automata have changed to the point
	 * where a redraw is appropriate.
	 */
	public void invalidate() {
		valid = false;
		this.invalidateBounds();
	}

	/**
	 * Informs the drawer that it should recalculate the bounds the next time
	 * they are requested. This method is called automatically if the diagram
	 * changes.
	 */
	public void invalidateBounds() {
		validBounds = false;
	}

	/**
	 * Gets the state at a particular point.
	 * 
	 * @param point
	 *            the point to check
	 * @return a <CODE>State</CODE> object at this particular point, or <CODE>null</CODE>
	 *         if no state is at this point
	 */
	public StateObject stateAtPoint(Point point) {
		StateObject[] states = getDiagram().getStates();
		// Work backwards, since we want to select the "top" state,
		// and states are drawn forwards so later is on top.
		for (int i = states.length - 1; i >= 0; i--)
			if (point.distance(states[i].getPoint()) <= StateDrawer.STATE_RADIUS)
				return states[i];
		// Not found. Drat!
		return null;
	}

	/**
	 * Gets the transition at a particular point.
	 * 
	 * @param point
	 *            the point to check
	 * @return a <CODE>Transition</CODE> object at this particular point, or
	 *         <CODE>null</CODE> if no transition is at this point
	 */
	public TransitionObject transitionAtPoint(Point point) {
		if (!valid)
			refreshArrowMap();
		Set arrows = arrowToTransitionMap.keySet();
		Iterator it = arrows.iterator();
		while (it.hasNext()) {
			CurvedArrow arrow = (CurvedArrow) it.next();
			if (arrow.isNear(point, 2))
				return (TransitionObject) arrowToTransitionMap.get(arrow);
		}
		return null;
	}
	

	/**
	 * Returns the state drawer.
	 * 
	 * @return the state drawer
	 */
	public StateDrawer getStateDrawer() {
		return statedrawer;
	}

	/**
	 * Listens for changes in transitions of our diagram. This method is
	 * called by the internal diagram listener for this object, and while not
	 * called directly by the diagram, is passed along the same event.
	 * 
	 * @param event
	 *            the transition event
	 */
	protected void transitionChange(TransitionEvent event) {
		invalidate();
	}

	/**
	 * Listens for changes in states of our diagram. This method is called by
	 * the internal diagram listener for this object, and while not called
	 * directly by the diagram, is passed along the same event.
	 * 
	 * @param event
	 *            the state event
	 */
	protected void stateChange(StateEvent event) {
		if (event.isMove())
			invalidate();
		else
			invalidateBounds();
	}

	/**
	 * Returns the curved arrow object that represents a particular transition.
	 * 
	 * @param t
	 *            the transition to find the arrow for
	 * @return the curved arrow object that is used to draw this transition
	 */
	protected CurvedArrow arrowForTransition(TransitionObject t) {
		return (CurvedArrow) transitionToArrowMap.get(t);
	}

	/**
	 * Returns if state labels are drawn in the diagram.
	 * 
	 * @return if state labels are drawn in the diagram
	 */
	public boolean doesDrawStateLabels() {
		return drawLabels;
	}

	/**
	 * Sets if state labels should be drawn in the diagram or not.
	 * 
	 * @param drawLabels
	 *            <CODE>true</CODE> if state labels should be drawn in the
	 *            state diagram, <CODE>false</CODE> if they should not be
	 */
	public void shouldDrawStateLabels(boolean drawLabels) {
		this.drawLabels = drawLabels;
	}

	public void setDiagram(Diagram newAuto) {
		if (newAuto == null) {

			//System.out.println("Setting diagram null");
			return;
		}

		diagram = newAuto;
		this.invalidate();
	}
	

	public void setSelectionBounds(Rectangle bounds) {
		mySelectionBounds = bounds;
		
	}
	public Rectangle getSelectionBounds() {
		return mySelectionBounds;
	}
	
//	public void setScale(double scale){
//	    scaleBy = scale;	
//	    validBounds = false;
//	}
	public void setTransform(AffineTransform af){
		curTransform = af;
	}
	
	private Rectangle mySelectionBounds = new Rectangle(0, 0, -1, -1);

	/** The diagram we're handling. */
	private Diagram diagram;

	/** If we should draw state labels or not. */
	private boolean drawLabels = true;

	/**
	 * The difference in angle from the emination point of the transitions from
	 * the point closest to the other state.
	 */
	protected static final double ANGLE = Math.PI / 25.0;

	/**
	 * Whether or not the drawing objects should be redone on the next draw.
	 */
	private boolean valid = false;

	/**
	 * If any change happens at all that could effect the bounds, this is
	 * changed.
	 */
	private boolean validBounds = false;

	/** The cached bounds. */
	private Rectangle cachedBounds = null;
	
	/**
	 * A map of self transitions mapped to their angle of appearance.
	 */
	public HashMap<TransitionObject, Double> selfTransitionMap = new HashMap();
	
	/**
	 * Map of curvatures for transitions
	 */
	public HashMap<Transition, Float> curveTransitionMap = new HashMap();

	/**
	 * A map of curved arrows to transitions. This object is also used for
	 * iteration over all arrows when drawing must be done
	 */
	public HashMap arrowToTransitionMap = new HashMap();

	/** The map from transitions to their respective arrows. */
	public HashMap transitionToArrowMap = new HashMap();

	/** The state drawer. */
	public StateDrawer statedrawer = new StateDrawer();
	
//	/**Amount to scale by, purely for scroll calclulation*/
//	private double scaleBy = 1;
	
	/**The transform instead*/
	private AffineTransform curTransform = new AffineTransform();
	

	/**
	 * This diagram listener takes care of responding to the events.
	 */
	private class DrawerListener implements StateListener,
            TransitionListener {
		/**
		 * Listens for changes in transitions of our diagram.
		 * 
		 * @param event
		 *            the transition event
		 */
		public void diagramTransitionChange(TransitionEvent event) {
			transitionChange(event);
		}

		/**
		 * Listens for changes in states of our diagram.
		 * 
		 * @param event
		 *            the state event
		 */
		public void diagramStateChange(StateEvent event) {
			stateChange(event);
		}
	}

}
