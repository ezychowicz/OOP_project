package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.ImportStats;
import agh.ics.oop.presenter.SimulationPresenter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static agh.ics.oop.WorldGUI.*;
import static java.lang.Thread.sleep;

public class Simulation implements Runnable {
    public static final String ANIMAL_STRING = "Animal";
    private final List<Vector2d> positions;
    private final WorldMap worldMap;
    public static int idCounter = 0;
    private SimulationEngine simEngine;
    private final Day day;
    private final int SIMULATION_SPEED = 200;

    public Simulation(List<Vector2d> positions, WorldMap worldMap, Day day) {
        this.positions = new ArrayList<>(positions); //zeby dało sie usuwać
        this.worldMap = worldMap;
        this.day = day;
    }

    public void setSimulationEngine(SimulationEngine simEngine) {
        this.simEngine = simEngine;
    }

    public void run() {
        fillWorldMap();

        day.setWatchedAnimalId(day.getFirstCurrSimAnimal().getId());

        while (true) {
            try {
                simEngine.pauseSimulationIfNeeded();
                day.setDayCnt(day.getDayCnt() + 1);
                day.dayProcedure();
                // statystyki
                SimulationPresenter.getInstance().updateSimulationInfo();
                SimulationPresenter.getInstance().updateAnimalInfo(day.getWatchedAnimal()); //domyslnie wybiera sie losowy animal a nie z najmniejszym id z jakiegos powodu ale w sumie to nawet lepiej wiec tego nie naprawiam!
                ((GrassField) worldMap).mapChanged("Day " + day.getDayCnt());

                Thread.sleep(SIMULATION_SPEED);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IncorrectPositionException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillWorldMap() {
        List<Integer> indicesToRemove = new ArrayList<>();
        for (int i = 0; i < positions.size(); i++) {
            Vector2d position = positions.get(i);
            Animal animal = new Animal(position);
            try {
                worldMap.place(animal);
                Thread.sleep(100);
            } catch(IncorrectPositionException | InterruptedException e){
                indicesToRemove.add(i);
            }
        }
        indicesToRemove.sort(Collections.reverseOrder());
        for (int idx : indicesToRemove) {
            positions.remove(idx);
        }
    }

    public int getAnimalsCount(){
        return day.currAnimalsCnt;
    }

    public int getPlantsCount(){
        return day.currPlantsCnt;
    }

    public int getFreeFieldsCount(){
        return day.currFreeFieldsCnt;
    }

    public String getMostPopularGenotypes(){
        return day.getMostPopularGenotypes();
    }

    public float getAverageEnergy(){
        return day.getAverageEnergy();
    }

    public float getAverageLifespan(){
        return day.getAverageLifespan();
    }

    public float getAverageChildren(){
        return day.getAverageChildren();
    }
}
