/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import graph.AbstractNetworkNode;
import graph.NodeInterface;

/**
 *
 * @author farmetta
 */
public class MyNeuron  extends AbstractNetworkNode {
    
    /**
     * TODO : dans la constrution de démonstration, les instances de MyNeuron représentent les entrées.
     * Il faut ainsi définir une fonction d'encodage (entrée1 -> 0000011111000000, entrée2 -> 0001110001111100, etc.) permetant
     * de définir l'état de chacune des entrées suivant le signal courant perçu par le système
    */
    private boolean activated;

    public MyNeuron(NodeInterface _node) {
        super(_node);
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
