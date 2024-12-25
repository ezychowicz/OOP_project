package agh.ics.oop.model;

import static agh.ics.oop.WorldGUI.ANIMALS_AMOUNT;
import static agh.ics.oop.WorldGUI.GENOME_LENGTH;
import static agh.ics.oop.WorldGUI.INITIAL_ANIMAL_ENERGY;

public class AnimalGenerator{
    private final int genomeLength;
    private final int initialEnergy;
    private final int animalsAmount;
    public AnimalGenerator(){
        this.genomeLength = GENOME_LENGTH;
        this.initialEnergy = INITIAL_ANIMAL_ENERGY;
        this.animalsAmount = ANIMALS_AMOUNT;
    }

    public Animal generateInitialAnimals(int animalsAmount){ // work in progress
        return null;
    }
}
