/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */





package com.cburch.logisim.statediagram.externalDrawer.diagram;

import java.awt.*;
import java.io.Serializable;

/**
 * A <CODE>Transition</CODE> object is a simple abstract class representing a
 * transition between two state objects in an diagram. Subclasses of this
 * transition class will have additional fields containing the particulars
 * necessary for their transition.
 * 
 * @see StateObject
 * @see Diagram
 * 
 * @author Thomas Finley, Henry Qin
 */

public abstract class TransitionObject implements Serializable, Cloneable {
	/**
	 * Instantiates a new <CODE>Transition</CODE>.
	 * 
	 * @param from
	 *            the state this transition is from
	 * @param to
	 *            the state this transition moves to
	 */
	public TransitionObject(StateObject from, StateObject to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Returns a copy of this transition, except for a new <CODE>from</CODE>
	 * and <CODE>to</CODE> state.
	 * 
	 * @param from
	 *            the state this transition goes to
	 * @param to
	 *            the state this transition comes from
	 * @return a copy of this transition as described
	 */
	public abstract TransitionObject copy(StateObject from, StateObject to);

	/**
	 * Returns a copy of this transition with the same <CODE>from</CODE> and
	 * <CODE>to</CODE> state.
	 * 
	 * @return a copy of this transition as described
	 */
	public Object clone() {
		TransitionObject res = copy(getFromState(), getToState());
        res.isSelected = this.isSelected;
        res.myControlPoint = this.myControlPoint == null? null : new Point(this.myControlPoint);
        return res;
	}

	/**
	 * Returns the state this transition eminates from.
	 * 
	 * @return the state this transition eminates from
	 */
	public StateObject getFromState() {
		return this.from;
	}

	/**
	 * Returns the state this transition travels to.
	 * 
	 * @return the state this transition travels to
	 */
	public StateObject getToState() {
		return this.to;
	}
	/**
	 * Sets the state the transition starts at.
	 * @param newFrom the state the transition starts at
	 */
	public void setFromState(StateObject newFrom) {
		this.from = newFrom;
	}

	/**
	 * Sets the state the transition goes to.
	 * @param newTo the state the transition goes to
	 */
	public void setToState(StateObject newTo) {
		this.to = newTo;
	}

	/**
	 * Returns the diagram this transition is over.
	 * 
	 * @return the diagram this transition is over
	 */
	public Diagram getDiagram() {
		return this.from.getDiagram();
	}

	/**
	 * Gets the description for a Transition. This defaults to nothing.
	 * Subclasses should override.
	 * 
	 * @return an empty string
	 */
	public String getDescription() {
		return "";
	}

	/**
	 * Returns a string representation of this object. The string returned is
	 * the string representation of the first state, and the string
	 * representation of the second state.
	 * 
	 * @return a string representation of this object
	 */
	public String toString() {
		return "[" + getFromState().toString() + "] -> ["
				+ getToState().toString() + "]";
	}

	/**
	 * Returns if this transition equals another object.
	 * 
	 * @param object
	 *            the object to test against
	 * @return <CODE>true</CODE> if the two are equal, <CODE>false</CODE>
	 *         otherwise
	 */
	public boolean equals(Object object) {
		try {
			TransitionObject t = (TransitionObject) object;
			return from == t.from && to == t.to;
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Returns the hash code for this transition.
	 * 
	 * @return the hash code for this transition
	 */
	public int hashCode() {
		return from.hashCode() ^ to.hashCode();
	}
	
    /**
      *This hash code is specifically for dealing with clone matching.
    */

	public int specialHash(){
		int t = from == to ? from.specialHash() : from.specialHash() ^ to.specialHash();
        if (myControlPoint != null) t ^= myControlPoint.hashCode();
        return t;
	}

    public Point getControl(){
        return myControlPoint;
    }

    public void setControl(Point p){
        myControlPoint = p; 
    }


	/** The states this transition goes between. */
	protected StateObject from, to;



    /**The control point, if this transition is under manual control*/
    private Point myControlPoint;

    public boolean isSelected = false;

    /**
     * A <CODE>PDATransition</CODE> is a <CODE>Transition</CODE> object with
     * additional fields for the label (input to read), the string to pop off the
     * stack, and the string to push on the stack.
     *  *
     * @author Ryan Cavalcante
     */

    public static class PDATransition extends TransitionObject {
        /**
         * Instantiates a new <CODE>PDATransition</CODE> object.
         *
         * @param from
         *            the state this transition comes from
         * @param to
         *            the state this transition goes to
         * @param inputToRead
         *            the string that the machine should satisfy before moving on to
         *            the next state.
         * @param stringToPop
         *            the string that the machine should pop from the stack.
         * @param stringToPush
         *            the string that the machine should push on to the stack.
         */
        public PDATransition(StateObject from, StateObject to, String inputToRead,
                String stringToPop, String stringToPush) {
            super(from, to);
            setInputToRead(inputToRead);
            setStringToPop(stringToPop);
            setStringToPush(stringToPush);
        }

        /**
         * Returns a copy of this transition with new from and to states.
         *
         * @param from
         *            the new from state for the returned transition
         * @param to
         *            the new to state for the returned transition
         * @return a copy of this trnasition with the new from and to states
         */
        public TransitionObject copy(StateObject from, StateObject to) {
            return new PDATransition(from, to, getInputToRead(), getStringToPop(),
                    getStringToPush());
        }

        /**
         * Returns the input to read portion of the transition label for this
         * transition.
         */
        public String getInputToRead() {
            return myInputToRead;
        }

        /**
         * Sets the input to read portion of the transition label for this
         * transition.
         *
         * @param inputToRead
         *            the input to read portion of the transition label.
         */
        protected void setInputToRead(String inputToRead) {
            /*
             * if (!automata.StringChecker.isAlphanumeric(inputToRead)) throw new
             * IllegalArgumentException("Label must be alphanumeric!");
             */
            myInputToRead = inputToRead;
        }

        /**
         * Returns the string to pop from stack portion of the transition label for
         * this transition.
         */
        public String getStringToPop() {
            return myStringToPop;
        }

        /**
         * Sets the string to pop from stack portion of the transition label for
         * this transition.
         *
         * @param stringToPop
         *            the string to pop from the stack.
         */
        protected void setStringToPop(String stringToPop) {
            /*
             * if (!automata.StringChecker.isAlphanumeric(stringToPop)) throw new
             * IllegalArgumentException("Pop string must "+ "be alphanumeric!");
             */
            if (stringToPop.length() > 1){
                throw new IllegalArgumentException("Pop string must have no more than one character!");
            }
            myStringToPop = stringToPop;
        }

        /**
         * Returns the string to push on to the stack portion of the transition
         * label for this transition.
         */
        public String getStringToPush() {
            return myStringToPush;

        }

        /**
         * Sets the string to push on to the stack portion of the transition label
         * for this transition.
         *
         * @param stringToPush
         *            the string to push on to the stack.
         */
        protected void setStringToPush(String stringToPush) {
            /*
             * if (!automata.StringChecker.isAlphanumeric(stringToPush)) throw new
             * IllegalArgumentException("Push string must "+ "be alphanumeric!");
             */
            if ( stringToPush.length() > 1)
                throw new IllegalArgumentException(
                        "Push string must have no more than one character!");
            myStringToPush = stringToPush;
        }

        /**
         * Returns the description for this transition.
         *
         * @return the description, in this case, the input to read, the string to
         *         pop off the stack, and the string to push on the stack.
         */
        public String getDescription() {
            String input = getInputToRead();
            if (input.length() == 0)
                input = "\u03BB";
            String toPop = getStringToPop();
            if (toPop.length() == 0)
                toPop = "\u03BB";
            String toPush = getStringToPush();
            if (toPush.length() == 0)
                toPush = "\u03BB";
            return input + " , " + toPop + " ; " + toPush;
        }

        /**
         * Returns the hashcode for this transition.
         *
         * @return the hashcode for this transition
         */
        public int hashCode() {
            return super.hashCode() ^ myInputToRead.hashCode()
                    ^ myStringToPop.hashCode() ^ myStringToPush.hashCode();
        }

        /**
         * Tests this transition against another object for equality.
         *
         * @param object
         *            the object to test for equality
         * @return <CODE>true</CODE> if this transition equals the passed in
         *         object, <CODE>false</CODE> otherwise
         */
        public boolean equals(Object object) {
            try {
                PDATransition t = (PDATransition) object;
                return super.equals(object)
                        && myInputToRead.equals(t.myInputToRead)
                        && myStringToPop.equals(t.myStringToPop)
                        && myStringToPush.equals(t.myStringToPush);
            } catch (ClassCastException e) {
                return false;
            }
        }

        /**
         * Returns a string representation of this object. This is the same as the
         * string representation for a regular transition object, with the
         * additional fields tacked on.
         *
         * @see TransitionObject#toString
         * @return a string representation of this object
         */
        public String toString() {
            return super.toString() + ": \"" + getInputToRead() + "\"" + ": \""
                    + getStringToPop() + "\"" + ": \"" + getStringToPush() + "\"";
        }

        /** The input to read portion of the transition label. */
        protected String myInputToRead;

        /** The string to pop off the stack. */
        protected String myStringToPop;

        /** The string to push on the stack. */
        protected String myStringToPush;
    }
}