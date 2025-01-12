package agh.ics.oop.model;

import static agh.ics.oop.WorldGUI.GRASS_ENERGY;

public class Consumption {

    public static void consume(GrassField grassField, Vector2d position) {
        if (grassField.getAnimalsAt(position) == null || grassField.getAnimalsAt(position).isEmpty()) return;
        // nie wiem czy ten if powinien sie wogole moc wydarzyc itp, to moze nie dzialac calkowicie
        if (grassField.getAnimalsAt(position).size() >= 1 && grassField.getGrassAt(position) != null) {
            Animal winner=grassField.resolveConflict(grassField.getAnimalsAt(position));
            winner.updateEnergy(GRASS_ENERGY);
            winner.EatenGrassCntIncrement();
            grassField.eatenGrassProcedure(winner.getPos());
        }
    }
}
