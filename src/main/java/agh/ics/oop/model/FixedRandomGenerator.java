package agh.ics.oop.model;
import java.util.Collections;
import java.util.List;

public class FixedRandomGenerator implements Randomizer {
    private final List<Integer> nextRandoms;
    private final List<List<Integer>> swapRecipe;
    private int randomIndex = 0;

    public FixedRandomGenerator(List<Integer> nextRandoms, List<List<Integer>> swapRecipe) {
        this.nextRandoms = nextRandoms;
        this.swapRecipe = swapRecipe;
    }

    @Override
    public int nextInt(int bound) {
        if (randomIndex >= nextRandoms.size()) {
            throw new IllegalStateException("No more predefined random numbers available.");
        }
        int nextRandom = nextRandoms.get(randomIndex);
        randomIndex++;
        return nextRandom % bound;
    }

    @Override
    public void shuffle(List<Integer> list) {
        for (List<Integer> swap : swapRecipe) {
            int index1 = swap.getFirst();
            int index2 = swap.getLast();
            if (index1 >= 0 && index1 < list.size() && index2 >= 0 && index2 < list.size()) {
                Collections.swap(list, index1, index2);
            } else {
                throw new IndexOutOfBoundsException("Index in shuffle recipe is out of bounds: index1=" + index1 + ", index2=" + index2);
            }
        }
    }
}