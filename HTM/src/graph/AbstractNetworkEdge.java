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
 * Classe de base utilisée pour les arrêtes du réseau. Utiliser l'attribut edge pour se déplacer dans le graphe.
 * 
 */
public class AbstractNetworkEdge {
    private EdgeInterface edge;
    
    public AbstractNetworkEdge(EdgeInterface _edge) {
        edge = _edge;
    }
    
    public EdgeInterface getEdge() {
        return edge;
    }
    
    
    
}
