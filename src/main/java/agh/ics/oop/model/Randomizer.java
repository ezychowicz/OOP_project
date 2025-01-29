package agh.ics.oop.model;

import java.util.List;

public interface Randomizer {
    /**
     * @param bound as in nextInt of Random
    * Custom function that should work like nextInt from Random (but can be different).
     */
    int nextInt(int bound);

    /**
     * @param list as in shuffle from Collections
     * Custom shuffling algorithm.
     */
    void shuffle(List<Integer> list);

}
