package com.cburch.logisim.statediagram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.analyze.model.AnalyzerModel;
import com.cburch.logisim.analyze.model.Entry;

public class DiagramTable {
	
	private List<String> inputNames;
	private List<String> outputNames;
	private Entry[][] outputValues;
	
	private int logicInputNumber; //Numero de bits de entrada del diagrama
	private int logicOutputNumber; //Numero de bits de salida del diagrama
	private int memoryBitNumber; //Numero de bits que necesito para la memoria
	
	//TODO Marcar entradas/salidas al registro
	//TODO Documentar correctamente
	//TODO Constructor que reciba objeto RepresentationMatrix
	
	/**
	 * Constructor momentaneo, util para demostraciones. Genera una tabla default.
	 */
	public DiagramTable(){
		List<String> inputNames = Arrays.asList("Q0", "X0");
		List<String> outputNames = Arrays.asList("D0", "Z0");
		Entry[][] outputValues = new Entry[2][1 << 2];
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
		
		this.inputNames = inputNames;
		this.outputNames = outputNames;
		this.outputValues = outputValues;
	}
	
	public void loadIntoModel(AnalyzerModel model){
		model.setVariables(this.inputNames, this.outputNames);
		for (int i = 0; i < this.outputValues.length; i++) // i es el numero de columna
			model.getTruthTable().setOutputColumn(i, this.outputValues[i]);
	}
	
	//
	//De aqui en adelante es trabajo por completar. De momento no se utiliza.
	//
	
	public DiagramTable(RepresentationMatrix matrix){
		
	}
	
	/**
	 * Constructor momentaneo de DiagramTable. Inicializa lo minimo de DiagramTable.
	 * Aun por decidir la version final.
	 * @param stateNumber	Numero de estados del diagrama
	 * @param logicInputNumber	Numero de inputs del diagrama (no considera los inputs que representan memoria)
	 * @param logicOutputNumber	Numero de outputs del diagrama (no considera los outputs que representan memoria)
	 */
	public DiagramTable(int stateNumber, int logicInputNumber, int logicOutputNumber) {
		this.memoryBitNumber = (int) Math.ceil(Math.log(stateNumber)/Math.log(2));
		this.logicInputNumber = logicInputNumber;
		this.logicOutputNumber = logicOutputNumber;
		
		this.inputNames = new ArrayList<String>();
		this.outputNames = new ArrayList<String>();
		this.outputValues = new Entry[logicOutputNumber+memoryBitNumber][1 << (logicInputNumber + memoryBitNumber)]; //[Columnas][Filas]
	}
	
	public DiagramTable(List<String> inputNames, List<String> outputNames, Entry[][] outputValues, int memoryBitNumber){
		this.memoryBitNumber = memoryBitNumber;
		this.logicInputNumber = inputNames.size() - memoryBitNumber;
		this.logicOutputNumber = outputNames.size() - memoryBitNumber;
		
		this.inputNames = inputNames;
		this.outputNames = outputNames;
		this.outputValues = outputValues; //[Columnas][Filas]
	}
}
