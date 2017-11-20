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
 * * Interface utilisée pour :
 * - Définir les propriétés d'une arrête (état, position)
 * - Se déplacer dans le graphe
 */
public interface EdgeInterface {
    public static enum State {ACTIVATED, DESACTIVATED}; // on peut définir de nouveaux états, l'affichage associé est à définir dans MyGraphStreamEdge (si GraphStream est utilisé en couche basse)
    
    public void setState(State s);
    public void setAbstractNetworkEdge(AbstractNetworkEdge _abstractNetworkEdge);
    public AbstractNetworkEdge getAbstractNetworkEdge();
    
    public NodeInterface getNodeIn();
    public NodeInterface getNodeOut();
    
}
