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

    private static final double LEARNINGRATE = 0.005;
    public static final double NEIGHBORHOODRADIUS = 5;
    private static final int INPUTMAX = 18;
    private static final double MINRATEACTIVITY = 0.01;
    public static final double CENTERBOOST = 0.1;
    private static final int MINOVERLAP = 1;
    private static final int desiredLocalActivity = 2;

    private NodeBuilder nb;
    private EdgeBuilder eb;
    
    ArrayList<MyNeuron> lstMN;
    ArrayList<MyColumn> lstMC;
    private static double minActivity = 0.01;
    
    
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

        //*
        double i = lstMN.size()/lstMC.size()/2;
        double columnIndex = 0;
        double neuronIndex = 0;
        for(MyColumn c : lstMC){
            int j = 0;
            for(MyNeuron n : lstMN){
                EdgeInterface e = eb.getNewEdge(n.getNode(), c.getNode());
                double coordNeuron = neuronIndex/lstMN.size();
                double coordColumn = columnIndex/lstMC.size();
                double startDistance = Math.abs(coordColumn-coordNeuron);
                MySynapse s = new MySynapse(e);
                s.setEdgeLength(startDistance);
                s.applyBias(i,j);
                e.setAbstractNetworkEdge(s);
                j++;
                neuronIndex +=1;
            }
            i += lstMN.size()/lstMC.size();
            columnIndex +=1;
            neuronIndex = 0;
        }
        //*/
        /*
         * Connection basée sur la distance
         */
        /*
        int i = 0;
        double columnIndex = 0;
        double neuronIndex = 0;
        for(MyColumn c : lstMC){
            int j = 0;
            for(MyNeuron n : lstMN){
                double coordNeuron = neuronIndex/lstMN.size()+ 0.5/lstMN.size();
                double coordColumn = columnIndex/lstMC.size()+ 0.5/lstMC.size();
                double startDistance = Math.min(Math.abs(coordColumn-coordNeuron), 1-Math.abs(coordColumn-coordNeuron));
                System.out.println(startDistance);
                if(startDistance <=  1/(NEIGHBORHOODRADIUS) ){
                    EdgeInterface e = eb.getNewEdge(n.getNode(), c.getNode());
                    MySynapse s = new MySynapse(e);
                    s.applyBias(i, j);
                    e.setAbstractNetworkEdge(s);
                }
                j++;
                neuronIndex +=1;
            }
            i += lstMN.size()/lstMC.size();
            neuronIndex =0;
            columnIndex +=1;
        }
        //*/
        
        
    }

    @Override
    public void run() {
        int[] countWin = new int[8];
        int[] inputs = IntStream.rangeClosed(0,INPUTMAX).toArray();
        int count = 0;
        int count2 = 0;
        boolean learning = true;
                while (true) {
            count2++;
            count = (count + 1)%inputs.length;
            int currentInput = new Random().nextInt(INPUTMAX);
            // processus de démontration qui permet de voyager dans le graphe et de faire varier les état des synaptes, entrées et colonnes
            encode(currentInput);

            List<MyColumn> winners = new ArrayList<>();
            int j = -1;
            System.out.println("------");
            for(MyColumn c : lstMC){
                boolean inCompetition = false;
                c.getNode().setState(NodeInterface.State.DESACTIVATED);
                c.setActivated(false);
                c.setCurrentOverlap(0);
                for (EdgeInterface e : c.getNode().getEdgeIn()) {
                    // Comment These following lines to ignore geographical proximity
                    //*
                    Double random = new Random().nextDouble();
                    if(learning && ((MySynapse) e.getAbstractNetworkEdge()).getEdgeLength() > random){
                        continue;
                    }
                    //*/
                    MyNeuron n = (MyNeuron) e.getNodeIn().getAbstractNetworkNode();
                    if(n.isActivated()){
                        inCompetition = true;
                    }
                    else{
                        continue;
                    }
                    if(!((MySynapse) e.getAbstractNetworkEdge()).isActivated()){
                        continue;
                    }
                    c.incrementCurrentOverlap();
                }
                j++;
                if(c.getCurrentOverlap() < MINOVERLAP){
                    countWin[j] = countWin[j] + 1;
                    c.setCurrentOverlap(0);
                    c.updateOverlapDutyCycle(false);
                }else{
                    c.updateOverlapDutyCycle(true);
                    c.setCurrentOverlap(c.getCurrentOverlap()*c.getBoost());
                }
                System.out.println("Overlap "+j+" : "+ ((double)countWin[j]/(double)count2));
                if(winners.isEmpty()){
                    winners.add(c);
                }else{
                    boolean added = false;
                    for(int i = 0; i < winners.size(); i++){
                        if(winners.get(i).getCurrentOverlap() < c.getCurrentOverlap()){
                            winners.add(i, c);
                            added = true;
                            break;
                        }
                    }
                    if(!added)
                        winners.add(c);
                }
            }
            System.out.println("------\n");

            for(int i = 0; i < desiredLocalActivity && winners.get(i).getCurrentOverlap() > 0; i++){
                MyColumn c = winners.get(i);
                c.getNode().setState(NodeInterface.State.ACTIVATED);
                c.setActivated(true);
                if(learning) {
                    for (EdgeInterface e : c.getNode().getEdgeIn()) {
                        MyNeuron n = (MyNeuron) e.getNodeIn().getAbstractNetworkNode();
                        if (n.isActivated()) {
                            ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(LEARNINGRATE);
                        } else {
                            ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(-LEARNINGRATE);
                        }
                    }
                }
            }

            if(learning) {
                double maxActivity = 0.0;
                double maxOverlap = 0.0;
                int i = 0;
                for (MyColumn c : lstMC) {
                    c.updateActivity();
                    if (c.getActivity() > maxActivity)
                        maxActivity = c.getActivity();
                    if(c.getOverlapDutyCycle() > maxOverlap)
                        maxOverlap = c.getOverlapDutyCycle();
                    i++;
                }
                double minActivityAllowed = maxActivity * MINRATEACTIVITY;
                double minOverlapAllowed = maxOverlap * MINRATEACTIVITY;
                for (MyColumn c : winners) {
                    if (c.getActivity() < minActivityAllowed) {
                        c.boostfunction();
                    } else {
                        c.resetBoost();
                    }
                    if (c.getOverlapDutyCycle() < minOverlapAllowed) {
                        for (EdgeInterface e : c.getNode().getEdgeIn()) {
                            ((MySynapse) e.getAbstractNetworkEdge()).currentValueUdpate(0.01 * MySynapse.THRESHOLD);
                        }
                    }
                }

            }

            try{
                if(count2 < 15000){
                    Thread.sleep(1);
                }else {
                    learning = false;
                    for(MyColumn c : lstMC){
                        c.resetBoost();
                    }
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
        double step = 1;
        double bitWidth = 2;
        for(int i = 0; i < lstMN.size(); i++) {
            if(currentInput >= i*step && currentInput <= i*step+bitWidth){
                lstMN.get(i).getNode().setState(NodeInterface.State.ACTIVATED);
                lstMN.get(i).setActivated(true);
            }else{
                lstMN.get(i).getNode().setState(NodeInterface.State.DESACTIVATED);
                lstMN.get(i).setActivated(false);
            }
        }
    }

}
