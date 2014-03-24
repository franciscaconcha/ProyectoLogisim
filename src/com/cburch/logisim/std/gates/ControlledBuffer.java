/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.std.gates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

import com.cburch.logisim.comp.ComponentFactory;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeOption;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Location;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.file.Options;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.tools.WireRepair;
import com.cburch.logisim.tools.WireRepairData;
import com.cburch.logisim.tools.key.BitWidthConfigurator;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.util.Icons;

class ControlledBuffer extends InstanceFactory {
	private static final AttributeOption RIGHT_HANDED
		= new AttributeOption("right", Strings.getter("controlledRightHanded"));
	private static final AttributeOption LEFT_HANDED
		= new AttributeOption("left", Strings.getter("controlledLeftHanded"));
	private static final Attribute<AttributeOption> ATTR_CONTROL
		= Attributes.forOption("control", Strings.getter("controlledControlOption"),
				new AttributeOption[] { RIGHT_HANDED, LEFT_HANDED });
			
	public static ComponentFactory FACTORY_BUFFER = new ControlledBuffer(false);
	public static ComponentFactory FACTORY_INVERTER = new ControlledBuffer(true);
	
	private static final Icon ICON_BUFFER = Icons.getIcon("controlledBuffer.gif");
	private static final Icon ICON_INVERTER = Icons.getIcon("controlledInverter.gif");

	private boolean isInverter;

	private ControlledBuffer(boolean isInverter) {
		super(isInverter ? "Controlled Inverter" : "Controlled Buffer",
			isInverter ? Strings.getter("controlledInverterComponent")
					: Strings.getter("controlledBufferComponent"));
		this.isInverter = isInverter;
		if (isInverter) {
			setAttributes(new Attribute[] { StdAttr.FACING, StdAttr.WIDTH,
					NotGate.ATTR_SIZE, ATTR_CONTROL,
					StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { Direction.EAST, BitWidth.ONE,
					NotGate.SIZE_WIDE, RIGHT_HANDED,
					"", StdAttr.DEFAULT_LABEL_FONT });
		} else {
			setAttributes(new Attribute[] {
					StdAttr.FACING, StdAttr.WIDTH, ATTR_CONTROL,
					StdAttr.LABEL, StdAttr.LABEL_FONT },
				new Object[] { Direction.EAST, BitWidth.ONE, RIGHT_HANDED,
					"", StdAttr.DEFAULT_LABEL_FONT });
		}
		setFacingAttribute(StdAttr.FACING);
		setKeyConfigurator(new BitWidthConfigurator(StdAttr.WIDTH));
	}

	@Override
	public Bounds getOffsetBounds(AttributeSet attrs) {
		int w = 20;
		if (isInverter &&
				!NotGate.SIZE_NARROW.equals(attrs.getValue(NotGate.ATTR_SIZE))) {
			w = 30;
		}
		Direction facing = attrs.getValue(StdAttr.FACING);
		if (facing == Direction.NORTH) return Bounds.create(-10,  0, 20, w);
		if (facing == Direction.SOUTH) return Bounds.create(-10, -w, 20, w);
		if (facing == Direction.WEST) return Bounds.create(0, -10, w, 20);
		return Bounds.create(-w, -10, w, 20);
	}
	
	//
	// graphics methods
	//
	@Override
	public void paintGhost(InstancePainter painter) {
		paintShape(painter);
	}

	@Override
	public void paintIcon(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		Icon icon = isInverter ? ICON_INVERTER : ICON_BUFFER;
		if (icon != null) {
			icon.paintIcon(painter.getDestination(), g, 2, 2);
		} else {
			int x = isInverter ? 0 : 2;
			g.setColor(Color.BLACK);
			int[] xp = new int[] { x + 15, x + 1, x + 1, x + 15 };
			int[] yp = new int[] { 10,     3,     17,    10 };
			g.drawPolyline(xp, yp, 4);
			if (isInverter) g.drawOval(x + 13, 8, 4, 4);
			g.setColor(Value.FALSE_COLOR);
			g.drawLine(x + 8, 14, x + 8, 18);
		}
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Direction face = painter.getAttributeValue(StdAttr.FACING);

		Graphics g = painter.getGraphics();

		// draw control wire
		GraphicsUtil.switchToWidth(g, 3);
		Location pt0 = painter.getInstance().getPortLocation(2);
		Location pt1;
		if (painter.getAttributeValue(ATTR_CONTROL) == LEFT_HANDED) {
			pt1 = pt0.translate(face, 0, 6);
		} else {
			pt1 = pt0.translate(face, 0, -6);
		}
		if (painter.getShowState()) {
			g.setColor(painter.getPort(2).getColor());
		}
		g.drawLine(pt0.getX(), pt0.getY(), pt1.getX(), pt1.getY());

		// draw triangle
		g.setColor(Color.BLACK);
		paintShape(painter);

		// draw input and output pins
		if (!painter.isPrintView()) {
			painter.drawPort(0);
			painter.drawPort(1);
		}
		painter.drawLabel();
	}
	
	private void paintShape(InstancePainter painter) {
		Direction facing = painter.getAttributeValue(StdAttr.FACING);
		Location loc = painter.getLocation();
		int x = loc.getX();
		int y = loc.getY();
		double rotate = 0.0;
		Graphics g = painter.getGraphics();
		g.translate(x, y);
		if (facing != Direction.EAST && g instanceof Graphics2D) {
			rotate = -facing.toRadians();
			((Graphics2D) g).rotate(rotate);
		}

		if (isInverter) {
			PainterShaped.paintNot(painter);
		} else {
			GraphicsUtil.switchToWidth(g, 2);
			int d = isInverter ? 10 : 0;
			int[] xp = new int[] { -d, -19 - d, -19 - d, -d };
			int[] yp = new int[] {  0,  -7,       7,      0 };
			g.drawPolyline(xp, yp, 4);
			// if (isInverter) g.drawOval(-9, -4, 9, 9);
		}
		
		if (rotate != 0.0) {
			((Graphics2D) g).rotate(-rotate);
		}
		g.translate(-x, -y);
	}

	//
	// methods for instances
	//
	@Override
	protected void configureNewInstance(Instance instance) {
		instance.addAttributeListener();
		configurePorts(instance);
		NotGate.configureLabel(instance, false, instance.getPortLocation(2));
	}
	
	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		if (attr == StdAttr.FACING || attr == NotGate.ATTR_SIZE) {
			instance.recomputeBounds();
			configurePorts(instance);
			NotGate.configureLabel(instance, false, instance.getPortLocation(2));
		} else if (attr == ATTR_CONTROL) {
			configurePorts(instance);
			NotGate.configureLabel(instance, false, instance.getPortLocation(2));
		}
	}

	private void configurePorts(Instance instance) {
		Direction facing = instance.getAttributeValue(StdAttr.FACING);
		Bounds bds = getOffsetBounds(instance.getAttributeSet());
		int d = Math.max(bds.getWidth(), bds.getHeight()) - 20;
		Location loc0 = Location.create(0, 0);
		Location loc1 = loc0.translate(facing.reverse(), 20 + d);
		Location loc2;
		if (instance.getAttributeValue(ATTR_CONTROL) == LEFT_HANDED) {
			loc2 = loc0.translate(facing.reverse(), 10 + d, 10);
		} else {
			loc2 = loc0.translate(facing.reverse(), 10 + d, -10);
		}

		Port[] ports = new Port[3];
		ports[0] = new Port(0, 0, Port.OUTPUT, StdAttr.WIDTH);
		ports[1] = new Port(loc1.getX(), loc1.getY(), Port.INPUT, StdAttr.WIDTH);
		ports[2] = new Port(loc2.getX(), loc2.getY(), Port.INPUT, 1);
		instance.setPorts(ports);
	}

	@Override
	public void propagate(InstanceState state) {
		Value control = state.getPort(2);
		BitWidth width = state.getAttributeValue(StdAttr.WIDTH);
		if (control == Value.TRUE) {
			Value in = state.getPort(1);
			state.setPort(0, isInverter ? in.not() : in, GateAttributes.DELAY);
		} else if (control == Value.ERROR || control == Value.UNKNOWN) {
			state.setPort(0, Value.createError(width), GateAttributes.DELAY);
		} else {
			Value out;
			if (control == Value.UNKNOWN || control == Value.NIL) {
				AttributeSet opts = state.getProject().getOptions().getAttributeSet();
				if (opts.getValue(Options.ATTR_GATE_UNDEFINED)
						.equals(Options.GATE_UNDEFINED_ERROR)) {
					out = Value.createError(width);
				} else {
					out = Value.createUnknown(width);
				}
			} else {
				out = Value.createUnknown(width);
			}
			state.setPort(0, out, GateAttributes.DELAY);
		}
	}
	
	@Override
	public Object getInstanceFeature(final Instance instance, Object key) {
		if (key == WireRepair.class) {
			return new WireRepair() {
				public boolean shouldRepairWire(WireRepairData data) {
					Location port2 = instance.getPortLocation(2);
					return data.getPoint().equals(port2);
				}
			};
		}
		return super.getInstanceFeature(instance, key);
	}
}
