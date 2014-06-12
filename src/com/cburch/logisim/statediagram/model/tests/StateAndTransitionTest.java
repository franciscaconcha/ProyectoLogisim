package com.cburch.logisim.statediagram.model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cburch.logisim.statediagram.model.State;
import com.cburch.logisim.statediagram.model.Transition;
import com.cburch.logisim.statediagram.model.exceptions.InvalidTransitionException;

public class StateAndTransitionTest {
	State s1;
	State s2;
	State s3;
	Transition t1;
	Transition t2;
	Transition t3;

	@Test
	public void stateTests() {
		int z=0;
		int o=1;
		String a= "state0";
		String b="state1";
		s1=new State(0, "state0");
		s2=new State(1, "state1");
		assertFalse(s1.equals(s2));
		assertEquals(z,s1.getId());
		assertEquals(a,s1.getName());
		assertEquals(a, s1.toString());
		s2.setId(z);
		assertEquals(z, s2.getId());
		assertFalse(s1.equals(s2));
		s2.setName(a);
		assertEquals(a,s2.getName());
		assertTrue(s2.equals(s1));
	}
	@Test
	public void equalTransitionTest() throws InvalidTransitionException{
		int z=0;
		int o=1;
		String a= "state0";
		String b="state1";
		String in="0*";
		String out="*1";
		String toString=a+"--"+in+"/"+out+"->"+b;
		s1=new State(0, "state0");
		s2=new State(1, "state1");
		t1=new Transition(s1, s2, in, out);
		t2=new Transition(s1, s2, in, out);
		t3=new Transition(s2,s1,out,in);
		assertTrue(t1.equals(t2));
		assertFalse(t1.equals(t3));
	}
	@Test(expected=InvalidTransitionException.class)
	public void invalidTransitionTest() throws InvalidTransitionException{
		int z=0;
		int o=1;
		String a= "state0";
		String b="state1";
		String in="a";
		String out="b";
		String in2="1*";
		String out2="*0";
		s1=new State(0, "state0");
		s2=new State(1, "state1");
		t1=new Transition(s1, s2, in, out);
		t2=new Transition(s1, s2, in2, out);
		t3=new Transition(s1, s2, in, out2);
	}
	@Test
	public void validTransitionTest() throws InvalidTransitionException{
		int z=0;
		int o=1;
		String a= "state0";
		String b="state1";
		String in="0*";
		String out="*1";
		String toString=a+"--"+in+"/"+out+"->"+b;
		s1=new State(0, "state0");
		s2=new State(1, "state1");
		t1=new Transition(s1, s2, in, out);
		assertTrue(s1.equals(t1.getOrigin()));
		assertTrue(s2.equals(t1.getDestiny()));
		assertEquals(in,t1.getInput());
		assertEquals(out,t1.getOutput());
		assertEquals(toString,t1.toString());
		t1.setInput(out);
		assertEquals(out, t1.getInput());
		t1.setOutput(in);
		assertEquals(in, t1.getOutput());
		t1.setDestiny(s1);
		assertTrue(s1.equals(t1.getDestiny()));
		t1.setOrigin(s2);
		assertTrue(s2.equals(t1.getOrigin()));
	}
}