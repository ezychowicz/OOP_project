package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

import static agh.ics.oop.Simulation.ANIMAL_STRING;

public abstract class AbstractWorldMap implements WorldMap{
    private final UUID id = UUID.randomUUID();
    protected Map<Vector2d, List<Animal>> animals = new HashMap<>();  //animals jest w obu mapach
    public abstract Boundary getCurrentBounds();
    public abstract boolean canMoveTo(Vector2d position);
    protected final List<MapChangeListener> observers = new ArrayList<>();


    public void addObserver(MapChangeListener newObserver){
        observers.add(newObserver);
    }


    public void removeObserver(MapChangeListener oldObserver){
        observers.remove(oldObserver);
    }

    public void mapChanged(String message) {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this, message);
        }
    }

    public boolean isOccupied(Vector2d position){ //jakies dziadostwo
        if (animals.containsKey(position)) {
            return !animals.get(position).isEmpty();
        }
        return false;
    }

    @Override
    public WorldElement objectAt(Vector2d position){ //najlepsze zwierze na danej pozycji
        return resolveConflict(animals.get(position));
    }

    public List<Animal> getAnimalsAt(Vector2d position){
        return animals.get(position);
    }

    public Map<Vector2d, List<Animal>> getAnimals(){
        return animals;
    }


    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPos())) {
            animals.put(animal.getPos(), new ArrayList<>(List.of(animal)));
            mapChanged("%s placed at %s".formatted(ANIMAL_STRING, animal.getPos()));
            return true;
        }
        throw new IncorrectPositionException(animal.getPos());
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animals.containsKey(animal.getPos()) && animals.get(animal.getPos()).contains(animal)) {
            Vector2d initialPos = animal.getPos();
            animals.get(initialPos).remove(animal);  // remove the animal from the current position
            animal.move(direction, this); // move the animal
            animals.computeIfAbsent(animal.getPos(), k -> new ArrayList<>()).add(animal); // add the animal to the new position
            if (!Objects.equals(initialPos, animal.getPos())) { // if the animal actually moved
                mapChanged("%s moved %s to %s".formatted(ANIMAL_STRING, direction, animal.getPos()));
            } else if (direction == MoveDirection.RIGHT || direction == MoveDirection.LEFT) {
                mapChanged("%s on %s turned %s".formatted(ANIMAL_STRING, initialPos, direction));
            }
        }
    }



    public Vector2d getUpperRightBoundary(){
        return getCurrentBounds().upperRightBound();
    }

    public Vector2d getLowerLeftBoundary(){
        return getCurrentBounds().lowerLeftBound();
    }

    @Override
    public String toString(){
        Vector2d lowerLeftBoundary = getLowerLeftBoundary();
        Vector2d upperRightBoundary =getUpperRightBoundary();
        MapVisualizer mapVis = new MapVisualizer(this);
        return mapVis.draw(lowerLeftBoundary, upperRightBoundary);
    }


    public Animal resolveConflict(List<Animal> animals) { //rozwiazuje konflikt gdy wiecej niz jedno zwierze na danej pozycji
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

    public UUID getId(){
        return id;
    }
}

