package agh.ics.oop;

import agh.ics.oop.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static agh.ics.oop.WorldGUI.DAY_EFFORT_ENERGY;

public class Day {
    private final int dayCnt = 0;
    private final GrassField grassField;
    private final Map<Vector2d, List<Animal>> animals;
    private final AnimalBehaviour animalBehaviour;
    public Day(GrassField grassField, AnimalBehaviour animalBehaviour) {
        this.grassField = grassField;
        this.animals = grassField.getAnimals(); //pozycje zwierzakow
        this.animalBehaviour = animalBehaviour;
    }

    public void dayProcedure(){
        //usuwanie martwych zwierzat z animals i codzienne redukowanie energii
        updateAnimalsState();

        //poruszanie sie zwierzakow
        animals = updateAnimalsPositions(animalBehaviour);

        //konsumpcja roslin
        for (Vector2d position : animals.keySet()) {
            Consumption.consume(grassField, position);
        }

        //rozmnazanie


        //wzrost nowych roslin
        grassField.plantingGrasses();
    }

    private void updateAnimalsState(){
        for (List<Animal> animalsAtPos : animals.values()){
            Vector2d pos = animalsAtPos.getFirst().getPos(); //potrzebne zeby potem moc ewentualnie usunac po kluczu(czyli po position)
            List<Integer> toRemoveIdxs = new ArrayList<>();
            for (int i = 0; i < animalsAtPos.size(); i++){
                Animal animal = animalsAtPos.get(i);
                animal.updateEnergy(-DAY_EFFORT_ENERGY);
                if (animal.getEnergy() <= 0){
                    toRemoveIdxs.add(i);
                }
            }
            for (int idx : toRemoveIdxs.reversed()){
                animalsAtPos.remove(idx);
            }
            if (animalsAtPos.isEmpty()){
                animals.remove(pos);
            }
        }
    }

    private void addAnimalAtPos(Map<Vector2d, List<Animal>> animals, Animal newAnimal, Vector2d position){
        if (!animals.containsKey(position)){
            animals.put(position, new ArrayList<>());
        }else{
            animals.get(position).add(newAnimal);
        }
    }
    private Map<Vector2d,List<Animal>> updateAnimalsPositions(AnimalBehaviour behaviour){
        Map<Vector2d,List<Animal>> newAnimals =  new HashMap<>();
        for (List<Animal> animalsAtPos : animals.values()){
            for (Animal animal : animalsAtPos){
                animal.move(MoveDirection.geneToDirection(animal.getGenomeAtIdx(behaviour.nextGeneIdx(animal))), grassField);
                //to jest okropne. demeter zawiedziona
                addAnimalAtPos(newAnimals, animal, animal.getPos());
            }
        }
        return newAnimals;
    }


}
