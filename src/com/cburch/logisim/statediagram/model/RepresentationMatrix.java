package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;

import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

public class RepresentationMatrix {
	private ArrayList<ArrayList<ArrayList<Transition>>> matrix;
	private int size;
	private int inputLength;
	private int outputLength;
	public RepresentationMatrix(int n) throws InvalidTransitionException{
		size=n;
		matrix=new ArrayList<ArrayList<ArrayList<Transition>>>();
		ArrayList<Transition> cell;
		ArrayList<ArrayList<Transition>> row;
		for(int i=0;i<size;i++){
			 row=new ArrayList<ArrayList<Transition>>();
			for(int j=0;j<size;j++){
				cell=new ArrayList<Transition>();
				row.add(cell);
			}
			matrix.add(row);
		}
	}
	public void addTransition(Transition t) throws InvalidTransitionException{
		int o=t.getOrigin().getId();
		int d=t.getDestiny().getId();
		matrix.get(o).get(d).add(t);
		
	}
	public ArrayList<Transition> getTransitions(int o, int d){
		return matrix.get(o).get(d);
	}
	public void setInputLength(int l){
		this.inputLength=l;
	}
	public int getInputLength(){
		return this.inputLength;
	}
	public void setOutputLength(int l){
		this.outputLength=l;
	}
	public int getOutputLength(){
		return this.outputLength;
	}
	public int getSize(){
		return this.size;
	}
}
