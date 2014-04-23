package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;



public class StateDiagramChecker {
	static class InconsistentInputLengthException extends Exception {	}
	static class InconsistentOutputLengthException extends Exception {	}
	public StateDiagramChecker(){}
	public void checkAll(StateDiagram sd){
		this.checkTransitionsLength(sd);
		this.checkConexity(sd);
		this.checkTransitionConsistency(sd);
	}
	public void checkConexity(StateDiagram sd){
		//TO-DO
	}
	public void checkTransitionConsistency(StateDiagram sd){
		//TO-DO
	}
	public void checkTransitionsLength(StateDiagram sd){
		try {
			checkInputsLengths(sd);
		} catch (InconsistentInputLengthException e) {
			System.out.println("Inputs de distinto largo");
			e.printStackTrace();
		}
		try {
			checkOuputLengths(sd);
		} catch (InconsistentOutputLengthException e) {
			System.out.println("Outputs de distinto largo");
			e.printStackTrace();
		}
	}
	public void checkInputsLengths(StateDiagram sd) throws InconsistentInputLengthException {
		ArrayList<Transition> t=sd.getTransitions();
		Transition test=t.get(0);
		int testLength=test.getInput().length();
		for (Transition trans: t)
			if (trans.getInput().length()!=testLength)
				throw new InconsistentInputLengthException();
	}
	public void checkOuputLengths(StateDiagram sd) throws InconsistentOutputLengthException{
		ArrayList<Transition> t=sd.getTransitions();
		Transition test=t.get(0);
		int testLength=test.getInput().length();
		for (Transition trans: t)
			if (trans.getOutput().length()!=testLength)
				throw new InconsistentOutputLengthException();
	}

}
