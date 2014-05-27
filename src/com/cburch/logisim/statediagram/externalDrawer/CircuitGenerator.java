package com.cburch.logisim.statediagram.externalDrawer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.cburch.logisim.analyze.gui.Analyzer;
import com.cburch.logisim.analyze.gui.AnalyzerManager;
import com.cburch.logisim.circuit.Analyze;
import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.model.AbsentStateException;
import com.cburch.logisim.statediagram.model.DiagramTable;
import com.cburch.logisim.statediagram.model.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.InvalidTransitionException;
import com.cburch.logisim.statediagram.model.StateDiagram;
import com.cburch.logisim.statediagram.model.notStronglyConnectedDiagram;

public class CircuitGenerator {
	
	private Diagram diagram;
	private JFrame frame;
	
	public CircuitGenerator(){}
	
	public void generate(){
		
		StateDiagram finalModel=null;
		
		try {
			
			finalModel = diagram.getAlternativeModel();
			
		} catch (InvalidTransitionException e) {
			
			JOptionPane.showMessageDialog(new JFrame(), "Transition can only contain '*', '1' and '0'"
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
			
			return;
			
		}catch (AbsentStateException e){
			
			e.printStackTrace();
			return;
		}
		
		try {
			finalModel.isCorrect();
			
			System.out.println("Circuit :D :D :D");
			frame.dispose();
			
		} catch (InconsistentInputLengthException e){
		
			JOptionPane.showMessageDialog(new JFrame(), "Input transitions must have the same length."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
			
		}catch (InconsistentOutputLengthException e){
			
			JOptionPane.showMessageDialog(new JFrame(), "Output transitions must have the same length."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
			
		}catch (notStronglyConnectedDiagram e) {
			
			JOptionPane.showMessageDialog(new JFrame(), "There must be a path between every pair of states."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
		}
		DiagramTable table=new DiagramTable(finalModel.getRepresentationMatrix());
		Analyzer a=AnalyzerManager.getAnalyzer();
		Analyze.loadDiagramTable(a.getModel());
		
	}
	
	public void setDiagram(Diagram diagram){
		this.diagram=diagram;
	}
	
	public void setFrame(JFrame frame){
		this.frame=frame;
	}

}
