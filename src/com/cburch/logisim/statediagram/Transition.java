package com.cburch.logisim.statediagram;


public class Transition {
	static class InvalidTransitionException extends Exception {	}
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
	private boolean checkTransition(String in, String out){
		boolean validIn=checkTransitionMember(in);
		boolean validOut=checkTransitionMember(out);
		return (validIn && validOut);
	}
	private boolean checkTransitionMember(String mem){
		boolean valid=true;
		char[] charArray=mem.toCharArray();
		for(char c: charArray){
			if(c!='1' && c!='0' && c!='*')
				valid=false;
		}
		return valid;

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