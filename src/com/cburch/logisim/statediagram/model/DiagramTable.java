package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.analyze.model.AnalyzerModel;
import com.cburch.logisim.analyze.model.Entry;
import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

public class DiagramTable {
	
	private List<String> inputNames;
	private List<String> outputNames;
	private Entry[][] outputValues; //[Columna][Fila]
	
	private int logicInputNumber; //Numero de bits de entrada del diagrama
	private int logicOutputNumber; //Numero de bits de salida del diagrama
	private int memoryBitNumber; //Numero de bits que necesito para la memoria
	
	//TODO Marcar entradas/salidas al registro
	//TODO Documentar correctamente
	
	/**
	 * Constructor momentaneo, util para demostraciones.
	 * Genera una tabla default.
	 * @throws InvalidTransitionException 
	 * @throws AbsentStateException 
	 */
	public DiagramTable() throws InvalidTransitionException, AbsentStateException{
		inputNames = Arrays.asList("Q0", "X0");
		outputNames = Arrays.asList("D0", "Z0");
		outputValues = new Entry[2][1 << 2];
		outputValues[0][0] = Entry.ONE;
		outputValues[0][1] = Entry.ZERO;
		outputValues[0][2] = Entry.ONE;
		outputValues[0][3] = Entry.ZERO;
		outputValues[1][0] = Entry.ZERO;
		outputValues[1][1] = Entry.ZERO;
		outputValues[1][2] = Entry.ONE;
		outputValues[1][3] = Entry.ONE;
		
		this.memoryBitNumber = 1;
		this.logicInputNumber = 1;
		this.logicOutputNumber = 1;
	}
	
	/**
	 * Crea objeto DiagramTable a partir de un objeto RepresentationMatrix
	 * @param matrix
	 */
	public DiagramTable(RepresentationMatrix matrix){
		this.memoryBitNumber = calculateBitNumber(matrix.getSize());
		this.logicInputNumber = matrix.getInputLength();
		this.logicOutputNumber = matrix.getOutputLength();
		
		this.inputNames= generateNameList("Q", this.memoryBitNumber); //Columnas que identifican estado actual
		this.inputNames.addAll( generateNameList("x", this.logicInputNumber) ); //Columnas que identifican entradas
		
		this.outputNames= generateNameList("D", this.memoryBitNumber); //Columnas que identifican estado siguiente
		this.outputNames.addAll( generateNameList("z", this.logicOutputNumber) ); //Columnas que identifican salidas
		
		//TODO outputValues!!
		this.outputValues = generateValues(matrix);
	}
	
	/**
	 * Genera los valores de las salidas
	 * POR AHORA NO SOPORTA ENTRADAS/SALIDAS con (*) !!!!!!
	 * TODO generalizar para strings de largo > 1
	 * @param matrix
	 * @return
	 */
	private Entry[][] generateValues(RepresentationMatrix matrix) {
		int size = matrix.getSize(); // Numero de estados
		String input, output;
		
		Entry[][] outputValues = new Entry[this.outputNames.size()][1 << this.inputNames.size()]; //[Columna][Fila]
		
		for (int i=0; i<size; i++ ){ //Numero decimal del estado actual || Fila de RepresentationMatrix
			for (int j=0; j<size; j++){ //Numero decimal del estado siguiente || Columna de RepresentationMatrix
				
				input= matrix.getInput(i, j); //string
				output= matrix.getOutput(i, j); //string
				
				if (input.length() == 0) //Si la casilla indica que no hay transicion
					continue;
				else
					process(i, j, input, output, outputValues);
			}
		}
		return outputValues;
	}
	
	private void process(int i, int j, String input, String output, Entry[][] outputValues){
		if (input.length() == 0) //Si no hay transicion, no hace nada
			return;
		
		int index = input.indexOf("*");
		
		if (index != -1){ //Si hay *
			int length = input.length();
			String input0 = input.substring(0, index) + "0" + input.substring(index+1, length);
			String input1 = input.substring(0, index) + "1" + input.substring(index+1, length);
			
			System.out.println(i + " " + j + " " + input0 + " " + input1);
			
			process(i, j, input0, output, outputValues);
			process(i, j, input1, output, outputValues);
			return;
		}
		else{ //Si input es numerico
			int row;
			if (i==0)
				row = Integer.parseInt(input, 2);
			else
				row = (int) (Math.pow(2, i) + Integer.parseInt(input, 2)); //Fila de DiagramTable, general
			
			fillMemoryValues(outputValues, row, Integer.toBinaryString(j)); //general
			fillLogicValues(outputValues, row, output); //general
		}
	}
	
	/**
	 * Llena los valores de los output
	 * @param entry 
	 * @param row
	 * @param output
	 */
	private void fillLogicValues(Entry[][] entry, int row, String output) {
		for (int i = 0; i< this.logicOutputNumber; i++){
			if (output.charAt(i) == '0')
				entry[i+this.memoryBitNumber][row] = Entry.ZERO;
			else if (output.charAt(i) == '1')
				entry[i+this.memoryBitNumber][row] = Entry.ONE;
			else
				entry[i+this.memoryBitNumber][row] = Entry.DONT_CARE;
			System.out.println(output.charAt(i));
		}
	}

	/**
	 * Llena los valores de bit de memoria de fila row segun binaryString
	 * @param entry 
	 * @param row
	 * @param binaryString
	 */
	private void fillMemoryValues(Entry[][] entry, int row, String binaryString) {
		
		if (binaryString.length() < this.memoryBitNumber){ //Agrega 0s faltantes
			int dif = this.memoryBitNumber - binaryString.length();
			for (int k=0; k< dif; k++)
				binaryString = "0" + binaryString;
		}
		System.out.println("binaryS=" + binaryString + " ");
		for (int i = 0; i< this.memoryBitNumber; i++){ //Por cada numero en el string, coincide con columna
			if (binaryString.charAt(i) == '0')
				entry[i][row] = Entry.ZERO;
			else // si es 1
				entry[i][row] = Entry.ONE;
		}
	}

	/**
	 * Retorna el numero de bits necesarios para codificar el parametro de entrada en base 2.
	 * @param stateNumber	Numero a codificar
	 * @return 				Numero de bits necesarios
	 */
	private int calculateBitNumber(int stateNumber){
		return (int) Math.ceil(Math.log(stateNumber)/Math.log(2));
	}
	
	/**
	 * Genera una lista de strings que corresponden a los nombres de las columnas en la tabla
	 * @param name
	 * @param length
	 * @return
	 */
	private List<String> generateNameList(String name, int length){
		List<String> list = new ArrayList();
		for (int i=0; i<length; i++)
			list.add(name + i);
		//System.out.println(list.toString());
		return list;
	}
	
	/**
	 * Actualiza la tabla actual de logisim segun los datos de DiagramTable.
	 * @param model
	 */
	public void loadIntoModel(AnalyzerModel model){
		model.setVariables(this.inputNames, this.outputNames);
		for (int i = 0; i < this.outputValues.length; i++) // i es el numero de columna
			model.getTruthTable().setOutputColumn(i, this.outputValues[i]);
	}
	
	/**
	 * Retorna el numero de bits de memoria utilizados para codificar estados
	 * @return
	 */
	public int getMemoryBitNumber(){
		return this.memoryBitNumber;
	}
	
	/**
	 * Retorna el numero de inputs del diagrama.
	 * @return
	 */
	public int getInputNumber(){
		return this.logicInputNumber;
	}
	
	/**
	 * Retorna el numero de outputs del diagrama.
	 * @return
	 */
	public int getOutputNumber(){
		return this.logicOutputNumber;
	}
}
