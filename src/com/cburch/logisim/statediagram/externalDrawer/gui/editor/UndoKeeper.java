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




package com.cburch.logisim.statediagram.externalDrawer.gui.editor;

import com.cburch.logisim.statediagram.externalDrawer.diagram.Diagram;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This class will store the states between actions, that we may undo them.
 * Since there should be one set of states per active window, this class should be instantiated
 * within the AutomatonFrame class.
 * 
 * Let's use it statically for now (testing / prototype), so we don't have to change environment frame to instantiate it.
 * @author Henry Qin
 *
 */
public class UndoKeeper { 
    	
    private Diagram myMaster;
    
    private Deque<Diagram> myDeck;
    private Deque<Diagram> myBackDeck;

    //private final int DEFAULT_NUM = 50;

    private int numUndo;
    
    public UndoKeeper(Diagram master){
    	myMaster = master;
    	myDeck = new LinkedList<Diagram>();
    	myBackDeck = new LinkedList<Diagram>();
        numUndo = 50;
    }

   /* public void setNumUndo(int nn){
        numUndo = nn;
    }*/
	
	public boolean sensitive = false;
	private boolean wait = false;
	
   /* public void setWait(){
    	wait = true;
    }*/
	
    public void saveStatus(){
//        EDebug.print("I have been called upon");
        if (wait){
        	wait = false;
        	return;
        }

        myDeck.push((Diagram)myMaster.clone()); //push on head
    	    
//        EDebug.print("The master that is getting pushed on has hascode = " + myMaster.hashCode());
        System.out.println();


        if (myDeck.size() >= 2)
        {
        	Diagram first = myDeck.pop();
        	Diagram second = myDeck.pop();
//            EDebug.print("The first is " + first.hashCode() + "While the second is " + second.hashCode());
        	if (first.hashCode() == second.hashCode()){
        	    myDeck.push(first);	
        	}
        	else{
        	    myDeck.push(second);	
        	    myDeck.push(first);	
                myBackDeck.clear();
        	}
        }

        while (myDeck.size() > numUndo) myDeck.removeLast();
    }
}
