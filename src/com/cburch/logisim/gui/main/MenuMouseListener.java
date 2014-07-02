package com.cburch.logisim.gui.main;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.Wire;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentDrawContext;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.SetAttributeAction;

public class MenuMouseListener implements MouseListener {

	private Circuit circuit;
	private ArrayList<Location> locations;
	private Canvas canvas;

	public MenuMouseListener( ArrayList<Location> locations, Circuit circuit, Canvas canvas ) {
		this.locations = locations;
		this.circuit = circuit;
		this.canvas = canvas;
	}

	@Override
	public void mouseClicked( MouseEvent arg0 ) {
		
	}

	/*
	Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException
	at com.cburch.logisim.circuit.Wire.draw(Wire.java:207)
	at com.cburch.logisim.gui.main.MenuMouseListener.mouseEntered(MenuMouseListener.java:40)
	at java.awt.AWTEventMulticaster.mouseEntered(AWTEventMulticaster.java:300)
	at java.awt.Component.processMouseEvent(Component.java:6514)
	at javax.swing.JComponent.processMouseEvent(JComponent.java:3311)
	at java.awt.Component.processEvent(Component.java:6270)
	at java.awt.Container.processEvent(Container.java:2229)
	at java.awt.Component.dispatchEventImpl(Component.java:4861)
	at java.awt.Container.dispatchEventImpl(Container.java:2287)
	at java.awt.Component.dispatchEvent(Component.java:4687)
	at java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4832)
	at java.awt.LightweightDispatcher.trackMouseEnterExit(Container.java:4620)
	at java.awt.LightweightDispatcher.processMouseEvent(Container.java:4474)
	at java.awt.LightweightDispatcher.dispatchEvent(Container.java:4422)
	at java.awt.Container.dispatchEventImpl(Container.java:2273)
	at java.awt.Window.dispatchEventImpl(Window.java:2719)
	at java.awt.Component.dispatchEvent(Component.java:4687)
	at java.awt.EventQueue.dispatchEventImpl(EventQueue.java:735)
	at java.awt.EventQueue.access$200(EventQueue.java:103)
	at java.awt.EventQueue$3.run(EventQueue.java:694)
	at java.awt.EventQueue$3.run(EventQueue.java:692)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$1.doIntersectionPrivilege(ProtectionDomain.java:76)
	at java.security.ProtectionDomain$1.doIntersectionPrivilege(ProtectionDomain.java:87)
	at java.awt.EventQueue$4.run(EventQueue.java:708)
	at java.awt.EventQueue$4.run(EventQueue.java:706)
	at java.security.AccessController.doPrivileged(Native Method)
	at java.security.ProtectionDomain$1.doIntersectionPrivilege(ProtectionDomain.java:76)
	at java.awt.EventQueue.dispatchEvent(EventQueue.java:705)
	at java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:242)
	at java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:161)
	at java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:150)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:146)
	at java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:138)
	at java.awt.EventDispatchThread.run(EventDispatchThread.java:91)
	 */
	
	
	@Override
	public void mouseEntered( MouseEvent arg0 ) {
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = Color.BLUE;
				//ComponentDrawContext context = ( (Wire) comp ).getContext();
				//( (Wire) comp ).draw( context, Color.MAGENTA );
			}
		}
		canvas.repaint(); //BORRAR PARA VER LOS SUBMENU
	}

	@Override
	public void mouseExited( MouseEvent arg0 ) {
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = new Color(255, 123, 0);
				//ComponentDrawContext context = ( (Wire) comp ).getContext();
				//( (Wire) comp ).draw( context, Color.MAGENTA );
			}
		}
		canvas.repaint(); //BORRAR PARA VER LOS SUBMENU
	}

	@Override
	public void mousePressed( MouseEvent arg0 ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased( MouseEvent arg0 ) {/*
		for( Location location : locations ) {
			for( Wire wire : circuit.getWires(location ) ) {
				wire.widthErrorColor = new Color(255, 123, 0);
				//ComponentDrawContext context = ( (Wire) comp ).getContext();
				//( (Wire) comp ).draw( context, Color.MAGENTA );
			}
		} */
	}

}
