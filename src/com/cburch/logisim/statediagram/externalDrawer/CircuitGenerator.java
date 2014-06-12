package com.cburch.logisim.statediagram.externalDrawer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.cburch.logisim.analyze.gui.Analyzer;
import com.cburch.logisim.analyze.gui.AnalyzerManager;
import com.cburch.logisim.analyze.model.AnalyzerModel;
import com.cburch.logisim.circuit.Analyze;
import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitMutation;
import com.cburch.logisim.file.LogisimFileActions;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.ExternalDiagramDrawer;
import com.cburch.logisim.statediagram.model.AbsentStateException;
import com.cburch.logisim.statediagram.model.DiagramTable;
import com.cburch.logisim.statediagram.model.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.InvalidTransitionException;
import com.cburch.logisim.statediagram.model.MissingTransitionException;
import com.cburch.logisim.statediagram.model.NotStronglyConnectedDiagram;
import com.cburch.logisim.statediagram.model.RepeatedTransitionException;
import com.cburch.logisim.statediagram.model.StateDiagram;
import com.cburch.logisim.statediagram.view.SequentialCircuit;
import com.cburch.logisim.std.gates.CircuitBuilder;
import com.cburch.logisim.analyze.gui.Strings;

public class CircuitGenerator {
	
	private ExternalDiagramDrawer stateDiagramDrawer;
	private Project logisimProject;
	
	public CircuitGenerator(Project logisimProject){
		
		this.logisimProject=logisimProject;		
		stateDiagramDrawer=new ExternalDiagramDrawer(this);
		
	}
	
	public void showExternalDrawer(){
		
		stateDiagramDrawer.show();
		
	}
	
	public void generate(){
		
		StateDiagram finalModel=null;
		
		try {
			
			finalModel = stateDiagramDrawer.getDiagram().getAlternativeModel();
			
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
			stateDiagramDrawer.getFrame().dispose();
			
		} catch (InconsistentInputLengthException e){
		
			JOptionPane.showMessageDialog(new JFrame(), "Input transitions must have the same length."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
			
		} catch (InconsistentOutputLengthException e){
			
			JOptionPane.showMessageDialog(new JFrame(), "Output transitions must have the same length."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
			
		} catch (RepeatedTransitionException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Two transitions with the same origin state cannot have the same input."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
		} catch (MissingTransitionException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Transitions coming from the same state must cover all the possible inputs."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
		} catch (NotStronglyConnectedDiagram e) {
			
			JOptionPane.showMessageDialog(new JFrame(), "There must be a path between every pair of states."
	                ,"Error", JOptionPane.PLAIN_MESSAGE);
		}
		
		
		DiagramTable table=new DiagramTable(finalModel.getRepresentationMatrix()); //Segun RepMatrix
		AnalyzerModel analyzerModel = AnalyzerManager.getAnalyzer().getModel(); //Obtiene modelo actual
		table.loadIntoModel(analyzerModel); //Actualiza el modelo
		
		Circuit combinatorial = new Circuit("Combinatorial");
		logisimProject.doAction(LogisimFileActions.addCircuit(combinatorial));
		
		
		CircuitMutation xn = CircuitBuilder.build(combinatorial, analyzerModel, false,
				true);

		logisimProject.doAction(xn.toAction(Strings.getter("replaceCircuitAction")));
		// TODO Juanjo: aquí se debe tomar el circuito y modificarlo, ya que recién después de llamar a 
		// doAction el proyecto cambia.
		SequentialCircuit sq = new SequentialCircuit(logisimProject, combinatorial);
		stateDiagramDrawer.getFrame().dispose();
	}

}
