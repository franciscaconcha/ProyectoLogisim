package com.cburch.logisim.statediagram.model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cburch.logisim.statediagram.model.*;
import com.cburch.logisim.statediagram.model.exceptions.AbsentStateException;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

import java.util.ArrayList;
// Falta el test m�s importante: isCorrect()
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
