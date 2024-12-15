package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.HashMap;
import java.util.Map;

import static agh.ics.oop.model.MoveDirection.*;

public class RectangularMap extends AbstractWorldMap {
    private final int width;
    private final int height;
    private final Vector2d upperRightBoundary;
    private final Vector2d lowerLeftBoundary;

    public RectangularMap(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.upperRightBoundary = new Vector2d(this.width, this.height);
        this.lowerLeftBoundary = new Vector2d(0, 0);
    }

    public boolean isOnMap(Vector2d position) {
        return position.precedes(upperRightBoundary) && position.follows(lowerLeftBoundary);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && isOnMap(position);
    }

    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(lowerLeftBoundary, upperRightBoundary);
    }


    private Vector2d moveForwardBackward(Animal animal, MoveDirection currMoveDirection) {
        Vector2d move;
        switch (animal.getDirection()) {
            case NORTH ->
                    move = (currMoveDirection == FORWARD ? MapDirection.NORTH_UNIT_VECTOR : MapDirection.SOUTH_UNIT_VECTOR);
            case SOUTH ->
                    move = (currMoveDirection == FORWARD ? MapDirection.SOUTH_UNIT_VECTOR : MapDirection.NORTH_UNIT_VECTOR);
            case EAST ->
                    move = (currMoveDirection == FORWARD ? MapDirection.EAST_UNIT_VECTOR : MapDirection.WEST_UNIT_VECTOR);
            case WEST ->
                    move = (currMoveDirection == FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
            default -> throw new IllegalStateException("Unexpected value: " + animal.getDirection());
        }
        return animal.getPos().add(move);
    }
}
