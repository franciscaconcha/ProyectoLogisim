package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;

import com.cburch.logisim.statediagram.model.exceptions.InconsistentInputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.InconsistentOutputLengthException;
import com.cburch.logisim.statediagram.model.exceptions.MissingTransitionException;
import com.cburch.logisim.statediagram.model.exceptions.NoStatesException;
import com.cburch.logisim.statediagram.model.exceptions.NoTransitionsException;
import com.cburch.logisim.statediagram.model.exceptions.NotStronglyConnectedDiagram;
import com.cburch.logisim.statediagram.model.exceptions.RepeatedTransitionException;




public class StateDiagramChecker {
	public StateDiagramChecker(){}
	
	public void checkAll(StateDiagram sd) throws InconsistentInputLengthException, InconsistentOutputLengthException, NotStronglyConnectedDiagram, MissingTransitionException, RepeatedTransitionException, NoStatesException, NoTransitionsException{
		this.checkComponentEmpyness(sd);
		this.checkTransitionsLength(sd);
		this.checkNoRepeatedTransitions(sd);
		this.checkTransitionCompleteness(sd);
		//this.checkStrongConnectivity(sd);

	}
	private void checkComponentEmpyness(StateDiagram sd) throws NoStatesException, NoTransitionsException {
		if(sd.getStates().isEmpty())
			throw new NoStatesException();
		if(sd.getTransitions().isEmpty())
			throw new NoTransitionsException();
		
	}

	public void checkNoRepeatedTransitions(StateDiagram sd) throws RepeatedTransitionException{
		for(State s: sd.getStates())
			this.checkNoRepeatedTransitionsFromState(s,sd);
		
	}
	private void checkNoRepeatedTransitionsFromState(State s, StateDiagram sd) throws RepeatedTransitionException {
		ArrayList<String> thisStateOutTransitionInputs=new ArrayList<String>();
		for(Transition t: sd.getTransitions()){
			if(t.getOrigin().equals(s))
				if(thisStateOutTransitionInputs.contains(t.getOrigin()))
					throw new RepeatedTransitionException();
				thisStateOutTransitionInputs.add(t.getInput());
		}
		//TODO checkear si hay redundancia considerando asteriscos
	}

	public void checkTransitionCompleteness(StateDiagram sd) throws MissingTransitionException{
		ArrayList<State> states=sd.getStates();
		for (State s: states){
			this.checkOutTransitionsFromState(s,sd);
		}
	}
	public void checkOutTransitionsFromState(State s, StateDiagram sd) throws MissingTransitionException{
		int l=sd.getRepresentationMatrix().getInputLength();
		int possibleTransitions=(int)Math.pow(2, l);
		int[] checkList=new int[possibleTransitions];
		ArrayList<Transition> trans=sd.getTransitions();
		String in;
		int n;
		for(Transition t: trans){
			if(t.getOrigin().equals(s)){
				in=t.getInput();
				if(in.contains("*")){
					this.getNumberFromTransitionWithWildcard(t,checkList);
				}
				else{
					this.getNumberFromTransition(t.getInput(),checkList);
				}
			}
		}
		int sum=0;
		for(int i=0;i<possibleTransitions;i++)
			sum+=checkList[i];
		if(sum<possibleTransitions)
			throw new MissingTransitionException();
	}
	public void getNumberFromTransitionWithWildcard(Transition t, int[] checkList) {
		ArrayList<String> equivalentTrans=new ArrayList<String>();
		String originalInput=t.getInput();
		equivalentTrans.add(originalInput);
		this.equivalentTransitionsGetter(equivalentTrans);
		for(String s: equivalentTrans)
			this.getNumberFromTransition(s, checkList);
		
	}

	private void equivalentTransitionsGetter(ArrayList<String> equivalentTrans) {
		for(String s: equivalentTrans){
			if(s.contains("*")){
				int index=s.indexOf("*");
				String remove=equivalentTrans.get(index);
				equivalentTrans.remove(index);
				String add1=remove.replaceFirst("*", "0");
				String add2=remove.replaceFirst("*", "0");
				ArrayList<String> aux=new ArrayList<String>();
				aux.add(add1);
				aux.add(add2);
				this.equivalentTransitionsGetter(aux);
				equivalentTrans.addAll(aux);
			}
		}
		
	}

	public void getNumberFromTransition(String s, int[] checkList) {
		int l=s.length();
		int number=0;
		int i; char c;
		for(i=0;i<l;i++){
			c=s.charAt(i);
			number=2*number+Character.getNumericValue(c);
		}
		checkList[number]=1;
		
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
	public void checkStrongConnectivity(StateDiagram sd) throws NotStronglyConnectedDiagram{
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
			throw new NotStronglyConnectedDiagram();
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
