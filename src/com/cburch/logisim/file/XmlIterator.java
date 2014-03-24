/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.Main source code and at www.cburch.com/logisim/. */

package com.cburch.logisim.file;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlIterator<E extends Node> implements Iterable<E>, Iterator<E>, Cloneable {
	public static XmlIterator<Node> forChildren(Element node) {
		return new XmlIterator<Node>(node.getChildNodes());
	}
	
	public static Iterable<Element> forChildElements(Element node) {
		NodeList nodes = node.getChildNodes();
		ArrayList<Element> ret = new ArrayList<Element>();
		for (int i = 0, n = nodes.getLength(); i < n; i++) {
			Node sub = nodes.item(i);
			if (sub.getNodeType() == Node.ELEMENT_NODE) {
				ret.add((Element) sub);
			}
		}
		return ret;
	}

	public static Iterable<Element> forChildElements(Element node, String tagName) {
		NodeList nodes = node.getChildNodes();
		ArrayList<Element> ret = new ArrayList<Element>();
		for (int i = 0, n = nodes.getLength(); i < n; i++) {
			Node sub = nodes.item(i);
			if (sub.getNodeType() == Node.ELEMENT_NODE) {
				Element elt = (Element) sub;
				if (elt.getTagName().equals(tagName)) ret.add(elt);
			}
		}
		return ret;
	}
	
	public static Iterable<Element> forDescendantElements(Element node, String tagName) {
		return new XmlIterator<Element>(node.getElementsByTagName(tagName));
	}
	
	private NodeList list;
	private int index;
	
	public XmlIterator(NodeList nodes) {
		list = nodes;
		index = 0;
	}
	
	@Override
	public XmlIterator<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			XmlIterator<E> ret = (XmlIterator<E>) super.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			return this;
		}
	}

	public Iterator<E> iterator() {
		XmlIterator<E> ret = this.clone();
		ret.index = 0;
		return ret;
	}

	public boolean hasNext() {
		return list != null && index < list.getLength();
	}

	public E next() {
		Node ret = list.item(index);
		if (ret == null) {
			throw new NoSuchElementException();
		} else {
			index++;
			@SuppressWarnings("unchecked")
			E ret2 = (E) ret;
			return ret2;
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("XmlChildIterator.remove");
	}
}
