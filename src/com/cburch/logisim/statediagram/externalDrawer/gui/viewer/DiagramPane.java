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

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.StateListener;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionEvent;
import com.cburch.logisim.statediagram.externalDrawer.diagram.event.TransitionListener;
import com.cburch.logisim.statediagram.externalDrawer.gui.JMultiLineToolTip;
import com.cburch.logisim.statediagram.externalDrawer.gui.editor.EditorPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;


/**
 * A simple view that draws an diagram.
 * 
 * @author Thomas Finley
 */

public class DiagramPane extends JPanel implements Scrollable {
	/**
	 * Instantiates an DiagramPane.
	 * 
	 * @param drawer
	 *            the diagram drawer
	 */
	public DiagramPane(DiagramDrawer drawer) {
		this(drawer, true);
		this.setLayout(null);
	}

	/**
	 * Since labels support multiple lines, so too must the tool tips for this
	 * component.
	 * 
	 * @return a multiline tool tip
	 */
	public JToolTip createToolTip() {
		return new JMultiLineToolTip();
	}

	/**
	 * Returns the location for a tool tip to display.
	 * 
	 * @return the location for a tool tip
	 */
	public Point getToolTipLocation(MouseEvent event) {
		try {
			return transformFromDiagramToView(drawer.stateAtPoint(
                    event.getPoint()).getPoint());
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Return the text for a tool tip.
	 * 
	 * @return text for a tool tip
	 */
	public String getToolTipText(MouseEvent event) {
		if (!drawer.doesDrawStateLabels()) {
			StateObject s = drawer.stateAtPoint(event.getPoint());
			if (s == null)
				return null;
			return s.getLabel();
		}
		return null;
	}

	/**
	 * Instantiates an DiagramPane.
	 * 
	 * @param drawer
	 *            the diagram drawer
	 * @param adapt
	 *            whether or not to adapt the size of the view
	 */
	public DiagramPane(DiagramDrawer drawer, boolean adapt) {
		super();
		this.drawer = drawer;
		this.adapt = adapt;
		setPreferredSize(new Dimension(400, 300));
		Listener listener = new Listener();
		drawer.getDiagram().addStateListener(listener);
		drawer.getDiagram().addTransitionListener(listener);
		addComponentListener(listener);
		setToolTipText("Beavis"); // Tool tips require some text. :P
		setOpaque(true);
	}

	/**
	 * Draws itself, and the diagram.
	 * 
	 * @param g
	 *            the graphics object to draw upon
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (transformNeedsReform)
			reformTransform(new Rectangle(getSize()));
		g.setColor(java.awt.Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		Graphics2D g2 = (Graphics2D) g;

		g2.transform(transform);
		drawer.drawDiagram(g);

	}

	/**
	 * Prints itself, with the diagram drawn so that it fills the space.
	 *
	 * @param g
	 *            the graphics interface for the printer device
	 */

	public void printComponent(Graphics g) {
//		boolean oldAdapt = adapt;
//		adapt = true;

		if (transformNeedsReform)
			reformTransform(g.getClipBounds());
		g.setColor(java.awt.Color.white);
		g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);
        
		Graphics2D g2 = (Graphics2D) g;
		g2.transform(transform);
        drawer.invalidate();
		drawer.drawDiagram(g);

	}

	/**
	 * Returns a rectangle that roughly bounds the diagram, or the section of
	 * the diagram that should be shown if not all should be shown.
	 * 
	 * @return a rectangle that bounds the diagram
	 */
	protected Rectangle getDiagramBounds() {
		Rectangle rect = drawer.getBounds();
		if (rect == null)
			return new Rectangle(getSize());
		return rect;
	}

	/**
	 * Reforms the transform.
	 * 
	 * @param bounds
	 *            the current bounds of the drawing area, which may be used if
	 *            this component adapts it's size
	 */
	public void reformTransform(Rectangle viewBounds) {

	    
		transformNeedsReform = false;
//		System.out.print("hello\n");
		Rectangle bounds = new Rectangle(getDiagramBounds());
		if (!adapt) {
			// Much of this is to make sure that this component
			// displays properly in a scroll box. (It must work
			// either in a scroll box, or not!)
			Rectangle visible = getVisibleRect();
			// What is the point of the upper left corner of the
			// viewport in diagram drawer space?
			Point viewportUpperLeft = new Point((int) (visible.x - transform
					.getTranslateX()), (int) (visible.y - transform
					.getTranslateY()));
			// What is the point of the upper left corner of the
			// scrolling component in diagram drawer space?
			Point componentUpperLeft = new Point(Math.min(bounds.x, Math.min(0,
					viewportUpperLeft.x)), Math.min(bounds.y, Math.min(0,
					viewportUpperLeft.y)));
			// Make the transform draw the diagram in the
			// appropriate location.
			transform = new AffineTransform();
		    //push to drawer
			drawer.setTransform(transform);
			drawer.invalidateBounds();
			
			transform.translate(-componentUpperLeft.x, -componentUpperLeft.y);
			
		    
			//inserted by Henry
//			if (scaleBy > 0){
//                System.out.println("scaling now");
			    transform.scale(scaleBy, scaleBy); 	 //always and forever, you should scale
//				scaleBy = -1;
//			}
			
			
			// Set the size of the thing appropriately.
			Dimension newSize = new Dimension(Math.max(bounds.width + bounds.x,
					viewportUpperLeft.x + visible.width)
					- componentUpperLeft.x, Math.max(bounds.height + bounds.y,
					viewportUpperLeft.y + visible.height)
					- componentUpperLeft.y);
			if (newSize.equals(getPreferredSize()))
				return;
			
			
			setPreferredSize(newSize);

			revalidate();
			scrollRectToVisible(visible);
			
			return;
		}

		// We do make the diagram fit in the visible space!
		transform = new AffineTransform();
		bounds.grow(20, 20);
		Rectangle ourBounds = viewBounds;
		double aRatio = bounds.getWidth() / bounds.getHeight();
		double vRatio = ourBounds.getWidth() / ourBounds.getHeight();
		if (aRatio > vRatio) {
			// The diagram is wider than the view.
			double targetHeight = bounds.getWidth() / vRatio;
			targetHeight -= bounds.getHeight();
			// Must extend by targetHeight.
			bounds.setRect(bounds.getX(), bounds.getY() - targetHeight / 2.0,
					bounds.getWidth(), bounds.getHeight() + targetHeight);
		} else {
			// The diagram is taller than the view.
			double targetWidth = bounds.getHeight() * vRatio;
			targetWidth -= bounds.getWidth();
			// Extend by targetWidth.
			bounds.setRect(bounds.getX() - targetWidth / 2.0, bounds.getY(),
					bounds.getWidth() + targetWidth, bounds.getHeight());
		}
		double scale = ourBounds.getWidth() / bounds.getWidth();
		transform.scale(scale, scale);
		System.out.println(scale+"..."+(ourBounds.getX() - bounds.getX()));
		transform.translate(ourBounds.getX() - bounds.getX(), ourBounds.getY()
				- bounds.getY());
		
	}

	/**
	 * If an diagram changes, repaint this object.
	 */
	private class Listener extends ComponentAdapter implements
            StateListener, TransitionListener {
		public void diagramTransitionChange(TransitionEvent e) {
			transformNeedsReform = true;
			repaint();
		}

		public void diagramStateChange(StateEvent e) {
			transformNeedsReform = true;
			repaint();
		}

		public void componentResized(ComponentEvent e) {
			if (adapt)
				transformNeedsReform = true;
			repaint();
		}
	}

	/**
	 * Transforms a mouse event to have its mouse click location reflect the
	 * diagram space, not the view space.
	 * 
	 * @param event
	 *            the mouse event to transform
	 */
	public void transformMouseEvent(MouseEvent event) {
		if (transformNeedsReform)
			reformTransform(new Rectangle(getSize()));
		Point point = new Point(), ePoint = event.getPoint();
		try {
			// transform.transform(ePoint, point);
			transform.inverseTransform(ePoint, point);
			event.translatePoint(point.x - ePoint.x, point.y - ePoint.y);
		} catch (NoninvertibleTransformException e) {
			// Well, what CAN we do?
		}
	}

	/**
	 * Transforms a point from diagram space to view space.
	 * 
	 * @param point
	 *            the point, which will be modified
	 */
	public Point transformFromDiagramToView(Point point) {
		return (Point) transform.transform(point, new Point());
	}



	/**
	 * Processes mouse events with a transformed mouse event.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void processMouseEvent(MouseEvent event) {
		transformMouseEvent(event);
		super.processMouseEvent(event);
	}

	/**
	 * Processes mouse motion events with a transformed mouse event.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void processMouseMotionEvent(MouseEvent event) {
		transformMouseEvent(event);
		super.processMouseMotionEvent(event);
	}

	/**
	 * Returns the diagram drawer.
	 * 
	 * @return the diagram drawer
	 */
	public DiagramDrawer getDrawer() {
		return drawer;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return orientation == SwingConstants.VERTICAL ? visibleRect.height
				: visibleRect.width;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 5;
	}

	/**
	 * If this is an adaptive diagram pane we want this component to be the
	 * width of a viewport of course, but if this is NOT an adaptive container
	 * we want it to be stretched so its width is at least that of the viewport
	 * (to avoid clicks seemingly "not happening" to the right of automata).
	 */
	public boolean getScrollableTracksViewportWidth() {
		if (adapt)
			return true;
		return getPreferredSize().width < getParent().getSize().width;
	}

	/**
	 * If this is an adaptive diagram pane we want this component to be the
	 * height of a viewport of course, but if this is NOT an adaptive container
	 * we want it to be stretched so its height is at least that of the viewport
	 * (to avoid clicks seemingly "not happening" below automata).
	 */
	public boolean getScrollableTracksViewportHeight() {
		if (adapt)
			return true;
		return getPreferredSize().height < getParent().getSize().height;
	}


	public Component add(Component c) {
		if (c instanceof JTable)
			table = (JTable) c;
		return super.add(c);
	}

	public void setTablePoint(Point p) {
		tp = p;
	}

	public void doLayout() {
		try {
			super.doLayout();
			table.setLocation(tp);
		} catch (NullPointerException e) {
			super.doLayout();
		}
	}
    
    public void setAdapt(boolean newAdapt)
    {
        adapt = newAdapt;
        transformNeedsReform = true;
        repaint();
    }
    
    
    public boolean getAdapt()
    {
        return adapt;
     
    }
    
    public void setCreator(EditorPane pane) {
		myCreator = pane;
	}
	
	public EditorPane getCreator(){
		return myCreator;
	}
	
	public void setScale(double scale){
	    scaleBy = scale;	
//	    drawer.setScale(scale);
	}
	
	public void requestTransform(){
		transformNeedsReform = true;
		repaint();
	}
	

	private EditorPane myCreator;


	/** The table point. */
	private Point tp = null;

	/** The drawer. */
	protected DiagramDrawer drawer;

	/** The transform. */
	public AffineTransform transform = new AffineTransform();

	/** Whether or not we should adapt the view by transforming it. */
	private boolean adapt;

	/** Whether the transform needs to be reformed. */
	private boolean transformNeedsReform = true;

	/** THe table... bleh. */
	private JTable table;
	
	
	/**Factor to scale everyone by, when we don't use auto-scale*/
	private double scaleBy = 1;
}
