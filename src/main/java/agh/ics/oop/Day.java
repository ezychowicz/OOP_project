package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.CopulationFailedException;
import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.*;

import static agh.ics.oop.WorldGUI.GRASS_GROWTH_EACH_DAY;
import static agh.ics.oop.WorldGUI.INITIAL_ANIMAL_ENERGY;


public class Day {
    private int dayCnt = 0;
    private final GrassField grassField;
    private Map<Vector2d, List<Animal>> animals;
    private final AnimalBehaviour animalBehaviour;
    private static final int DAY_EFFORT_ENERGY = 1;
    public Day(GrassField grassField, AnimalBehaviour animalBehaviour) {
        this.grassField = grassField;
        this.animals = grassField.getAnimals(); //pozycje zwierzakow
        this.animalBehaviour = animalBehaviour;
    }

    public void dayProcedure() throws IncorrectPositionException{
        System.out.println("Dzien: " + dayCnt);
        //usuwanie martwych zwierzat z animals i codzienne redukowanie energii
        updateAnimalsState();
        //poruszanie sie zwierzakow
        animals = updateAnimalsPositions(animalBehaviour);
        grassField.setAnimals(animals); // sprawdz czy na pewno to jest git

        //konsumpcja roslin
        for (Vector2d position : animals.keySet()) {
            Consumption.consume(grassField, position);
        }

        //rozmnazanie
        //iterator umozliwia modyfikowanie obiektu iterowanego w trakcie iteracji po nim w sposob bezpieczny
        Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
            Vector2d position = entry.getKey();
            List<Animal> animalsAtPos = entry.getValue();

            if (animalsAtPos.size() >= 2) {
                try {
                    Copulation copulation = new Copulation(position, grassField);
                    Animal newborn = copulation.copulate();
                    animalsAtPos.add(newborn); // Dodajemy nowo narodzone zwierzę do listy

                    // Zaktualizuj listę zwierząt w mapie
                    animals.put(position, animalsAtPos); // Bez usuwania klucza, po prostu zaktualizuj listę

                } catch (CopulationFailedException e) {
                    System.out.println("Kopulacja nie powiodła się dla pozycji: " + position);
                }
            }
        }
        grassField.setAnimals(animals);

        //wzrost nowych roslin
        grassField.plantingGrasses(GRASS_GROWTH_EACH_DAY);
    }

    private void updateAnimalsState() {
        List<Vector2d> positionsToRemove = new ArrayList<>();

        // wczesniej animalsy byly usuwane w trakcie iterowania, ale to nie dziala - trzeba stworzyc liste "do usuniecia" i dopiero potem je usunac
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            Vector2d pos = entry.getKey();
            List<Animal> animalsAtPos = entry.getValue();
            List<Integer> toRemoveIdxs = new ArrayList<>();

            for (int i = 0; i < animalsAtPos.size(); i++) {
                Animal animal = animalsAtPos.get(i);
                animal.updateEnergy(-DAY_EFFORT_ENERGY);
                if (animal.getEnergy() <= 0){
                    System.out.println("huj huj huj");
                    toRemoveIdxs.add(i);
                }
            }

            // Remove animals marked for removal
            for (int idx = toRemoveIdxs.size() - 1; idx >= 0; idx--) { // Reverse loop
                animalsAtPos.remove((int) toRemoveIdxs.get(idx));
            }

            // Mark position for removal if no animals are left
            if (animalsAtPos.isEmpty()) {
                positionsToRemove.add(pos);
            }
        }

        // Remove empty positions from the map
        for (Vector2d pos : positionsToRemove) {
            animals.remove(pos);
        }
    }

    private void addAnimalAtPos(Map<Vector2d, List<Animal>> animals, Animal newAnimal, Vector2d position){
        if (!animals.containsKey(position)) {
            animals.put(position, new ArrayList<>());
        }
        animals.get(position).add(newAnimal);

    }
    private Map<Vector2d,List<Animal>> updateAnimalsPositions(AnimalBehaviour behaviour) throws IncorrectPositionException{
        Map<Vector2d,List<Animal>> newAnimals =  new HashMap<>();
        for (List<Animal> animalsAtPos : animals.values()){
            for (Animal animal : animalsAtPos){
                animal.move(MoveDirection.geneToDirection(animal.getGenomeAtIdx(behaviour.nextGeneIdx(animal))), grassField);
                System.out.println("id zwierzaka: " + animal.getId() + ", energia: " + animal.getEnergy());
                //to jest okropne. demeter zawiedziona
                addAnimalAtPos(newAnimals, animal, animal.getPos());
            }
        }
        return newAnimals;
    }


    public int getDayCnt(){
        return dayCnt;
    }

    public void setDayCnt(String s){
        dayCnt = Integer.parseInt(s);
    }
}
