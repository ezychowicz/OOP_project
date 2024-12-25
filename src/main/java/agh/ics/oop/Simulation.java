package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;
public class Simulation implements Runnable {
    public static final String ANIMAL_STRING = "Animal";
    private final List<Vector2d> positions;
    private final WorldMap worldMap;
    public static int idCounter= 0;
    public Simulation(List<Vector2d> positions, WorldMap worldMap) {
        this.positions = new ArrayList<>(positions); //zeby dało sie usuwać
        this.worldMap = worldMap;
//        fillWorldMap();
    }

    private void fillWorldMap() {
        List<Integer> indicesToRemove = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            Vector2d position = positions.get(i);
            Animal animal = new Animal(position);
            try {
                worldMap.place(animal);
                sleep(750);
            }catch (IncorrectPositionException e){
                indicesToRemove.add(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        indicesToRemove.sort(Collections.reverseOrder());
        for (int idx : indicesToRemove) {
            positions.remove(idx);
        }


    }
    public WorldMap getWorldMap() {
        return worldMap;
    }

    public List<Vector2d> getPositions() {
        return positions;
    }

    public void run(){
        fillWorldMap();
        Day day = new Day((GrassField) worldMap, new NormalBehaviour());
        while (true) {
            day.setDayCnt(String.valueOf(day.getDayCnt()+1));
            try{
                day.dayProcedure();
            } catch(IncorrectPositionException e){
                throw new RuntimeException(e);
            }
            ((GrassField) worldMap).mapChanged("Day " + day.getDayCnt());

            try{
                sleep(1000);
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }

    }
}
