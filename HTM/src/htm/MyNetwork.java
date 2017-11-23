/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import graph.EdgeBuilder;
import graph.EdgeInterface;
import graph.NodeBuilder;
import graph.NodeInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;


/**
 *
 * @author farmetta
 */
public class MyNetwork implements Runnable {

    private static final int INPUTMAX = 18;
    private static final double MINRATEACTIVITY = 0.01;
    private final int MINOVERLAP = 1;
    private final int desiredLocalActivity = 2;
    private NodeBuilder nb;
    private EdgeBuilder eb;
    
    ArrayList<MyNeuron> lstMN;
    ArrayList<MyColumn> lstMC;
    private static double minActivity = 0.5;
    
    
    public MyNetwork(NodeBuilder _nb, EdgeBuilder _eb) {
        nb = _nb;
        eb = _eb;
    }
    
    
    private static final int DENSITE_INPUT_COLUMNS = 8;
    public void buildNetwork(int nbInputs, int nbColumns) {
        
        
        // création des entrées
        lstMN = new ArrayList<MyNeuron>();
        for (int i = 0; i < nbInputs; i++) {
            NodeInterface ni = nb.getNewNode();
            MyNeuron n = new MyNeuron(ni);
            n.getNode().setPosition(i, 0);
            ni.setAbstractNetworkNode(n);
            lstMN.add(n);
        }
        // création des colonnes
        lstMC = new ArrayList<MyColumn>();
        for (int i = 0; i < nbColumns; i++) {
            NodeInterface ni = nb.getNewNode();
            MyColumn c = new MyColumn(ni);
            c.getNode().setPosition(i*2, 2);
            ni.setAbstractNetworkNode(c);
            
            lstMC.add(c);
        }
        
        Random rnd = new Random();
        // Connection entre entrées et colonnes
        /*
         * Connections aléatoires
        for (int i = 0; i < DENSITE_INPUT_COLUMNS * lstMC.size(); i++) {
            
            MyNeuron n = lstMN.get(rnd.nextInt(lstMN.size()));
            MyColumn c = lstMC.get(rnd.nextInt(lstMC.size()));
            
            if (!n.getNode().isConnectedTo(c.getNode())) {
                EdgeInterface e = eb.getNewEdge(n.getNode(), c.getNode());
                MySynapse s = new MySynapse(e);
                e.setAbstractNetworkEdge(s);
                
            } else {
                i--;
            }
        }
        */

        /*
         * Connections totale
         */

        for(MyColumn c : lstMC){
            for(MyNeuron n : lstMN){
                EdgeInterface e = eb.getNewEdge(n.getNode(), c.getNode());
                MySynapse s = new MySynapse(e);
                e.setAbstractNetworkEdge(s);

            }
        }
        
        
    }

    @Override
    public void run() {
        int[] inputs = IntStream.rangeClosed(1,INPUTMAX).toArray();
        int count = 0;
        int count2 = 0;
        while (true) {
            count2++;
            int currentInput = inputs[count];
            count = (count + 1)%inputs.length;

            // processus de démontration qui permet de voyager dans le graphe et de faire varier les état des synaptes, entrées et colonnes
            encode(currentInput);

            List<MyColumn> winners = new ArrayList<>();
            for(MyColumn c : lstMC){
                c.getNode().setState(NodeInterface.State.DESACTIVATED);
                c.setActivated(false);
                c.setCurrentOverlap(0);
                for (EdgeInterface e : c.getNode().getEdgeIn()) {
                    if(!((MySynapse) e.getAbstractNetworkEdge()).isActivated()){
                        continue;
                    }
                    MyNeuron n = (MyNeuron) e.getNodeIn().getAbstractNetworkNode();
                    if(n.isActivated()){
                        c.incrementCurrentOverlap();
                    }
                }
                if(c.getCurrentOverlap() < MINOVERLAP){
                    c.setCurrentOverlap(0);
                    c.updateOverlapDutyCycle(false);
                }else{
                    c.updateOverlapDutyCycle(true);
                    c.setCurrentOverlap(c.getCurrentOverlap()*c.getBoost());
                }
                if(winners.isEmpty()){
                    winners.add(c);
                }else{
                    for(int i = 0; i < winners.size(); i++){
                        if(winners.get(i).getCurrentOverlap() < c.getCurrentOverlap()){
                            winners.add(i, c);
                            break;
                        }
                    }
                    winners.add(c);
                }
            }

            for(int i = 0; i < desiredLocalActivity; i++){
                MyColumn c = winners.get(i);
                c.getNode().setState(NodeInterface.State.ACTIVATED);
                c.setActivated(true);
                for (EdgeInterface e : c.getNode().getEdgeIn()) {
                    MyNeuron n = (MyNeuron) e.getNodeIn().getAbstractNetworkNode();
                    if(n.isActivated()){
                        ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(0.002);
                    }else{
                        ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(-0.01);
                    }
                }
            }

            double maxActivity = 0.0;
            for(MyColumn c : lstMC){
                c.updateActivity();
                if(c.getActivity() > maxActivity)
                    maxActivity = c.getActivity();
            }

            double minActivityAllowed = maxActivity * MINRATEACTIVITY;
            for(MyColumn c : lstMC){
                if(c.getActivity()<minActivityAllowed){
                    c.boostfunction();
                }else{
                    c.resetBoost();
                }
                if(c.getOverlapDutyCycle() < minActivityAllowed){
                    for (EdgeInterface e : c.getNode().getEdgeIn()) {
                        ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(0.1*MySynapse.THRESHOLD);
                    }
                }
            }

            try{
                if(count2 < 50000){
                    // No sleep for faster "learning" phase
                }else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
            for (MyColumn c : lstMC) {
                
                if (new Random().nextBoolean()) {
                    c.getNode().setState(NodeInterface.State.ACTIVATED);
                } else {
                    c.getNode().setState(NodeInterface.State.DESACTIVATED);
                }
                
                for (EdgeInterface e : c.getNode().getEdgeIn()) {
                    
                    ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(new Random().nextDouble() - 0.5);
                    
                    
                    
                    MyNeuron n = (MyNeuron) e.getNodeIn().getAbstractNetworkNode(); // récupère le neurone d'entrée
                    if (new Random().nextBoolean()) {
                        n.getNode().setState(NodeInterface.State.ACTIVATED);
                    } else {
                        n.getNode().setState(NodeInterface.State.DESACTIVATED);
                    }
                
                    
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MyNetwork.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
             */
            
        }
    }

    private void encode(int currentInput){

        /*
        double marge = 0.5;
        double temp = (currentInput+1);
        for(int i = 1; i <= lstMN.size(); i++){
            if(i <= temp/2 + marge && i >= temp/2 - marge){
                lstMN.get(i-1).getNode().setState(NodeInterface.State.ACTIVATED);
                lstMN.get(i-1).setActivated(true);
            }else{
                lstMN.get(i-1).getNode().setState(NodeInterface.State.DESACTIVATED);
                lstMN.get(i-1).setActivated(false);
            }
        }
        */
        int bitWide = 2;
        for(int i = 0; i < lstMN.size(); i++) {
            if(currentInput >= i && currentInput <= i+bitWide){
                lstMN.get(i).getNode().setState(NodeInterface.State.ACTIVATED);
                lstMN.get(i).setActivated(true);
            }else{
                lstMN.get(i).getNode().setState(NodeInterface.State.DESACTIVATED);
                lstMN.get(i).setActivated(false);
            }
        }
    }

}
