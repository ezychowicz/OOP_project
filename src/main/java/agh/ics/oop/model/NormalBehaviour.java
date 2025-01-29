package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;

public class NormalBehaviour implements AnimalBehaviour {
    private final Config config = Config.getInstance();
    private final int GENOME_LENGTH = config.getInt("GENOME_LENGTH");

    @Override
    public int nextGeneIdx(Animal animal) {
        return (animal.getGenomeIdx() + 1)%GENOME_LENGTH;
    }
}
