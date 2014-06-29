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

package com.cburch.logisim.statediagram.xml;

import java.util.Map;
import java.util.ArrayList;
import java.io.File;

import javax.swing.text.html.HTMLDocument.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import automata.Automaton;
import com.cburch.logisim.statediagram.model.StateDiagram;
import com.cburch.logisim.statediagram.model.State;
import com.cburch.logisim.statediagram.model.Transition;
//import automata.fsa.FSATransition;
//import automata.fsa.FiniteStateAutomaton;

/**
 * Toma un StateDiagram y retorna un archivo XML
 * con sus estados y transiciones
 */

public class StateDiagramTransducer{
	
	public void SDtoXML(StateDiagram sd){
		SDdivider(sd);
	}
	
	/**
	 * Toma un elemento StateDiagram y lo divide
	 * en su lista de estados y transiciones
	 */
	public void SDdivider(StateDiagram SD){
		ArrayList<State> stateList = SD.getStates();
		ArrayList<Transition> transitionList = SD.getTransitions();
		
		XMLGenerator(stateList,transitionList);
	}
	
	/**
	 * Toma una lista de estados y transiciones
	 * y los va escribiendo en un XML
	 */
	public void XMLGenerator(ArrayList<State> states, 
			ArrayList<Transition> transitions){
		
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			// El elemento principal
			Element SDElement = doc.createElement("statediagram");
			doc.appendChild(SDElement);
			
			// agrego un nodo por cada estado
			for(int i = 0; i<states.size(); i++) {
				State curr=states.get(i);
				Element stateElement = doc.createElement("state");
				SDElement.appendChild(stateElement);
			
				// agrego como atributo el id del estado
				Attr attr = doc.createAttribute("id");
				attr.setValue(Integer.toString(curr.getId()));
				stateElement.setAttributeNode(attr);
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(curr.getName()));
				stateElement.appendChild(name);
			}

			// agrego un nodo por cada transicion
			for(int i = 0; i<transitions.size(); i++) {
				Transition curr=transitions.get(i);
				Element transElement = doc.createElement("transition");
				SDElement.appendChild(transElement);
			
				// agrego como atributo un id, que corresponde al i actual
				Attr attr = doc.createAttribute("id");
				attr.setValue(Integer.toString(i));
				transElement.setAttributeNode(attr);
				// agrego origin, destiny, input, y output como elementos
				Element origin = doc.createElement("origin");
				State thisorigin = curr.getOrigin();
				int originname = thisorigin.getId();
				origin.appendChild(doc.createTextNode(Integer.toString(originname)));
				transElement.appendChild(origin);
				
				Element destiny = doc.createElement("destiny");
				State thisdestiny = curr.getDestiny();
				int destinyname = thisdestiny.getId();
				destiny.appendChild(doc.createTextNode(Integer.toString(destinyname)));
				transElement.appendChild(destiny);
				
				Element input = doc.createElement("input");
				input.appendChild(doc.createTextNode(curr.getInput()));
				transElement.appendChild(input);
				
				Element output = doc.createElement("output");
				output.appendChild(doc.createTextNode(curr.getOutput()));
				transElement.appendChild(output);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\file.xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	 
			//System.out.println("File saved!");
			
			
		} 
		catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
		
	}
	
}
