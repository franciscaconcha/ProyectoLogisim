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

package com.cburch.logisim.statediagram.externalDrawer.diagramSaver.xml;

import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.awt.Point;
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
import org.w3c.dom.NodeList;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject.StateTransition;
import com.cburch.logisim.statediagram.externalDrawer.diagramSaver.DiagramSaver;
//import automata.Automaton;
import com.cburch.logisim.statediagram.model.StateDiagram;
import com.cburch.logisim.statediagram.model.State;
import com.cburch.logisim.statediagram.model.Transition;
import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;
//import automata.fsa.FSATransition;
//import automata.fsa.FiniteStateAutomaton;

/**
 * Toma un Diagram y retorna un archivo XML
 * con sus estados y transiciones
 */

public class StateDiagramTransducer implements DiagramSaver{
	
	private String filename;
	public void SDtoXML(Diagram sd, String filename){
		this.filename = filename;
		SDdivider(sd);
	}
	
	/**
	 * Toma un elemento Diagram y lo divide
	 * en su lista de estados y transiciones
	 */
	public void SDdivider(Diagram SD){
		StateObject[] stateList = SD.getStates();
		TransitionObject[] transitionList = SD.getTransitions();
		
		XMLGenerator(stateList,transitionList);
	}
	
	/**
	 * Toma una lista de estados y transiciones
	 * y los va escribiendo en un XML
	 */
	public void XMLGenerator(StateObject[] states, 
			TransitionObject[] transitions){
		
		try{
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			// El elemento principal
			Element SDElement = doc.createElement("statediagram");
			doc.appendChild(SDElement);
			
			// agrego un nodo por cada estado
			for(int i = 0; i<states.length; i++) {
				StateObject curr=states[i];
				Element stateElement = doc.createElement("state");
				SDElement.appendChild(stateElement);
			
				// agrego como atributo el id del estado
				Attr attr = doc.createAttribute("id");
				attr.setValue(Integer.toString(curr.getID()));
				stateElement.setAttributeNode(attr);
				//nombre
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(curr.getName()));
				stateElement.appendChild(name);
				//coordenadas
				Element point = doc.createElement("point");
				name.appendChild(doc.createTextNode((curr.getPoint().toString())));
			}

			// agrego un nodo por cada transicion
			for(int i = 0; i<transitions.length; i++) {
				TransitionObject curr=transitions[i];
				Element transElement = doc.createElement("transition");
				SDElement.appendChild(transElement);
			
				// agrego como atributo un id, que corresponde al i actual
				Attr attr = doc.createAttribute("id");
				attr.setValue(Integer.toString(i));
				transElement.setAttributeNode(attr);
				// agrego origin, destiny, input, y output como elementos
				int originname=curr.getFromState().getID();
				int destinyname=curr.getToState().getID();
				String Tinput=((StateTransition) curr).getInput();
				String Toutput=((StateTransition) curr).getOutput();
				
				Element origin = doc.createElement("origin");
				origin.appendChild(doc.createTextNode(Integer.toString(originname)));
				transElement.appendChild(origin);
				
				Element destiny = doc.createElement("destiny");
				destiny.appendChild(doc.createTextNode(Integer.toString(destinyname)));
				transElement.appendChild(destiny);
				
				Element input = doc.createElement("input");
				input.appendChild(doc.createTextNode(Tinput));
				transElement.appendChild(input);
				
				Element output = doc.createElement("output");
				output.appendChild(doc.createTextNode(Toutput));
				transElement.appendChild(output);
			}
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));
	 
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

	@Override
	public void saveDiagram(Diagram diagram, String filename) {
		
		try {
			SDtoXML(diagram, filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (AbsentStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	
	@Override
	public Diagram getSavedDiagram(String filename) {
		
		Diagram diag = new Diagram();
		
		try {	 
			File XmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList sList = doc.getElementsByTagName("state"); //state list
			NodeList tList = doc.getElementsByTagName("transition"); //transition list
			
			//StateObject[] states;
			//TransitionObject[] transitions;
			
			//agrego los estados
			for (int temp = 0; temp < sList.getLength(); temp++) {
				 Node nNode = sList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					int stateId = Integer.parseInt(eElement.getAttribute("id"));
					String statePointstring = eElement.getElementsByTagName("point").item(0).getTextContent();
					String[] parts = statePointstring.split(",");
					String part1 = parts[0].split("(")[1];
					String part2 = parts[1].split(")")[0];
					Point statePoint = new Point(Integer.parseInt(part1),Integer.parseInt(part2));
					
					StateObject newstate = new StateObject(stateId,statePoint,diag);
					
					diag.addState(newstate);		 
				}
			}
			
			//agrego las transiciones
			for(int temp = 0; temp < sList.getLength(); temp++) {
				 Node nNode = tList.item(temp);
				 
				 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String stateFrom = eElement.getElementsByTagName("origin").item(0).getTextContent();
					String stateTo = eElement.getElementsByTagName("destiny").item(0).getTextContent();
					
					StateObject origin = diag.getStateWithID(Integer.parseInt(stateFrom));
					StateObject destiny = diag.getStateWithID(Integer.parseInt(stateTo));
					
					TransitionObject newtransition = new TransitionObject(origin,destiny);
					
					diag.addTransition(newtransition);					
				 }
			}
			
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		return diag;
	}
	
}
