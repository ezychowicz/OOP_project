package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;

public class Consumption {
    private final Config config = Config.getInstance();
    private final int GRASS_ENERGY = config.getInt("GRASS_ENERGY");

    public void consume(GrassField grassField,Vector2d position) {
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
