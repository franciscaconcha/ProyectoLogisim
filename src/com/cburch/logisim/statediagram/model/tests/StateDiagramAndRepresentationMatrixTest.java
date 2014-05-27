package com.cburch.logisim.statediagram.model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cburch.logisim.statediagram.model.*;

import java.util.ArrayList;
// Falta el test más importante: isCorrect()
public class StateDiagramAndRepresentationMatrixTest {
StateDiagram sd;
State s1,s2,s3,s4;
Transition t1,t2,t3,t4;
@Test
public void statesTest(){
	sd=new StateDiagram();
	ArrayList<State>sList=new ArrayList<State>();
	s1=new State(0,"s1");
	sd.addState("s1", 0);
	s2=new State(1,"s2");
	sd.addState("s2", 1);
	s3=new State(2,"s3");
	sd.addState("s3", 2);
	s4=new State(3,"s4");
	assertTrue(s1.equals(sd.getState(0)));
	assertTrue(sd.isState(s2));
	assertTrue(s3.equals(sd.getStates().get(2)));
	assertFalse(sd.isState(s4));
}
@Test
public void validTransitionTest() throws InvalidTransitionException, AbsentStateException{
	sd=new StateDiagram();
	sd.addState("s1", 0);
	s1=new State(0,"s1");
	sd.addState("s2", 1);
	s2=new State(1,"s2");
	sd.addState("s3", 2);
	s3=new State(2,"s3");
	assertTrue(sd.isState(s1));
	assertTrue(sd.isState(s2));
	sd.addTransition(s1, s2, "*", "*");
	t1=new Transition(s1, s2, "*", "*");
	t2=new Transition(s2, s1, "*", "*");
	assertTrue(sd.isTransition(t1));
	assertTrue(sd.getTransitions().get(0).equals(t1));
	assertFalse(sd.isTransition(t2));
}
@Test
public void representationMatrixTest() throws InvalidTransitionException, AbsentStateException{
	s1=new State(0, "s1");
	s2=new State(1, "s2");
	sd=new StateDiagram();
	sd.addState(s1.getName(), s1.getId());
	sd.addState(s2.getName(), s2.getId());
	t1=new Transition(s1, s2, "1", "0");
	t2=new Transition(s2, s1, "0", "1");
	t3=new Transition(s1, s1, "0", "0");
	t4=new Transition(s2,s2,"1","1");
	String[][][] mat=new String[2][2][2];
	mat[0][1][0]=t1.getInput();
	mat[0][1][1]=t1.getOutput();
	mat[1][0][0]=t2.getInput();
	mat[1][0][1]=t2.getOutput();
	mat[0][0][0]=t3.getInput();
	mat[0][0][1]=t3.getOutput();
	mat[1][1][0]=t4.getInput();
	mat[1][1][1]=t4.getOutput();

	sd.addTransition(t1.getOrigin(), t1.getDestiny(), t1.getInput(), t1.getOutput());
	sd.addTransition(t2.getOrigin(), t2.getDestiny(), t2.getInput(), t2.getOutput());
	sd.addTransition(t3.getOrigin(), t3.getDestiny(), t3.getInput(), t3.getOutput());
	sd.addTransition(t4.getOrigin(), t4.getDestiny(), t4.getInput(), t4.getInput());
	RepresentationMatrix repMat=sd.getRepresentationMatrix();
	assertEquals(2, repMat.getSize());
	assertEquals(1, repMat.getInputLength());
	assertEquals(1, repMat.getOutputLength());
	assertEquals(mat[0][1][0], repMat.getInput(0, 1));
	assertEquals(mat[0][1][1], repMat.getOutput(0, 1));
	assertEquals(mat[1][0][0], repMat.getInput(1, 0));
	assertEquals(mat[1][0][1], repMat.getOutput(1, 0));
	assertEquals(mat[0][0][0], repMat.getInput(0, 0));
	assertEquals(mat[0][0][1], repMat.getOutput(0, 0));
	assertEquals(mat[1][1][0], repMat.getInput(1, 1));
	assertEquals(mat[1][1][1], repMat.getOutput(1, 1));

	String[][][] sMat=repMat.getMatrix();
	assertEquals(mat[0][1][0], sMat[0][1][0]);
	assertEquals(mat[0][1][1], sMat[0][1][1]);
	assertEquals(mat[1][0][0], sMat[1][0][0]);
	assertEquals(mat[1][0][1], sMat[1][0][1]);
	assertEquals(mat[0][0][0], sMat[0][0][0]);
	assertEquals(mat[0][0][1], sMat[0][0][1]);
	assertEquals(mat[1][1][0], sMat[1][1][0]);
	assertEquals(mat[1][1][1], sMat[1][1][1]);

}
@Test(expected=AbsentStateException.class)
public void invalidTransitionTest() throws InvalidTransitionException, AbsentStateException{
	sd=new StateDiagram();
	sd.addState("s1", 0);
	s1=new State(0,"s1");
	s2=new State(1,"s1");
	s4=new State(3,"s4");
	sd.addTransition(s1, s4, "1", "0");
	sd.addTransition(s2, s4, "1", "0");
	
	
}

}
