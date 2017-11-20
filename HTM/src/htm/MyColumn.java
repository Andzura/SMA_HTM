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
public class MyColumn extends AbstractNetworkNode {

    /**
     * TODO : Au cours de l'apprentissage, chaque colonne doit atteindre un taux d'activation. 
     * Une colonnne est activée si elle reçoit suffisament de retours positif de ses synapses 
     * (le retour est positif si la synapse est active et que son entrée associée l'est également).
     * 
     * Pour l'apprentissage, parcourir les synapses en entrée, et faire évoluer les poids synaptiques adéquatement.
     * 
     */
    private boolean activated = false;
    private double currentOverlap = 0;
    private double activity = 1.0;
    private double boost = 1;



    public MyColumn(NodeInterface _node) {
        super(_node);
    }

    public double getCurrentOverlap() {
        return currentOverlap;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public void setCurrentOverlap(double currentOverlap) {
        this.currentOverlap = currentOverlap;
    }

    public void incrementCurrentOverlap(){
        this.currentOverlap++;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void updateActivity() {
        if(this.isActivated()){
            activity *= 1.01;
        }else{
            activity *= 0.99;
        }
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(double boost) {
        this.boost = boost;
    }
}


