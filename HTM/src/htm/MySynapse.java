/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import graph.AbstractNetworkEdge;
import graph.EdgeInterface;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author farmetta
 */
public class MySynapse extends AbstractNetworkEdge {

    public static final double THRESHOLD = 0.5;
    private double currentValue = THRESHOLD;
    private double inertia = 0.1;
    private double lastVariation = 0;
    private boolean activated;
    private double edgeLength = 0;

    public void setEdgeLength(double edgeLength) {
        this.edgeLength = edgeLength;
    }

    public double getEdgeLength() {
        return edgeLength;
    }

    protected MySynapse(EdgeInterface _edge) {
        super(_edge);
        currentValueUdpate(ThreadLocalRandom.current().nextDouble(-0.25,0.25));
    }
    
    public void currentValueUdpate(double delta) {
        double variation = inertia*lastVariation + delta;
        lastVariation = variation;
        currentValue += variation;
        inertia *= 1-Math.pow(10,-5);
        
        if (currentValue > 1) {
            currentValue = 1;
        }
        if (currentValue < 0) {
            currentValue = 0;
        }
        
        if (currentValue >= THRESHOLD) {
            getEdge().setState(EdgeInterface.State.ACTIVATED);
            this.setActivated(true);
        } else {
            getEdge().setState(EdgeInterface.State.DESACTIVATED);
            this.setActivated(false);
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void applyBias(double i, int j) {
        double bias = Math.abs(i-j);
        bias = bias <= MyNetwork.NEIGHBORHOODRADIUS ? 1-(bias/MyNetwork.NEIGHBORHOODRADIUS) : 0;
        currentValueUdpate(bias * MyNetwork.CENTERBOOST * THRESHOLD);
    }
}
