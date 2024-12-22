package agh.ics.oop;

import agh.ics.oop.model.Consumption;
import agh.ics.oop.model.GrassField;
import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Day {
    private final int dayCnt = 0;
    private final GrassField grassField;
    private final List<Vector2d> positions;

    public Day(GrassField grassField, List<Vector2d> positions){
        this.grassField = grassField;
        this.positions = new ArrayList<>(positions); //pozycje zwierzakow
    }

    public void dayProcedure(){
        //usuwanie martwe zwierzeta

        //przenoszenie zwierzaki

        //konsumpcja roslin

        Consumption.consume(getAnimalsAt());
        //rozmnazanie

        //wzrost nowych roslin
        grassField.plantingGrasses();
    }
}
