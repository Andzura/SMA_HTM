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
 * Interface utilisée pour :
 * - Définir les propriétés d'un noeud (état, position)
 * - Se déplacer dans le graphe
 */
public interface NodeInterface {
    
    public static enum State {ACTIVATED, DESACTIVATED}; // on peut définir de nouveaux états, l'affichage associé est à définir dans MyGraphStreamNode (si GraphStream est utilisé en couche basse)
    public void setState(State s);
    public boolean isConnectedTo(NodeInterface dest);
    
    public void setPosition(int x, int y);
    
    public void setAbstractNetworkNode(AbstractNetworkNode _AbstractNetworkNode);
    public AbstractNetworkNode getAbstractNetworkNode();
    
    public EdgeInterface[] getEdgeIn();
    public EdgeInterface[] getEdgeOut();
    
}
