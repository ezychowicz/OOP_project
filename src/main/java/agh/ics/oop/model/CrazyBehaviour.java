package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.util.MathUtils;

import java.util.Random;



public class CrazyBehaviour extends NormalBehaviour {
    private final Randomizer randomizer;
    private final Config config = Config.getInstance();
    private final int GENOME_LENGTH = config.getInt("GENOME_LENGTH");
    private final double boringProbability;
    public CrazyBehaviour() {
        super();
        this.randomizer = new NonDeterministicRandomGenerator();
        this.boringProbability = 0.8;
    }

    public CrazyBehaviour(Randomizer randomizer, double boringProbability) {
        super();
        this.randomizer = randomizer;
        this.boringProbability = boringProbability;
    }
    @Override
    public int nextGeneIdx(Animal animal) {
        String behaviour = MathUtils.nonClassicalProbabilityRandom("boring", boringProbability,"crazy",1 - boringProbability);
        if (behaviour.equals("crazy")){
            return drawNextGene(animal.getGenomeIdx());
        }
        return super.nextGeneIdx(animal);
    }

    public int drawNextGene(int currIdx){
        int idx = currIdx;
        while (idx == currIdx){ //losuj do skutku. zlozonosc i tak lepsza niz robienie tego niederministycznie imo
            idx = randomizer.nextInt(GENOME_LENGTH);
        }
        return idx;
    }
}
