package agh.ics.oop.model;

import java.util.Comparator;
import java.util.List;

import static agh.ics.oop.WorldGUI.PLANT_ENERGY;

public class Consumption {

    public static void consume(List<Animal> animals, GrassField grassField) {
        /*
        animals to lista zwierzat na tej samej pozycji, ktore bija sie o rosline
         */
        Animal winner = resolveConflict(animals);
        winner.updateEnergy(PLANT_ENERGY);
        grassField.eatenGrassProcedure(winner.getPos());
    }

    private static Animal resolveConflict(List<Animal> animals) {
        if (animals.size() == 1) {
            return animals.getFirst();
        }
        animals.sort(Comparator
                .comparingInt(Animal::getEnergy)
                .thenComparingInt(Animal::getDaysOld)
                .thenComparingInt(Animal::getChildrenCnt)
        );
        return animals.getLast();
    }
}
