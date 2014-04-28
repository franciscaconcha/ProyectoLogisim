package com.cburch.logisim.gui.main;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.std.gates.GateAttributes;
import com.cburch.logisim.std.wiring.Pin;
import com.cburch.logisim.tools.SetAttributeAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ActionItemError implements ActionListener {

    private ArrayList<Component> componentes;
    private int value;
    private Project project;

    public ActionItemError(ArrayList<Component> componentes, int value, Project project) {
        this.componentes = componentes;
        this.value = value;
        this.project = project;
    }

    public ActionItemError(Project proj) {
        this.project = proj;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Selection selection = project.getFrame().getCanvas().getSelection();
        SetAttributeAction act = new SetAttributeAction(project.getCurrentCircuit(),
                Strings.getter("selectionAttributeAction"));

        for (Component comp : project.getCurrentCircuit().getNonWires()) {
            if (!(comp.getFactory() instanceof Pin)) {
                act.set(comp, GateAttributes.ATTR_INPUTS, 15);
            }
            act.set(comp, StdAttr.WIDTH, BitWidth.create(12));
        }

        project.doAction(act);
//        CircuitMutator c =  new CircuitMutatorImpl();
//        c.add(project.getCurrentCircuit(), (Component)new Pin());
    }

}
