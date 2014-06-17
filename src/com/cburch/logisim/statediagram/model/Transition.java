package com.cburch.logisim.statediagram.model;

import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

/**
 * Clase Transicion. Contiene setters y getters para los estados que relaciona la transicion 
 * (origen y destino) y para los strings de la transicion (input y output).
 * @author Cate
 *
 */

public class Transition {
	private State origin;
	private State destiny;
	private String input;
	private String output;
	
	public Transition(State o, State d, String in, String out) throws InvalidTransitionException{
		boolean b=checkTransition(in, out);
		if (b){
			this.origin=o;
			this.destiny=d;
			this.input=in;
			this.output=out;
		}
		else{
			throw new InvalidTransitionException();
			}
		
	}
	public String toString(){
		String s=origin.toString()+"--"+input+"/"+output+"->"+destiny.toString();
		return s;
	}
	/**
	 * Verifica que los strings de la transicion sean validos
	 * @see checkTransitionMember
	 * @param in
	 * @param out
	 * @return validIn && validOut
	 */
	private boolean checkTransition(String in, String out){
		boolean validIn=checkTransitionMember(in);
		boolean validOut=checkTransitionMember(out);
		return (validIn && validOut);
	}
	/**
	 * Verifica que cada caracter en un string sea 0, 1 o *
	 * @param mem
	 * @return valid
	 */
	private boolean checkTransitionMember(String mem){
		boolean valid=true;
		char[] charArray=mem.toCharArray();
		if(mem.length()==0){
			return valid;
		}
		for(char c: charArray){
			if(c!='1' && c!='0' && c!='*')
				valid=false;
		}
		return valid;
	}
	public boolean equals(Transition t){
		return (this.getOrigin()==t.getOrigin()) && (this.getDestiny()==t.getDestiny()
				&& (this.getInput()==t.getInput())&&(this.getOutput()==t.getOutput()));
	}
	public State getOrigin() {
		return origin;
	}
	public void setOrigin(State origin) {
		this.origin = origin;
	}
	public State getDestiny() {
		return destiny;
	}
	public void setDestiny(State destiny) {
		this.destiny = destiny;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

}
