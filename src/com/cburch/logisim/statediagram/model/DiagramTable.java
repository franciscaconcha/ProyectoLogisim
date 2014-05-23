package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.analyze.model.AnalyzerModel;
import com.cburch.logisim.analyze.model.Entry;

public class DiagramTable {
	
	private List<String> inputNames;
	private List<String> outputNames;
	private Entry[][] outputValues; //[Columna][Fila]
	
	private int logicInputNumber; //Numero de bits de entrada del diagrama
	private int logicOutputNumber; //Numero de bits de salida del diagrama
	private int memoryBitNumber; //Numero de bits que necesito para la memoria
	
	//TODO Marcar entradas/salidas al registro
	//TODO Documentar correctamente
	//TODO Constructor que reciba objeto RepresentationMatrix
	
	/**
	 * Constructor momentaneo, util para demostraciones.
	 * Genera una tabla default.
	 */
	public DiagramTable(){
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
		
		this.inputNames= generateNameList("Q", this.memoryBitNumber);
		this.inputNames.addAll( generateNameList("x", this.logicInputNumber) );
		
		this.outputNames= generateNameList("D", this.memoryBitNumber);
		this.outputNames.addAll( generateNameList("z", this.logicOutputNumber) );
		//Falta outputValues!!
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
	 * Revisar!
	 * @param name
	 * @param length
	 * @return
	 */
	private List<String> generateNameList(String name, int length){
		List<String> list = new ArrayList();
		for (int i=0; i<length; i++)
			list.add(name + i);
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
}
