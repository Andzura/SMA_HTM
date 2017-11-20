/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.graphstream;

import graph.AbstractNetworkNode;
import graph.EdgeInterface;
import graph.NodeInterface;
import java.util.Iterator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

/**
 *
 * @author farmetta
 * 
 * Définit le comportement des Noeux, si GraphStream est utilisé en couche basse
 * 
 */
public class MyGraphStreamNode extends SingleNode implements NodeInterface {
    
   private AbstractNetworkNode abstractNetworkNode;
    
   
   
    @Override
    public void setAbstractNetworkNode(AbstractNetworkNode _abstractNetworkNode) {
        abstractNetworkNode = _abstractNetworkNode;
    }
    
    @Override
    public AbstractNetworkNode getAbstractNetworkNode() {
        return abstractNetworkNode;
    }
    
    public MyGraphStreamNode(AbstractGraph g, String s) {
        super(g, s);
    }

   
    public boolean isConnectedTo(NodeInterface dest) {
        return hasEdgeBetween((Node) dest);
    }

    
    public void setPosition(int x, int y) {
        setAttribute("xyz", x, y, 0);
    }

    
    @Override
    public <T extends Edge> Iterable<T> getEachEnteringEdge() {
        return super.getEachEnteringEdge(); //To change body of generated methods, choose Tools | Templates.
    } 
   
    
    public EdgeInterface[] getEdgeIn() {
        
        Iterable<AbstractEdge> iterable = getEachEnteringEdge();
        Iterator<AbstractEdge> iterator = iterable.iterator();
        
        
        EdgeInterface[] tab = new EdgeInterface[getInDegree()];
        
        for (int i =0; i < getInDegree(); i++) {
            tab[i] = (EdgeInterface)iterator.next();
        }
        return tab;
    }
    
    public EdgeInterface[] getEdgeOut() {
        
        Iterable<AbstractEdge> iterable = getEachLeavingEdge();
        Iterator<AbstractEdge> iterator = iterable.iterator();
        
        
        EdgeInterface[] tab = new EdgeInterface[getOutDegree()];
        
        for (int i =0; i < getOutDegree(); i++) {
            tab[i] = (EdgeInterface)iterator.next();
        }
        return tab;
    }
   
    @Override
    public void setState(NodeInterface.State s) {
            switch (s) {
                case ACTIVATED : addAttribute("ui.style", "fill-color: red;");
                    break;
                case DESACTIVATED : addAttribute("ui.style", "fill-color: black;");     
            }
    }
    
    
}
