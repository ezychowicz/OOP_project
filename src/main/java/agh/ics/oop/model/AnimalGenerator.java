package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;

import java.util.*;

public class AnimalGenerator{
    private final int animalsAmount;
    private final Config config = Config.getInstance();

    private final int MAP_WIDTH = config.getInt("MAP_WIDTH");
    private final int MAP_HEIGHT = config.getInt("MAP_HEIGHT");

    public AnimalGenerator(){
        this.animalsAmount = config.getInt("ANIMALS_AMOUNT");
    }

    private Vector2d randomInitialPosition(){ // losowo ale gdzies blizej srodka
        return new Vector2d((int)(Math.random() * MAP_WIDTH/2 + MAP_WIDTH/4), (int)(Math.random() * MAP_HEIGHT/2 + MAP_HEIGHT/4));
    }

    public List<Vector2d> generateInitialPositions(){
        List<Vector2d> positions = new ArrayList<>();
        for (int i = 0; i < animalsAmount; i++){
            Vector2d position = randomInitialPosition();
            while (positions.contains(position)){
                position = randomInitialPosition();
            }
            positions.add(position);
        }
        return positions;
    }
}
