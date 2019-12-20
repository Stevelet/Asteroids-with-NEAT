package original;

import added.Random;

public class ConnectionGene {
    Node fromNode;
    Node toNode;
    double weight;
    boolean enabled = true;
    int innovationNo;//each connection is given a innovation number to compare genomes
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    ConnectionGene(Node from, Node to, double w, int inno) {
        fromNode = from;
        toNode = to;
        weight = w;
        innovationNo = inno;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //changes the weight
    void mutateWeight() {
        double rand2 = Random.random(1);
        if (rand2 < 0.1) {//10% of the time completely change the weight
            weight = Random.random(-1, 1);
        } else {//otherwise slightly change it
            weight += Random.randomGaussian()/50;
            //keep weight between bounds
            if(weight > 1){
                weight = 1;
            }
            if(weight < -1){
                weight = -1;

            }
        }
    }

    //----------------------------------------------------------------------------------------------------------
    //returns a copy of this original.ConnectionGene
    ConnectionGene clone(Node from, Node  to) {
        ConnectionGene clone = new ConnectionGene(from, to, weight, innovationNo);
        clone.enabled = enabled;

        return clone;
    }
}
