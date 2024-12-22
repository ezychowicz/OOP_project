package agh.ics.oop.model;

import static agh.ics.oop.WorldGUI.GRASS_ENERGY;

public class Consumption {

    public static void consume(GrassField grassField, Vector2d position) {
        /*
        animals to lista zwierzat na tej samej pozycji, ktore bija sie o rosline
         */
        Animal winner = grassField.resolveConflict(grassField.getAnimalsAt(position));
        winner.updateEnergy(GRASS_ENERGY);
        grassField.eatenGrassProcedure(winner.getPos());
    }


}
