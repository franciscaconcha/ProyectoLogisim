package com.cburch.logisim.statediagram.model;

import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;
/**
 * 
 * @author Cate
 * Clase de la Matriz de Representacion. Tiene muchos getters y setters para 
 * acceder a la matriz por indice. Es basicamente la matriz de adyacencia de 
 * un grafo, pero guarda 2 strings en cada celda, input y output.
 * Para acceder a la transicion que va desde el estado A (id a) al estado B 
 * (id b), hay que hacer get/set Input/Output (a,b (String a settear si 
 * corresponde)). Las dimensiones de la matriz estan determinadas por size, que
 * es la cantidad de estados del diagrama a partir del cual se quiere generar
 * la matriz, luego la matriz es de tama�o nxnx2. 
 */
public class RepresentationMatrix {
	private String[][][] matrix;
	private int size;
	private int inputLength;
	private int outputLength;
	public RepresentationMatrix(int n){
		size=n;
		this.matrix= new String [n][n][2];
		for(int i=0;i<size;i++)
			for(int j=0;j<n;j++){
				this.setInput(i, j, "");
				this.setOutput(i, j, "");
			}
		
	}
	
	public String getInput(int o, int d){
		return matrix[o][d][0];
	}
	public String getOutput(int o, int d){
		return matrix[o][d][1];
	}
	public void setInput(int o, int d, String in){
		 matrix[o][d][0]=in;
	}
	public void setOutput(int o, int d, String out){
		matrix[o][d][1]=out;
	}
	public String [][][] getMatrix() {
		return matrix;
	}

	public int getSize() {
		return size;
	}
	public int getInputLength(){
		return this.inputLength;
	}
	public int getOutputLength(){
		return this.outputLength;
	}
	public void setInputLength(int n){
		this.inputLength=n;
	}
	public void setOutputLength(int n){
		this.outputLength=n;
	}
		

}
