package agh.ics.oop.model;

import agh.ics.oop.model.util.MathUtils;

import java.util.Random;

import static agh.ics.oop.WorldGUI.GENOME_LENGTH;

public class CrazyBehaviour extends NormalBehaviour {
    @Override
    public int nextGeneIdx(Animal animal) {
        String behaviour = MathUtils.nonClassicalProbabilityRandom("boring", 0.8,"crazy",0.2);
        if (behaviour.equals("crazy")){
            Random random = new Random();
            return drawNextGene(animal.getGenomeIdx());
        }
        return super.nextGeneIdx(animal);
    }

    public int drawNextGene(int currIdx){
        int idx = currIdx;
        Random random = new Random();
        while (idx == currIdx){ //losuj do skutku. zlozonosc i tak lepsza niz robienie tego niederministycznie imo
            idx = random.nextInt(GENOME_LENGTH);
        }
        return idx;
    }
}
