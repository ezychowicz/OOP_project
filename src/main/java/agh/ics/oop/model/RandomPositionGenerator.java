package agh.ics.oop.model;

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
        this.maxWidth = grassField.getCurrentBounds().upperRightBound().getX();
        this.maxHeight = grassField.getCurrentBounds().upperRightBound().getY();
        this.grassCount = grassCount;
        this.preferredIdxs = grassField.getAvailableIdxs().getFirst();
        this.unPreferredIdxs = grassField.getAvailableIdxs().getLast();
        if (grassCount > preferredIdxs.size() + unPreferredIdxs.size()) {
            throw new IllegalArgumentException("grassCount cannot be greater than the total number of available positions");
        }
        this.count = 0;
        this.random = new Random();
    }


//    private String nonClassicalProbabilityRandom(String value1, double prob1, String value2, double prob2){
//        if (prob1 + prob2 != 1){
//            throw new IllegalArgumentException();
//        }
//        int convertedProb1 = (int) Math.round(100*prob1);
//        int convertedProb2 = (int) Math.round(100*prob2);
//
//        if (convertedProb1 + convertedProb2 != 100){
//            throw new IllegalArgumentException("given probabilities are unmappable to 0-100 integers");
//        }
//
//        int randomNum = random.nextInt(100);
//        return randomNum < convertedProb1 ? value1 : value2;
//    }
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
                    idxsToChoose = unPreferredIdxs;
                }
                int randomInt = random.nextInt(idxsToChoose.size()); //losuje w zakresie dostepnych pozycji
                Vector2d pos = grassField.getPosList().get(idxsToChoose.get(randomInt));
                idxsToChoose.remove(randomInt);
                return pos;
            }
        };
    }


}
