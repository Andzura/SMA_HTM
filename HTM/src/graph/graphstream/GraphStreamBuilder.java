/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.graphstream;

import graph.EdgeBuilder;
import graph.EdgeInterface;
import graph.NodeBuilder;
import graph.NodeInterface;
import org.graphstream.graph.Graph;

/**
 *
 * @author farmetta
 */
public class GraphStreamBuilder implements NodeBuilder, EdgeBuilder {
    public final Graph graph;
   
    private int nodeId = 0;
    private int edgeId = 0;
    
    public GraphStreamBuilder(Graph _graph) {
        graph = _graph;
    }
    
    public NodeInterface getNewNode() {
        graph.addNode(Integer.toString(nodeId));
        return graph.getNode(nodeId ++); 
    }
    
    public EdgeInterface getNewEdge(NodeInterface src, NodeInterface dest) {
        
        //graph.get
        
        return graph.addEdge(Integer.toString(edgeId ++), (org.graphstream.graph.Node) src, (org.graphstream.graph.Node) dest, true);
    }
    
}
