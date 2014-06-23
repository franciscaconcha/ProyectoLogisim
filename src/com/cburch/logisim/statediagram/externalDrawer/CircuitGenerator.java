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
import com.cburch.logisim.statediagram.model.DiagramTable;
import com.cburch.logisim.statediagram.model.DiagramTable2;
import com.cburch.logisim.statediagram.model.StateDiagram;
import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.MissingTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.NoStatesException;
import com.cburch.logisim.statediagram.model.exceptions.NoTransitionsException;
import com.cburch.logisim.statediagram.model.exceptions.NotStronglyConnectedDiagram;
import com.cburch.logisim.statediagram.model.exceptions.RepeatedTransitionException;
import com.cburch.logisim.statediagram.view.MainSubcircuit;
import com.cburch.logisim.statediagram.view.RegisterSubcircuit;
import com.cburch.logisim.std.gates.CircuitBuilder;
import com.cburch.logisim.analyze.gui.Strings;

public class CircuitGenerator {

	private ExternalDiagramDrawer stateDiagramDrawer;
	private Project logisimProject;

	public CircuitGenerator(Project logisimProject) {

		this.logisimProject = logisimProject;
		stateDiagramDrawer = new ExternalDiagramDrawer(this);

	}

	public void showExternalDrawer() {

		stateDiagramDrawer.show();

	}

	public void generate() {

		StateDiagram finalModel = null;

		try {

			finalModel = stateDiagramDrawer.getDiagram().getAlternativeModel();

		} catch (InvalidTransitionException e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"Transition can only contain '*', '1' and '0'", "Error",
					JOptionPane.PLAIN_MESSAGE);

			return;

		} catch (AbsentStateException e) {

			e.printStackTrace();
			return;

		} catch (Exception e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"Unknown error: The circuit can't be generated.", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		}

		try {
			finalModel.isCorrect();

		} catch (NoStatesException e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"You didn't draw any states...", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (NoTransitionsException e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"You didn't draw any transitions...", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (InconsistentInputLengthException e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"Input transitions must have the same length.", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (InconsistentOutputLengthException e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"Output transitions must have the same length.", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (RepeatedTransitionException e) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Two transitions with the same origin state cannot have the same input.",
							"Error", JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (MissingTransitionException e) {
			JOptionPane
					.showMessageDialog(
							new JFrame(),
							"Transitions coming from the same state must cover all the possible inputs.",
							"Error", JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (NotStronglyConnectedDiagram e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"There must be a path between every pair of states.",
					"Error", JOptionPane.PLAIN_MESSAGE);
			return;

		} catch (Exception e) {

			JOptionPane.showMessageDialog(new JFrame(),
					"Unknown error: The circuit can't be generated.", "Error",
					JOptionPane.PLAIN_MESSAGE);
			return;

		}

		try { // TODO Cambiar mensaje de error
			DiagramTable2 table = new DiagramTable2(
					finalModel.getRepresentationMatrix()); // Segun RepMatrix
			AnalyzerModel analyzerModel = AnalyzerManager.getAnalyzer()
					.getModel(); // Obtiene modelo actual
			table.loadIntoModel(analyzerModel); // Actualiza el modelo

			Circuit combinatorial = new Circuit("Combinatorial");
			logisimProject.doAction(LogisimFileActions
					.addCircuit(combinatorial));

			CircuitMutation xn = CircuitBuilder.build(combinatorial,
					analyzerModel, false, true);

			logisimProject.doAction(xn.toAction(Strings
					.getter("replaceCircuitAction")));
			// Juanjo: aquí se debe tomar el circuito y modificarlo, ya que recién después de llamar a
			// doAction el proyecto cambia.
			buildElements(combinatorial, table.getMemoryBitNumber());
			stateDiagramDrawer.getFrame().dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), "Error Cami xd.",
					"Error", JOptionPane.PLAIN_MESSAGE);
			return;
		}
	}

	private void buildElements(Circuit combinatorial, int bitWidth) {
		if (bitWidth != 0) { //verificamos que se hayan codificado estados
			RegisterSubcircuit rs = new RegisterSubcircuit(logisimProject,
					bitWidth);
			Circuit register = rs.create(logisimProject, bitWidth);
			MainSubcircuit ms = new MainSubcircuit(logisimProject,
					combinatorial, register, bitWidth);
			ms.create();
		}
	}
}