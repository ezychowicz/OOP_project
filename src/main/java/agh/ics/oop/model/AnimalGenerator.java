package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;

import java.util.*;

public class AnimalGenerator{
    private final int genomeLength;
    private final int initialEnergy;
    private final int animalsAmount;
    private final Config config = Config.getInstance();
    private final int GENOME_LENGTH = config.getInt("GENOME_LENGTH");
    private final int INITIAL_ANIMAL_ENERGY = config.getInt("INITIAL_ANIMAL_ENERGY");
    private final int ANIMALS_AMOUNT = config.getInt("ANIMALS_AMOUNT");
    private final int MAP_WIDTH = config.getInt("MAP_WIDTH");
    private final int MAP_HEIGHT = config.getInt("MAP_HEIGHT");
    public AnimalGenerator(){
        this.genomeLength = GENOME_LENGTH;
        this.initialEnergy = INITIAL_ANIMAL_ENERGY;
        this.animalsAmount = ANIMALS_AMOUNT;
    }

    private Vector2d randomInitialPosition(){
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
