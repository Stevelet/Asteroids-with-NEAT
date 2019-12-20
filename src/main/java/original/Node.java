package original;

import added.Vector;

import java.util.ArrayList;

public class Node {
    int number;
    double inputSum = 0;//current sum i.e. before activation
    double outputValue = 0; //after activation function is applied
    ArrayList<ConnectionGene> outputConnections = new ArrayList<ConnectionGene>();
    int layer = 0;
    Vector drawPos = new Vector();

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    Node(int no) {
        number = no;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //the node sends its output to the inputs of the nodes its connected to
    void engage() {
        if (layer != 0) { //no sigmoid for the inputs and bias
            outputValue = sigmoid(inputSum);
        }

        for (ConnectionGene outputConnection : outputConnections) {//for each connection
            if (outputConnection.enabled) {//dont do shit if not enabled
                outputConnection.toNode.inputSum += outputConnection.weight * outputValue;//add the weighted output to the sum of the inputs of whatever node this node is connected to
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
//sigmoid activation function
    double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -4.9 * x));
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------
    //returns whether this node connected to the parameter node
    //used when adding a new connection
    boolean isConnectedTo(Node node) {
        if (node.layer == layer) {//nodes in the same layer cannot be connected
            return false;
        }

        //you get it
        if (node.layer < layer) {
            for (int i = 0; i < node.outputConnections.size(); i++) {
                if (node.outputConnections.get(i).toNode == this) {
                    return true;
                }
            }
        } else {
            for (ConnectionGene outputConnection : outputConnections) {
                if (outputConnection.toNode == node) {
                    return true;
                }
            }
        }

        return false;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //returns a copy of this node
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Node clone() {
        Node clone = new Node(number);
        clone.layer = layer;
        return clone;
    }
}
