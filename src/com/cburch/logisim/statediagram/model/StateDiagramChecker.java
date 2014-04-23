package com.cburch.logisim.statediagram.model;




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
		Transition[] t=(Transition [])sd.getTransitions().toArray();
		Transition test=t[0];
		for (int i=1; i<t.length; i++)
			if (t[i].getInput().length()!=test.getInput().length())
				throw new InconsistentInputLengthException();
	}
	public void checkOuputLengths(StateDiagram sd) throws InconsistentOutputLengthException{
		Transition[] t=(Transition [])sd.getTransitions().toArray();
		Transition test=t[0];
		for (int i=1; i<t.length; i++)
			if (t[i].getOutput().length()!=test.getInput().length())
				throw new InconsistentOutputLengthException();
	}

}
