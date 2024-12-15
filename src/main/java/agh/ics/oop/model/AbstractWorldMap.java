package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

import static agh.ics.oop.Simulation.ANIMAL_STRING;

public abstract class AbstractWorldMap implements WorldMap{
    private final UUID id = UUID.randomUUID();
    protected final Map<Vector2d, Animal> animals = new HashMap<>();  //animals jest w obu mapach
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

    public boolean isOccupied(Vector2d position){
        return animals.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position){
        return animals.get(position);
    }

    public Map<Vector2d, Animal> getAnimals(){
        return animals;
    }


    @Override
    public boolean place(Animal animal) throws IncorrectPositionException {
        if (canMoveTo(animal.getPos())) {
            animals.put(animal.getPos(), animal);
            mapChanged("%s placed at %s".formatted(ANIMAL_STRING, animal.getPos()));
            return true;
        }
        throw new IncorrectPositionException(animal.getPos());
    }

    @Override
    public void move(Animal animal, MoveDirection direction) {
        if (animals.containsValue(animal)) {
            Vector2d initialPos = animal.getPos();
            animals.remove(animal.getPos());  //usuń zwierzaka z aktualnej pozycji na mapie
            animal.move(direction, this); //przenieś zwierzaka, chyba że sie nie da to nic nie zmieniaj
            animals.put(animal.getPos(), animal); //umieść na mapie zwierzaka tam gdzie go przeniosłeś (lub przywróć)
            if (!Objects.equals(initialPos, animal.getPos())) { //jesli faktycznie sie przemiescil
                mapChanged("%s moved %s to %s".formatted(ANIMAL_STRING, direction, animal.getPos()));
            }else if (direction == MoveDirection.RIGHT || direction == MoveDirection.LEFT){
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

    @Override
    public List<WorldElement> getElements(){
        return new ArrayList<>(animals.values());
    }

    public UUID getId(){
        return id;
    }
}

