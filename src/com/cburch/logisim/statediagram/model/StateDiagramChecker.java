package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;

import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.MissingTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.NoStatesException;
import com.cburch.logisim.statediagram.model.exceptions.NoTransitionsException;
import com.cburch.logisim.statediagram.model.exceptions.RepeatedTransitionException;

/**
 * Clase de Verificador de Diagrama de Estados, el objeto asociado a un Diagrama
 * de Estados que se encarga de determinar si el diagrama esta correcto o no.
 * 
 * @author raticate
 * 
 */

public class StateDiagramChecker {
	public StateDiagramChecker() {
	}

/**
 * Realiza todos los chequeos implementados
 * 
 * @param sd
 * @throws NoStatesException
 * @throws NoTransitionsException
 * @throws RepeatedTransitionException
 * @throws MissingTransitionException
 * @throws InconsistentInputLengthException
 * @throws InconsistentOutputLengthException
 * @throws InvalidTransitionException
 */
	public void checkAll(StateDiagram sd)
			throws InconsistentInputLengthException,
			InconsistentOutputLengthException,
			MissingTransitionException, RepeatedTransitionException,
			NoStatesException, NoTransitionsException,
			InvalidTransitionException {
		this.checkComponentEmpyness(sd);
		this.checkTransitionsLength(sd);
		this.checkNoRepeatedTransitions(sd);
		this.checkTransitionCompleteness(sd);

	}

	/**
	 * Verifica que ni el conjunto de estados ni de transiciones del diagrama
	 * sean vacios.
	 * 
	 * @param sd
	 * @throws NoStatesException
	 * @throws NoTransitionsException
	 */
	private void checkComponentEmpyness(StateDiagram sd)
			throws NoStatesException, NoTransitionsException {
		if (sd.getStates().isEmpty())
			throw new NoStatesException();
		if (sd.getTransitions().isEmpty())
			throw new NoTransitionsException();

	}

	/**
	 * Verifica que no hayan transiciones con el mismo input saliendo de un
	 * mismo estado, realiza esa verificacion para todos los estados del
	 * diagrama
	 * 
	 * @param sd
	 * @throws RepeatedTransitionException
	 */
	public void checkNoRepeatedTransitions(StateDiagram sd)
			throws RepeatedTransitionException {
		for (State s : sd.getStates())
			this.checkNoRepeatedTransitionsFromState(s, sd);

	}

	/**
	 * Verifica que no hayan transiciones con el mismo input saliendo de un
	 * estado especifico
	 * 
	 * @param s , sd
	 * @throws RepeatedTransitionException
	 */
	private void checkNoRepeatedTransitionsFromState(State s, StateDiagram sd)
			throws RepeatedTransitionException {
		ArrayList<String> thisStateOutTransitionInputs = new ArrayList<String>();
		for (Transition t : sd.getTransitions()) {
			if (t.getOrigin().equals(s))
				if (thisStateOutTransitionInputs.contains(t.getOrigin()))
					throw new RepeatedTransitionException();
			thisStateOutTransitionInputs.add(t.getInput());
		}
	}
	/**
	 * Verifica que las transiciones salientes de un estado cubran todos los inputs posibles
	 * para el largo de transicion dado.
	 * 
	 * @param s , sd
	 * @throws MissingTransitionException
	 * @throws InvalidTransitionException
	 */
	public void checkTransitionCompleteness(StateDiagram sd)
			throws MissingTransitionException, InvalidTransitionException {
		ArrayList<State> states = sd.getStates();
		for (State s : states) {
			this.checkOutTransitionsFromState(s, sd);
		}
	}

	public void checkOutTransitionsFromState(State s, StateDiagram sd)
			throws MissingTransitionException, InvalidTransitionException {
		int l = sd.getRepresentationMatrix().getInputLength();
		int possibleTransitions = (int) Math.pow(2, l);
		int[] checkList = new int[possibleTransitions];
		ArrayList<Transition> trans = sd.getTransitions();
		String in;
		int n;
		for (Transition t : trans) {
			if (t.getOrigin().equals(s)) {
				in = t.getInput();
				if (in.contains("*")) {
					this.getNumberFromTransitionWithWildcard(t, checkList);
				} else {
					this.getNumberFromTransition(t.getInput(), checkList);
				}
			}
		}
		int sum = 0;
		for (int i = 0; i < possibleTransitions; i++)
			sum += checkList[i];
		if (sum < possibleTransitions)
			throw new MissingTransitionException();
	}

	public void getNumberFromTransitionWithWildcard(Transition t,
			int[] checkList) {
		ArrayList<String> equivalentTrans = new ArrayList<String>();
		String originalInput = t.getInput();
		equivalentTrans.add(originalInput);
		this.equivalentTransitionsGetter(equivalentTrans);
		for (String s : equivalentTrans)
			this.getNumberFromTransition(s, checkList);

	}

	private void equivalentTransitionsGetter(ArrayList<String> equivalentTrans) {

		ArrayList<String> extensiveInputs = getExtensiveInputs(equivalentTrans);

		for (String toExtend : extensiveInputs) {

			String add1 = toExtend.replaceFirst("\\*", "0");
			String add2 = toExtend.replaceFirst("\\*", "1");
			ArrayList<String> aux = new ArrayList<String>();
			equivalentTrans.remove(toExtend);
			aux.add(add1);
			aux.add(add2);
			this.equivalentTransitionsGetter(aux);
			equivalentTrans.addAll(aux);
		}

	}

	private ArrayList<String> getExtensiveInputs(ArrayList<String> inputs) {

		ArrayList<String> extensiveInput = new ArrayList<String>();
		for (String s : inputs) {
			if (s.contains("*")) {
				extensiveInput.add(s);
			}
		}
		return extensiveInput;

	}

	public void getNumberFromTransition(String s, int[] checkList) {
		int l = s.length();
		int number = 0;
		int i;
		char c;
		for (i = 0; i < l; i++) {
			c = s.charAt(i);
			number = 2 * number + Character.getNumericValue(c);
		}
		checkList[number] = 1;

	}
	/**
	 * Verifica que todas las transiciones tengan strings de input con el mismo largo y de 
	 * output con el mismo largo
	 * 
	 * @param sd
	 * @throws InconsistentInputLengthException
	 * @throws InconsistentOutputLengthException
	 */
	public void checkTransitionsLength(StateDiagram sd)
			throws InconsistentInputLengthException,
			InconsistentOutputLengthException {
		checkInputsLengths(sd);
		checkOuputLengths(sd);
	}

	public void checkInputsLengths(StateDiagram sd)
			throws InconsistentInputLengthException {
		ArrayList<Transition> trans = sd.getTransitions();
		Transition test = trans.get(0);

		for (Transition transition : trans) {
			if (transition.getInput().length() != test.getInput().length())
				throw new InconsistentInputLengthException();

		}

	}

	public void checkOuputLengths(StateDiagram sd)
			throws InconsistentOutputLengthException {
		ArrayList<Transition> t = sd.getTransitions();
		Transition test = t.get(0);

		for (Transition transition : t) {
			if (transition.getOutput().length() != test.getOutput().length())
				throw new InconsistentOutputLengthException();
		}

	}

}
