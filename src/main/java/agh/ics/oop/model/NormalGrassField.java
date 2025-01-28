package agh.ics.oop.model;

import agh.ics.oop.model.util.Converter;

import java.util.ArrayList;

public class NormalGrassField extends GrassField{

    public NormalGrassField(int grassCount, int width, int height) {
        super(grassCount, width, height);
        initializeEquator();
        plantingGrasses(grassCount);
    }

    private void initializeEquator(){
        int equatorY = height/2;
        for (int x = 0; x < width; x++){
            preferredSet.add(Converter.convertToIdx(new Vector2d(x, equatorY),width));
        }
        for (int x = 0; x <width; x++){
            for (int y = 0; y < height; y++){
                if (y != equatorY) {
                    unpreferredSet.add(Converter.convertToIdx(new Vector2d(x, y), width));
                }
            }
        }
        availableIdxs.set(0, new ArrayList<Integer> (preferredSet));
        availableIdxs.set(1, new ArrayList<Integer> (unpreferredSet));
    }

    @Override
    public void eatenGrassProcedure(Vector2d position) {
        grasses.remove(position);
        int idx = Converter.convertToIdx(position, width);
        if (isOnEquator(position)) {
            preferredSet.add(idx);
        } else {
            unpreferredSet.add(idx);
        }
    }
    @Override
    public void plantingGrasses(int grassCount) {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(this, grassCount);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grasses.put(grassPosition, new Grass(grassPosition));
            int newGrassIdx = Converter.convertToIdx(grassPosition, width);
            preferredSet.remove(newGrassIdx); // Remove from preferred if a plant is planted there
            unpreferredSet.remove(newGrassIdx); // Remove from unpreferred if a plant is planted there (an occupied by plant position is neither preferred nor unpreferred)
        }
        availableIdxs.set(0, new ArrayList<Integer> (preferredSet));
        availableIdxs.set(1, new ArrayList<Integer> (unpreferredSet));
    }

    private boolean isOnEquator(Vector2d position){
        int equatorY = height/2;
        return position.getY() == equatorY;
    }
}
