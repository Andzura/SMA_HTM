/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

/**
 *
 * @author farmetta
 * 
 * Classe de base utilisée pour les noeuds du réseau. Utiliser l'attribut node pour se déplacer dans le graphe.
 * 
 */
public class AbstractNetworkNode {
    private NodeInterface node;
    
    public AbstractNetworkNode(NodeInterface _node) {
        node = _node;
    }
    
    public NodeInterface getNode() {
        return node;
    }
}
