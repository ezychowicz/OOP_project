package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IncorrectPositionException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulation implements Runnable {
    public static final String ANIMAL_STRING = "Animal";
    private final List<Vector2d> positions;
    private final List<MoveDirection> moves;
    private final WorldMap worldMap;
    public Simulation(List<Vector2d> positions, List<MoveDirection> moves, WorldMap worldMap) {
        this.positions = new ArrayList<>(positions); //zeby dało sie usuwać
        this.moves = moves;
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
                Thread.sleep(750);
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
        int animalsIndex;
        Vector2d currPos;
        fillWorldMap();
        for (int i = 0; i < moves.size(); i++) {
            animalsIndex = i % positions.size();
            currPos = positions.get(animalsIndex); //pozycja zwierzaka, którego bedziemy chcieli przenieść
            WorldElement animalAtCurrPos = worldMap.objectAt(currPos);
            worldMap.move((Animal) animalAtCurrPos, moves.get(i)); //próba przeniesienia
            positions.set(animalsIndex, animalAtCurrPos.getPos()); //zaktualizuj pozycje
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
