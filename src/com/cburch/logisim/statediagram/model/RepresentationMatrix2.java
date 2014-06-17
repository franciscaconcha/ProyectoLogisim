package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;

import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

public class RepresentationMatrix2 {
	private ArrayList<ArrayList<ArrayList<Transition>>> matrix;
	private int size;
	private int inputLength;
	private int outputLength;
	public RepresentationMatrix2(int n) throws InvalidTransitionException{
		size=n;
		matrix=new ArrayList<ArrayList<ArrayList<Transition>>>();
		Transition blank=new Transition(null, null, "", "");
		ArrayList<Transition> al1=new ArrayList<Transition>();
		al1.add(blank); // TODO corregir copia, Lady Catherine
		ArrayList<ArrayList<Transition>> al2=new ArrayList<ArrayList<Transition>>();
		for(int i=0;i<size;i++)
			al2.add(al1);
		ArrayList<ArrayList<ArrayList<Transition>>> al3=new ArrayList<ArrayList<ArrayList<Transition>>>();
		for(int i=0;i<size;i++)
			matrix.add(al2);
		
	}
	public void addTransition(Transition t) throws InvalidTransitionException{
		int o=t.getOrigin().getId();
		int d=t.getDestiny().getId();
		Transition blank=new Transition(null, null, "", "");
		if(matrix.get(o).get(d).get(0).equals(blank)){
			matrix.get(o).get(d).remove(0);
		}
		matrix.get(o).get(d).add(t);
		
	}
	public ArrayList<Transition> getTransitions(int o, int d){
		return matrix.get(o).get(d);
	}
	public void setInputLength(int l){
		this.inputLength=l;
	}
	public int getInputLength(int l){
		return this.inputLength;
	}
	public void setOutputLength(int l){
		this.outputLength=l;
	}
	public int getOutputLength(int l){
		return this.outputLength;
	}
	public int getSize(){
		return this.size;
	}
}
