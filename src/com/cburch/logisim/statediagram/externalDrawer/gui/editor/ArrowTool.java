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

import com.cburch.logisim.statediagram.externalDrawer.diagram.StateObject;
import com.cburch.logisim.statediagram.externalDrawer.diagram.TransitionObject;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.CurvedArrow;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramDrawer;
import com.cburch.logisim.statediagram.externalDrawer.gui.viewer.DiagramPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * The arrow tool is used mostly for editing existing objects.
 * 
 * @author Thomas Finley, Henry Qin
 */

public class ArrowTool extends Tool {


	/**
	 * Instantiates a new arrow tool.
	 * 
	 * @param view
	 *            the view where the diagram is drawn
	 * @param drawer
	 *            the object that draws the diagram
	 */
	public ArrowTool(DiagramPane view, DiagramDrawer drawer) {
		super(view, drawer);
		this.creator = TransitionCreator.creatorForAutomaton(getDiagram(),
				getView());
	}

	/**
	 * Gets the tool tip for this tool.
	 * 
	 * @return the tool tip for this tool
	 */
	public String getToolTip() {
		return "Attribute Editor";
	}

	/**
	 * Returns the tool icon.
	 * 
	 * @return the arrow tool icon
	 */
	protected Icon getIcon() {
		java.net.URL url = getClass().getResource("/resources/ICON/arrow.gif");
		return new javax.swing.ImageIcon(url);
	}

	/**
	 * On a mouse click, if this is a double click over a transition edit the
	 * transition. If this was a single click, then we select the transition.
	 * 
	 * @param event
	 *            the mouse event
	 */
	public void mouseClicked(MouseEvent event) {
		if (event.getClickCount() == 1){
            TransitionObject trans = getDrawer().transitionAtPoint(event.getPoint());
            if (trans != null){
            	
                if (trans.isSelected){
                    trans.isSelected = false;
                    selectedTransition = null;
                } 
                else{
                    if (selectedTransition != null) selectedTransition.isSelected = false;
                    trans.isSelected = true;
                    selectedTransition = trans;
                     
                }
                return;
            }else{
            	if (selectedTransition!= null)
            		selectedTransition.isSelected = false;
            	selectedTransition = null;
            }
        }
		TransitionObject trans = getDrawer().transitionAtPoint(event.getPoint());
		if (trans == null){
			Rectangle bounds;
			bounds = new Rectangle(0, 0, -1, -1);
			getView().getDrawer().getDiagram().selectStatesWithinBounds(bounds);
			getView().repaint();
			return;
		}
		creator.editTransition(trans, event.getPoint());

	}

	/**
	 * Possibly show a popup menu.
	 * 
	 * @param event
	 *            the mouse event
	 */
	protected void showPopup(MouseEvent event) {
		// Should we show a popup menu?
		if (event.isPopupTrigger()) {
			Point p = getView().transformFromDiagramToView(event.getPoint());
			if (lastClickedState != null && shouldShowStatePopup()) {
				stateMenu.show(lastClickedState, getView(), p);
			} else {
				emptyMenu.show(getView(), p);
			}
		}
		lastClickedState = null;
		lastClickedTransition = null;
	}

	/**
	 * On a mouse press, allows the state to be dragged about unless this is a
	 * popup trigger.
	 */
	public void mousePressed(MouseEvent event) {
		
       // if (getDrawer().getDiagram().getEnvironmentFrame() ==null)
        initialPointClick.setLocation(event.getPoint());
		
        lastClickedState = getDrawer().stateAtPoint(event.getPoint());
		if (lastClickedState == null)
			lastClickedTransition = getDrawer().transitionAtPoint(
					event.getPoint());


		// Should we show a popup menu?
		if (event.isPopupTrigger())
			showPopup(event);

		if (lastClickedState != null) {
			initialPointState.setLocation(lastClickedState.getPoint());
			if(!lastClickedState.isSelected()){
				Rectangle bounds = new Rectangle(0, 0, -1, -1);
				getView().getDrawer().getDiagram().selectStatesWithinBounds(bounds);
				getView().getDrawer().setSelectionBounds(bounds);
				lastClickedState.setSelect(true);
			}
			getView().repaint();
		}
		else if (lastClickedTransition != null) {
			initialPointClick.setLocation(event.getPoint());
		}	
		else {

			Rectangle bounds = new Rectangle(0, 0, -1, -1);
			getView().getDrawer().getDiagram().selectStatesWithinBounds(bounds);
			getView().getDrawer().setSelectionBounds(bounds);
		}

        //reset the selectedTransition after an Undo has happened.

        
        TransitionObject[] trans = getDiagram().getTransitions();
        for (int i = 0; i < trans.length; i++)
            if (trans[i].isSelected){
                selectedTransition = trans[i];
                return;
            }
        

        selectedTransition = null;




	}

	/**
	 * Returns if the state popup menu should be shown whenever applicable.
	 * 
	 * @return <CODE>true</CODE> if the state menu should be popped up, <CODE>false</CODE>
	 *         if it should not be... returns <CODE>true</CODE> by default
	 */
	protected boolean shouldShowStatePopup() {
		return true;
	}

	/**
	 * On a mouse drag, possibly move a state if the first press was on a state.
	 */
	public void mouseDragged(MouseEvent event) {



		if (lastClickedState != null) {
			if (event.isPopupTrigger())
				return;
			Point p = event.getPoint();
						
			//last clicked position
			int selectedX = (int)lastClickedState.getPoint().getX();
			int selectedY = (int)lastClickedState.getPoint().getY();
			
			StateObject[] states = getView().getDrawer().getDiagram().getStates();
			for(int k = 0; k < states.length; k++){
				System.out.println("state " +states[k].getName()+" "+states[k].isSelected());
				StateObject curState = states[k];
		
				if(curState.isSelected()){
					//diference between locations of last clicked state and current states
					int dx = (int)(selectedX-curState.getPoint().getX());
					int dy = (int)(selectedY-curState.getPoint().getY());
					//final location
					int x = p.x-dx;
					int y = p.y-dy;
					curState.getPoint().setLocation(x, y);
				}
			}
			initialPointClick = p;
			getView().repaint();
		} else if (lastClickedTransition != null) {
			if (event.isPopupTrigger())
				return;
			Point p = event.getPoint();
			int x = p.x - initialPointClick.x;
			int y = p.y - initialPointClick.y;
			StateObject f = lastClickedTransition.getFromState(), t = lastClickedTransition
					.getToState();
			if (f != t) {

				TransitionObject[] trans = getDiagram().getTransitionsFromStateToState(f, t);
				for (int n = 0; n < trans.length; n++) {
					CurvedArrow arrow = (CurvedArrow) getView().getDrawer().transitionToArrowMap.get(trans[n]);

                    

					
					getView().getDrawer().arrowToTransitionMap.put(arrow, trans[n]);
					getView().getDrawer().transitionToArrowMap.put(trans[n], arrow);
				}
			}
			initialPointClick.setLocation(p);
			getView().repaint();
		}
		else{
			Rectangle bounds;
			int nowX = event.getPoint().x;
			int nowY = event.getPoint().y;
			int leftX = initialPointClick.x;
			int topY = initialPointClick.y;
			if(nowX < initialPointClick.x) leftX = nowX;
			if(nowY < initialPointClick.y) topY = nowY;
			bounds = new Rectangle(leftX, topY, Math.abs(nowX-initialPointClick.x), Math.abs(nowY-initialPointClick.y));

            if (!transitionInFlux){
                getView().getDrawer().getDiagram().selectStatesWithinBounds(bounds);
                getView().getDrawer().setSelectionBounds(bounds);
            }

			getView().repaint();
		}
        
        //Deal with transition dragging here
        if (selectedTransition != null){ //simply set ...but we need to get the initial point to be clever
            CurvedArrow ca = (CurvedArrow)getView().getDrawer().transitionToArrowMap.get(selectedTransition);

            Point myClickP = event.getPoint();
            Point2D control = ca.getCurve().getCtrlPt();

            if (transitionInFlux || Math.sqrt((control.getX() - myClickP.x)*(control.getX() - myClickP.x) 
                        + (control.getY() - myClickP.y)*(control.getY() - myClickP.y)) < 15){
                            selectedTransition.setControl(myClickP);
        //                System.out.println("Move it damn it");
                             ca.refreshCurve();
                             transitionInFlux = true;
                             return;
                        }

        }
	}

    private boolean transitionInFlux = false;

	/**
	 * On a mouse release, sets the tool to the "virgin" state.
	 */
	public void mouseReleased(MouseEvent event) {
        transitionInFlux = false;
		if (event.isPopupTrigger())
			showPopup(event);
		
		
		StateObject[] states = getView().getDrawer().getDiagram().getStates();
		int count = 0;
		for(int k = 0; k < states.length; k++){			
			if(states[k].isSelected()){	
				count++;
			}
		}
		Rectangle bounds = getView().getDrawer().getSelectionBounds();
		if(count == 1 && bounds.isEmpty() && lastClickedState!=null) lastClickedState.setSelect(false);
		bounds = new Rectangle(0, 0, -1, -1);
		getView().getDrawer().setSelectionBounds(bounds);
		lastClickedState = null;
		lastClickedTransition = null;
		getView().repaint();
	}

	/**
	 * Returns the key stroke that will activate this tool.
	 * 
	 * @return the key stroke that will activate this tool
	 */
	public KeyStroke getKey() {
		return KeyStroke.getKeyStroke('a');
	}

	/**
	 * Returns true if only changing the final stateness of a state should be
	 * allowed in the state menu.
	 */
	public boolean shouldAllowOnlyFinalStateChange() {
		return false;
	}

	/**
	 * The contextual menu class for editing states.
	 */
    /*
     * I changed this from private class to protected class so I can 
     * remove the "Final State" option from Moore and Mealy machines.
     */
	protected class StateMenu extends JPopupMenu implements ActionListener {
		public StateMenu() {

			changeLabel = new JMenuItem("Change Label");
			deleteLabel = new JMenuItem("Clear Label");
			deleteAllLabels = new JMenuItem("Clear All Labels");
			editBlock = new JMenuItem("Edit Block");
			copyBlock = new JMenuItem("Duplicate Block");
			replaceSymbol = new JMenuItem("Replace Symbol");
			setName = new JMenuItem("Set Name");
			if (shouldAllowOnlyFinalStateChange())
				return;

			changeLabel.addActionListener(this);
			deleteLabel.addActionListener(this);
			deleteAllLabels.addActionListener(this);
			editBlock.addActionListener(this);
			setName.addActionListener(this);
			copyBlock.addActionListener(this);
			replaceSymbol.addActionListener(this);
			//this.add(makeInitial);
			this.add(changeLabel);
			this.add(deleteLabel);
			this.add(deleteAllLabels);
			this.add(setName);
		}

		public void show(StateObject state, Component comp, Point at) {
			this.remove(editBlock);
			this.state = state;

			deleteLabel.setEnabled(state.getLabel() != null);
			show(comp, at.x, at.y);
		}

		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
         
			if (item == changeLabel) {
				String oldlabel = state.getLabel();
				oldlabel = oldlabel == null ? "" : oldlabel;
				String label = (String) JOptionPane.showInputDialog(this,
						"Input a new label, or \n"
								+ "set blank to remove the label", "New Label",
						JOptionPane.QUESTION_MESSAGE, null, null, oldlabel);
				if (label == null)
					return;
				if (label.equals(""))
					label = null;
				state.setLabel(label);
			} else if (item == deleteLabel) {
				state.setLabel(null);
			} else if (item == deleteAllLabels) {
				StateObject[] states = getDiagram().getStates();
				for (int i = 0; i < states.length; i++)
					states[i].setLabel(null);

			} else if (item == setName) {
				String oldName = state.getName();
				oldName = oldName == null ? "" : oldName;
				String name = (String) JOptionPane.showInputDialog(this,
						"Input a new name, or \n"
								+ "set blank to remove the name", "New Name",
						JOptionPane.QUESTION_MESSAGE, null, null, oldName);
				if (name == null)
					return;
				if (name.equals(""))
					name = null;
				state.setName(name);
            }
    
			getView().repaint();
		}
		

        private StateObject state;



		private JMenuItem changeLabel, deleteLabel, deleteAllLabels, editBlock, copyBlock, replaceSymbol,
				setName;
	}


	/**
	 * The contextual menu class for context clicks in blank space.
	 */
	private class EmptyMenu extends JPopupMenu implements ActionListener {
		public EmptyMenu() {
			stateLabels = new JCheckBoxMenuItem("Display State Labels");
			stateLabels.addActionListener(this);
			this.add(stateLabels);

//           BEGIN SJK add
            adaptView = new JCheckBoxMenuItem("Auto-Zoom");

            adaptView.addActionListener(this);
            this.add(adaptView);
//          END SJK add

            
		}

		public void show(Component comp, Point at) {
			stateLabels.setSelected(getDrawer().doesDrawStateLabels());
			adaptView.setSelected(getView().getAdapt());
			myPoint = at;
			show(comp, at.x, at.y);
		}

		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem) e.getSource();
			if (item == stateLabels) {
				getView().getDrawer().shouldDrawStateLabels(item.isSelected());

			} else if (item == adaptView)
            {
                getView().setAdapt(item.isSelected());
            }
			getView().repaint();
			//boolean selected = adaptView.isSelected();
			emptyMenu = new EmptyMenu();
			//adaptView.setSelected(selected);
		}
		private Point myPoint;
		
		private JCheckBoxMenuItem stateLabels;
		private JMenuItem adaptView;
	}

	/** The transition creator for editing transitions. */
	private TransitionCreator creator;

	/** The state that was last clicked. */
	private StateObject lastClickedState = null;

	/** The transition that was last clicked. */
	private TransitionObject lastClickedTransition = null;
	
	/** The note that was last clicked. */
	//private Note lastClickedNote = null;

	/** The initial point of the state. */
	private Point initialPointState = new Point();

	/** The initial point of the click. */
	private Point initialPointClick = new Point();

	/** The state menu. */
    /*
     * I changed it to protected because I needed to mess with
     * it in a subclass. This is to remove the "Final State"
     * option in Moore and Mealy machines.
     */
	protected StateMenu stateMenu = new StateMenu();

	/** The empty menu. */
	private EmptyMenu emptyMenu = new EmptyMenu();

    private TransitionObject selectedTransition = null;
}
