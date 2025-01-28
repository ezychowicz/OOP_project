package agh.ics.oop.model;

import agh.ics.oop.model.util.Converter;

import java.util.ArrayList;

public class SprawlingJungle extends GrassField{
    public SprawlingJungle(int grassCount, int width, int height) {
        super(grassCount, width, height);
        initializeUnpreferred();
    }

    private void initializeUnpreferred() {
        for (int x = 0; x <width; x++){
            for (int y = 0; y < height; y++){
                unpreferredSet.add(Converter.convertToIdx(new Vector2d(x, y), width));
            }
        }
    }

    @Override
    public void eatenGrassProcedure(Vector2d position){
        /*
         * A method handling adding to preferred and unpreferred on fields where a plant has just been eaten (newly freed).
         * It is triggered only after decrementing neighbors count in cumulativePrefs of all removed plants on a given day,
         * because the cumulativePrefs state must be up-to-date.
         */
        grasses.remove(position);

        int idx = Converter.convertToIdx(position, width);
        cumulativePrefs.decrementAdjacentTo(position);
        if (cumulativePrefs.getPreferenceScoreAtPos(position) == 0){
            unpreferredSet.add(idx);
        } else {
            preferredSet.add(idx);
        }

    }

    @Override
    public void plantingGrasses(int grassCount){
        /*
         * Method for planting new plants
         */
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(this, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
            super.updatePreferences(grassPosition, preferredSet, unpreferredSet);
        }
        availableIdxs.set(0, new ArrayList<Integer>(preferredSet));
        availableIdxs.set(1, new ArrayList<Integer> (unpreferredSet));
    }
}