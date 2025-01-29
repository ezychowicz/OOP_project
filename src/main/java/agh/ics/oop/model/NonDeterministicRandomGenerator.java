package agh.ics.oop.model;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NonDeterministicRandomGenerator implements Randomizer {
    private final Random random = new Random();

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    @Override
    public void shuffle(List<Integer> list) {
        Collections.shuffle(list);
    }
}
