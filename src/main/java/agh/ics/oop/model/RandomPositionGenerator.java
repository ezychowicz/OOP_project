package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.util.MathUtils;

import java.util.*;

import static java.lang.Math.round;

public class RandomPositionGenerator implements Iterable<Vector2d> {
    private final int maxWidth;
    private final int maxHeight;
    private final int grassCount;
    private int count;
    private final Random random;
//    private final List<Integer> availableIndices;
    private final List<Integer> preferredIdxs;
    private final List<Integer> unPreferredIdxs;
    private final GrassField grassField;
    public RandomPositionGenerator(GrassField grassField, int grassCount) {

        this.grassField = grassField;
        this.maxWidth = grassField.getWidth();
        this.maxHeight = grassField.getHeight();
        this.grassCount = grassCount;
        this.preferredIdxs = grassField.getAvailableIdxs().get(0);
        this.unPreferredIdxs = grassField.getAvailableIdxs().get(1);
        this.count = 0;
        this.random = new Random();
    }


    @Override
    public Iterator<Vector2d> iterator() {
        return new Iterator<Vector2d>(){
            @Override
            public boolean hasNext() {
                return count < grassCount && (!preferredIdxs.isEmpty() || !unPreferredIdxs.isEmpty());
            }

            @Override
            public Vector2d next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No grasses left to allocate/no positions available");
                }
                count++;
                List<Integer> idxsToChoose;
                if (!preferredIdxs.isEmpty() && !unPreferredIdxs.isEmpty()) {
                    idxsToChoose = MathUtils.nonClassicalProbabilityRandom("preferred", 0.8, "unpreferred", 0.2).equals("preferred") ? preferredIdxs : unPreferredIdxs;
                } else if (!unPreferredIdxs.isEmpty()) {
                    idxsToChoose = unPreferredIdxs;
                } else {
                    idxsToChoose = preferredIdxs;
                }
                int randomInt = random.nextInt(idxsToChoose.size()); //losuje w zakresie dostepnych pozycji
                Vector2d pos = grassField.getPosList().get(idxsToChoose.get(randomInt));
                idxsToChoose.remove(randomInt);
                return pos;
            }
        };
    }


}
