/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;
import graph.graphstream.GraphStreamBuilder;
import graph.graphstream.MyGraphStreamEdge;
import graph.graphstream.MyGraphStreamNode;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author farmetta
 */
public class HTM {

    
    /** TODO
     *  Terminer la construction du réseau
     *  Dessiner suivant le type (carré et rond)
     *  Alimenter avec des données
     *  Préparer prototypes fonctions
     *  Connecter un graphe
     */
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        Graph graph = new SingleGraph("graph"); // création du graphe
        graph.setNodeFactory(new NodeFactory<MyGraphStreamNode>() {
			public MyGraphStreamNode newInstance(String id, Graph graph) {
				return new MyGraphStreamNode((AbstractGraph) graph, id); // les noeuds seront de type MyGraphStreamNode
			}
		});
        
        graph.setEdgeFactory(new EdgeFactory<MyGraphStreamEdge>() {
            
            @Override
            public MyGraphStreamEdge newInstance(String id, Node src, Node dst, boolean directed) {
                return new MyGraphStreamEdge(id, src, dst, directed); // les arrêtes seront du type MyGraphStreamEdge
            }
            
			
		});
        
        GraphStreamBuilder gb = new GraphStreamBuilder(graph);
        MyNetwork mn = new MyNetwork(gb, gb);
        
        mn.buildNetwork(16, 7); // un réseau de démonstration avec 16 entrées et 5 colonnes
        graph.display(false);
        
        
        new Thread(mn).start(); // exécution d'un processus d'apprentissage, à définir, pour mn
        
        
        
        
    }
    
}
