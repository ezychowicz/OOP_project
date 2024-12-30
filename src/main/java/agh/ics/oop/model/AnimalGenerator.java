package agh.ics.oop.model;

import java.util.*;

import static agh.ics.oop.WorldGUI.ANIMALS_AMOUNT;
import static agh.ics.oop.WorldGUI.GENOME_LENGTH;
import static agh.ics.oop.WorldGUI.INITIAL_ANIMAL_ENERGY;
import static agh.ics.oop.WorldGUI.MAP_WIDTH;
import static agh.ics.oop.WorldGUI.MAP_HEIGHT;

public class AnimalGenerator{
    private final int genomeLength;
    private final int initialEnergy;
    private final int animalsAmount;
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
