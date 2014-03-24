/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.draw.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cburch.draw.canvas.Selection;
import com.cburch.draw.canvas.SelectionEvent;
import com.cburch.draw.canvas.SelectionListener;
import com.cburch.draw.model.CanvasObject;
import com.cburch.logisim.data.AbstractAttributeSet;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeEvent;
import com.cburch.logisim.data.AttributeListener;
import com.cburch.logisim.data.AttributeSet;

public class SelectionAttributes extends AbstractAttributeSet {
	private class Listener implements SelectionListener, AttributeListener {
		//
		// SelectionListener
		//
		public void selectionChanged(SelectionEvent ex) {
			Map<AttributeSet, CanvasObject> oldSel = selected;
			Map<AttributeSet, CanvasObject> newSel = new HashMap<AttributeSet, CanvasObject>();
			for (CanvasObject o : selection.getSelected()) {
				newSel.put(o.getAttributeSet(), o);
			}
			selected = newSel;
			boolean change = false;
			for (AttributeSet attrs : oldSel.keySet()) {
				if (!newSel.containsKey(attrs)) {
					change = true;
					attrs.removeAttributeListener(this);
				}
			}
			for (AttributeSet attrs : newSel.keySet()) {
				if (!oldSel.containsKey(attrs)) {
					change = true;
					attrs.addAttributeListener(this);
				}
			}
			if (change) {
				computeAttributeList(newSel.keySet());
				fireAttributeListChanged();
			}
		}
		
		private void computeAttributeList(Set<AttributeSet> attrsSet) {
			Set<Attribute<?>> attrSet = new LinkedHashSet<Attribute<?>>();
			Iterator<AttributeSet> sit = attrsSet.iterator();
			if (sit.hasNext()) {
				AttributeSet first = sit.next();
				attrSet.addAll(first.getAttributes());
				while (sit.hasNext()) {
					AttributeSet next = sit.next();
					for (Iterator<Attribute<?>> ait = attrSet.iterator(); ait.hasNext(); ) {
						Attribute<?> attr = ait.next();
						if (!next.containsAttribute(attr)) {
							ait.remove();
						}
					}
				}
			}

			Attribute<?>[] attrs = new Attribute[attrSet.size()];
			Object[] values = new Object[attrs.length];
			int i = 0;
			for (Attribute<?> attr : attrSet) {
				attrs[i] = attr;
				values[i] = getSelectionValue(attr, attrsSet);
				i++;
			}
			SelectionAttributes.this.selAttrs = attrs;
			SelectionAttributes.this.selValues = values;
			SelectionAttributes.this.attrsView
				= Collections.unmodifiableList(Arrays.asList(attrs));
			fireAttributeListChanged();
		}

		//
		// AttributeSet listener
		//
		public void attributeListChanged(AttributeEvent e) {
			// show selection attributes
			computeAttributeList(selected.keySet());
		}

		public void attributeValueChanged(AttributeEvent e) {
			if (selected.containsKey(e.getSource())) {
				@SuppressWarnings("unchecked")
				Attribute<Object> attr = (Attribute<Object>) e.getAttribute();
				Attribute<?>[] attrs = SelectionAttributes.this.selAttrs;
				Object[] values = SelectionAttributes.this.selValues;
				for (int i = 0; i < attrs.length; i++) {
					if (attrs[i] == attr) {
						values[i] = getSelectionValue(attr, selected.keySet());
					}
				}
			}
		}
	}
	
	private Selection selection;
	private Listener listener;
	private Map<AttributeSet, CanvasObject> selected;
	private Attribute<?>[] selAttrs;
	private Object[] selValues;
	private List<Attribute<?>> attrsView;
	
	public SelectionAttributes(Selection selection) {
		this.selection = selection;
		this.listener = new Listener();
		this.selected = Collections.emptyMap();
		this.selAttrs = new Attribute<?>[0];
		this.selValues = new Object[0];
		this.attrsView = Collections.unmodifiableList(Arrays.asList(selAttrs));
		
		selection.addSelectionListener(listener);
		listener.selectionChanged(null);
	}
	
	public Iterable<Map.Entry<AttributeSet, CanvasObject>> entries() {
		Set<Map.Entry<AttributeSet, CanvasObject>> raw = selected.entrySet();
		ArrayList<Map.Entry<AttributeSet, CanvasObject>> ret;
		ret = new ArrayList<Map.Entry<AttributeSet, CanvasObject>>(raw);
		return ret;
	}

	//
	// AbstractAttributeSet methods
	//
	@Override
	protected void copyInto(AbstractAttributeSet dest) {
		listener = new Listener();
		selection.addSelectionListener(listener);
	}

	@Override
	public List<Attribute<?>> getAttributes() {
		return attrsView;
	}

	@Override
	public <V> V getValue(Attribute<V> attr) {
		Attribute<?>[] attrs = this.selAttrs;
		Object[] values = this.selValues;
		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i] == attr) {
				@SuppressWarnings("unchecked")
				V ret = (V) values[i];
				return ret;
			}
		}
		return null;
	}

	@Override
	public <V> void setValue(Attribute<V> attr, V value) {
		Attribute<?>[] attrs = this.selAttrs;
		Object[] values = this.selValues;
		for (int i = 0; i < attrs.length; i++) {
			if (attrs[i] == attr) {
				boolean same = value == null ? values[i] == null : value.equals(values[i]);
				if (!same) {
					values[i] = value;
					for (AttributeSet objAttrs : selected.keySet()) {
						objAttrs.setValue(attr, value);
					}
				}
				break;
			}
		}
	}
	
	private static Object getSelectionValue(Attribute<?> attr,
			Set<AttributeSet> sel) {
		Object ret = null;
		for (AttributeSet attrs : sel) {
			if (attrs.containsAttribute(attr)) {
				Object val = attrs.getValue(attr);
				if (ret == null) {
					ret = val;
				} else if (val != null && val.equals(ret)) {
					; // keep on, making sure everything else matches
				} else {
					return null;
				}
			}
		}
		return ret;
	}
}
