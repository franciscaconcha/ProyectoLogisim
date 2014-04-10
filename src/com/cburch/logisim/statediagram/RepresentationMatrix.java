package com.cburch.logisim.statediagram;

import com.cburch.logisim.statediagram.StateDiagram.AbsentStateException;
import com.cburch.logisim.statediagram.Transition.InvalidTransitionException;

public class RepresentationMatrix {
	private String[][][] matrix;
	private int size;
	public RepresentationMatrix(int n){
		size=n;
		this.matrix= new String [n][n][2];	
	}
	public StateDiagram generateDiagram(){
		StateDiagram s= new StateDiagram();
		int i=1;
		while (i<=this.getSize())
				s.addState(i+"");
		int j, k;
		Transition t;
		for(j=0;j<size;j++)
			for(k=0;k<size;k++)
				if (!matrix[j][k][0].isEmpty()){
					try {
						t=new Transition(s.getState(j), s.getState(k), matrix[j][k][0], matrix[j][k][1]);
						s.addTransition(t);
					} catch (InvalidTransitionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AbsentStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		return s;			
	}
	public String [][][] getMatrix() {
		return matrix;
	}
	public void setMatrix(String[][][] matrix) {
		this.matrix = matrix;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
		

}
