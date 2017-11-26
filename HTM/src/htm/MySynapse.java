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
    private boolean activated;
    private double edgeLength;

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
        currentValue += delta;
        
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
}
