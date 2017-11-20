/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author farmetta
 */
public interface EdgeBuilder {
    public EdgeInterface getNewEdge(NodeInterface src, NodeInterface dest);
    
}
