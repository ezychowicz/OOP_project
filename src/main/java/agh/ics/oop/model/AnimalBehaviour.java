package agh.ics.oop.model;

public interface AnimalBehaviour {
    /**
    Return index of a genome, that will be used next.
     */
    int nextGeneIdx(Animal animal);
}
