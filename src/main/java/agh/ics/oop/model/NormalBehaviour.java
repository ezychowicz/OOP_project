package agh.ics.oop.model;

import static agh.ics.oop.WorldGUI.GENOME_LENGTH;

public class NormalBehaviour implements AnimalBehaviour {
    @Override
    public int nextGeneIdx(Animal animal) {
        return (animal.getGenomeIdx() + 1)%GENOME_LENGTH;
    }
}
