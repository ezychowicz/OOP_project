package agh.ics.oop.model;

import agh.ics.oop.model.util.Converter;

import java.util.ArrayList;

public class SprawlingJungle extends GrassField{
    public SprawlingJungle(int grassCount, int width, int height) {
        super(grassCount, width, height);
    }

    @Override
    public void eatenGrassProcedure(Vector2d position){
        /*
         * metoda obslugujaca dodawanie do preferred i unpreferred na polach na ktorych dopiero zjedzono rosline (nowo zwolnionych)
         * uruchamiana dopiero po wykonaniu dekrementacji dla sasiadow wszystkich usuwanych traw danego dnia
         * bo musi byc aktualny stan cumulativePrefs
         */
        grasses.remove(position);

        int idx = Converter.convertToIdx(position, width);
        if (cumulativePrefs.getPreferenceScoreAtPos(position) == 0){
            unpreferredSet.add(idx);
        } else {
            preferredSet.add(idx);
        }

    }

    @Override
    public void plantingGrasses(int grassCount){
        /*
         * metoda do sadzenia nowych roslin i aktualizowania przy tym preferredSet i unpreferredSet
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