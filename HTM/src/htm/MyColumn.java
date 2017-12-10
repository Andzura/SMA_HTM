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


    private boolean activated = false;
    private double currentOverlap = 0;
    private double activity = 100.0;
    private double overlapDutyCycle = 100.0;
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

    //Nous faisons varier la valeur des synpses ici
    public void updateActivity() {
        if(this.isActivated()){
            activity *= 1.01;
        }else{
            activity *= 0.99;
        }
    }

    public void updateOverlapDutyCycle(boolean overMinOverlap){
        if(overMinOverlap){
            overlapDutyCycle *=1.01;
        }else{
            overlapDutyCycle *= 0.99;
        }
    }

    public double getBoost() {
        return boost;
    }

    public void resetBoost() {
        this.boost = 1;
    }

    public void boostfunction() {
        this.boost += 0.2;
    }

    public double getOverlapDutyCycle() {
        return overlapDutyCycle;
    }
}


