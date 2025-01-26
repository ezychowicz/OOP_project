package agh.ics.oop.model;

import java.util.List;

public interface Randomizer {
    int nextInt(int bound);
    void shuffle(List<Integer> list);

}
