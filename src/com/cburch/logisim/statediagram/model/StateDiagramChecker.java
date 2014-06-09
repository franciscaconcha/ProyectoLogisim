package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;




public class StateDiagramChecker {
	public StateDiagramChecker(){}
	
	public void checkAll(StateDiagram sd) throws InconsistentInputLengthException, InconsistentOutputLengthException, notStronglyConnectedDiagram{
		this.checkTransitionsLength(sd);
		//this.checkStrongConnectivity(sd);
		//this.checkTransitionCompleteness(sd);
	}
	public void checkTransitionCompleteness(StateDiagram sd){
		ArrayList<State> states=sd.getStates();
		for (State s: states){
			this.checkOutTransitionsFromState(s);
		}
	}
	public void checkOutTransitionsFromState(State s){
		
	}
	public void checkTransitionsLength(StateDiagram sd) throws InconsistentInputLengthException, InconsistentOutputLengthException{
		checkInputsLengths(sd);
		checkOuputLengths(sd);
	}
	public void checkInputsLengths(StateDiagram sd) throws InconsistentInputLengthException {
		ArrayList<Transition> trans=sd.getTransitions();
		Transition test=trans.get(0);
		
		for (Transition transition : trans){
			if(transition.getInput().length()!=test.getInput().length())
				throw new InconsistentInputLengthException();
				
		}
		
	}
	public void checkOuputLengths(StateDiagram sd) throws InconsistentOutputLengthException{
		ArrayList<Transition> t=sd.getTransitions();
		Transition test=t.get(0);
		
		for (Transition transition : t){
			if(transition.getOutput().length()!=test.getInput().length())
				throw new InconsistentOutputLengthException(); 
		}

	}
	public void checkStrongConnectivity(StateDiagram sd) throws notStronglyConnectedDiagram{
		ArrayList<Transition> trans=sd.getTransitions();
		ArrayList<State> states1=(ArrayList<State>) sd.getStates().clone();
		ArrayList<State> states2=(ArrayList<State>)sd.getStates().clone();
		State q0=trans.get(0).getOrigin();
		states1.remove(q0);
		markReachableStatesFrom(q0,states1,sd);
		states2.remove(q0);
		markStatesThatReach(q0,states2,sd);
		boolean isStronglyConnected=states1.isEmpty() && states2.isEmpty();
		if(!isStronglyConnected)
			throw new notStronglyConnectedDiagram();
		return;
	}	
	private void markStatesThatReach(State q, ArrayList<State> states2,
			StateDiagram sd) {
		if(!states2.isEmpty()){
			ArrayList<Transition> ts=sd.getTransitions();
			for(Transition t: ts){
				if (t.getDestiny().equals(q)){
						states2.remove(t.getOrigin());
						markReachableStatesFrom(t.getOrigin(),states2,sd);
				}
			}
		}
		return;
		
	}
	private void markReachableStatesFrom(State q, ArrayList<State> states1, StateDiagram sd){
		if(!states1.isEmpty()){
			ArrayList<Transition> ts=sd.getTransitions();
			for(Transition t: ts){
				if (t.getOrigin().equals(q)){
						states1.remove(t.getDestiny());
						markReachableStatesFrom(t.getDestiny(),states1,sd);
				}
			}
		}
		return;
		
	}

}
