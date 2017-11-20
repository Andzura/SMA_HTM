/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.graphstream;

import graph.AbstractNetworkEdge;
import graph.EdgeInterface;
import graph.NodeInterface;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractNode;

/**
 *
 * @author farmetta
 * 
 * Définit le comportement des Arrêtes, si GraphStream est utilisé en couche basse
 * 
 */
public class MyGraphStreamEdge extends AbstractEdge implements EdgeInterface {
    
    
    private AbstractNetworkEdge abstractNetworkEdge;
    
    @Override
    public void setAbstractNetworkEdge(AbstractNetworkEdge _abstractNetworkEdge) {
        abstractNetworkEdge = _abstractNetworkEdge;
    }
    
    @Override
    public AbstractNetworkEdge getAbstractNetworkEdge() {
        return abstractNetworkEdge;
    }
    
    public MyGraphStreamEdge(String id, Node source, Node target,
			boolean directed) {
        super (id, (AbstractNode) source, (AbstractNode) target, directed);
        
        
    }
    
    @Override
    public void setState(State s) {
            switch (s) {
                case ACTIVATED : addAttribute("ui.style", "fill-color: red;");
                    break;
                case DESACTIVATED : addAttribute("ui.style", "fill-color: black;");     
            }
    }

  
    @Override
    public NodeInterface getNodeIn() {
        
        return (NodeInterface) getSourceNode();
        
    }
    
    @Override
    public NodeInterface getNodeOut() {
        return (NodeInterface) getTargetNode();
        
    }
   
}
