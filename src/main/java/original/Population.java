package original;

import java.util.ArrayList;

public class Population {
    ArrayList<Player> pop = new ArrayList<Player>();
    Player bestPlayer;//the best ever player
    int bestScore =0;//the score of the best ever player
    //int species = 0;
    int gen;
    //int nextConnectionNumber;
    ArrayList<ConnectionHistory> innovationHistory = new ArrayList<ConnectionHistory>();
    ArrayList<Player> genPlayers = new ArrayList<Player>();
    ArrayList<Species> species = new ArrayList<Species>();



    //------------------------------------------------------------------------------------------------------------------------------------------
    //constructor
    Population(int size) {

        for (int i =0; i<size; i++) {
            pop.add(new Player());
            pop.get(i).brain.generateNetwork();
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //update all the players which are alive
    void updateAlive() {

        for (int i = 0; i< pop.size(); i++) {
            if (!pop.get(i).dead) {
                pop.get(i).look();//get inputs for brain
                pop.get(i).think();//use outputs from neural network
                pop.get(i).update();//move the player according to the outputs from the neural network
                if (!AsteroidGameNeat.showBest || i ==0) {//dont show dead players
                    pop.get(i).show();
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //returns true if all the players are dead      sad
    boolean done() {
        for (Player player : pop) {
            if (!player.dead) {
                return false;
            }
        }
        return true;
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //sets the best player globally and for this gen
    void setBestPlayer() {
        Player tempBest =  species.get(0).players.get(0);

        genPlayers.add(tempBest);

        //if best this gen is better than the global best score then set the global best as the best this gen

        if (tempBest.score > bestScore) {
            System.out.println("old best:" + bestScore);
            System.out.println("new best:" + tempBest.score);
            bestScore = tempBest.score;
            bestPlayer = tempBest.cloneForReplay();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------
    //this function is called when all the players in the population are dead and a new generation needs to be made
    void naturalSelection() {
        speciate();//seperate the population into species
        calculateFitness();//calculate the fitness of each player
        sortSpecies();//sort the species to be ranked in fitness order, best first
        cullSpecies();//kill off the bottom half of each species
        setBestPlayer();//save the best player of this gen
        killStaleSpecies();//remove species which haven't improved in the last 15(ish) generations
        killBadSpecies();//kill species which are so bad that they cant reproduce


        double averageSum = getAvgFitnessSum();
        ArrayList<Player> children = new ArrayList<Player>();//the next generation
        for (Species s : species) {//for each species
            children.add(s.players.get(0).clone());//add champion without any mutation
            int NoOfChildren = (int) (s.averageFitness/averageSum * pop.size()) -1;//the number of children this species is allowed, note -1 is because the champ is already added
            for (int i = 0; i< NoOfChildren; i++) {//get the calculated amount of children from this species
                children.add(s.giveMeBaby(innovationHistory));
            }
        }

        while (children.size() < pop.size()) {//if not enough babies (due to flooring the number of children to get a whole int)
            children.add(species.get(0).giveMeBaby(innovationHistory));//get babies from the best species
        }

        pop.clear();
        //noinspection unchecked
        pop = (ArrayList<Player>) children.clone(); //set the children as the current population

        gen+=1;
        System.out.println("generation" + gen + "Number of mutations" + innovationHistory.size() + "species: " + species.size());
        for (Player player : pop) {//generate networks for each of the children
            player.brain.generateNetwork();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
//seperate population into species based on how similar they are to the leaders of each species in the previous gen
    void speciate() {
        for (Species s : species) {//empty species
            s.players.clear();
        }
        for (Player player : pop) {//for each player
            boolean speciesFound = false;
            for (Species s : species) {//for each species
                if (s.sameSpecies(player.brain)) {//if the player is similar enough to be considered in the same species
                    s.addToSpecies(player);//add it to the species
                    speciesFound = true;
                    break;
                }
            }
            if (!speciesFound) {//if no species was similar enough then add a new species with this as its champion
                species.add(new Species(player));
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //calculates the fitness of all of the players
    void calculateFitness() {

        for (int i =1; i<pop.size(); i++) {
            pop.get(i).calculateFitness();
        }

    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //sorts the players within a species and the species by their fitnesses
    void sortSpecies() {
        //sort the players within a species
        for (Species s : species) {
            s.sortSpecies();
        }

        //sort the species by the fitness of its best player
        //using selection sort like a loser
        ArrayList<Species> temp = new ArrayList<Species>();
        for (int i = 0; i < species.size(); i ++) {
            double max = 0;
            int maxIndex = 0;
            for (int j = 0; j< species.size(); j++) {
                if (species.get(j).bestFitness > max) {
                    max = species.get(j).bestFitness;
                    maxIndex = j;
                }
            }
            temp.add(species.get(maxIndex));
            species.remove(maxIndex);
            i--;
        }
        //noinspection unchecked
        species = (ArrayList<Species>) temp.clone();
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //kills all species which haven't improved in 15 generations
    void killStaleSpecies() {
        for (int i = 1; i< species.size(); i++) {
            if (species.get(i).staleness >= 15) {
                species.remove(i);
                i--;
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //if a species sucks so much that it wont even be allocated 1 child for the next generation then kill it now
    void killBadSpecies() {
        double averageSum = getAvgFitnessSum();

        for (int i = 1; i< species.size(); i++) {
            if (species.get(i).averageFitness/averageSum * pop.size() < 1) {//if wont be given a single child
                species.remove(i);//sad
                i--;
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------------------
    //returns the sum of each species average fitness
    double getAvgFitnessSum() {
        double averageSum = 0;
        for (Species s : species) {
            averageSum += s.averageFitness;
        }
        return averageSum;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //kill the bottom half of each species
    void cullSpecies() {
        for (Species s : species) {
            s.cull(); //kill bottom half
            s.fitnessSharing();//also while we're at it lets do fitness sharing
            s.setAverage();//reset averages because they will have changed
        }
    }
}