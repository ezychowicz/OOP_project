package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;

public class Animal implements WorldElement{
    private MapDirection direction;
    private Vector2d pos;
    private int energy;
    private int daysOld;
    private int childrenCnt;
    private static final Vector2d RIGHT_BOUNDARY_VECTOR = new Vector2d(4,4);
    private static final Vector2d LEFT_BOUNDARY_VECTOR = new Vector2d(0,0);
    public Animal(Vector2d startPosition){
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
    }

    //nowy konstruktor docelowo ten pierwszy zostanie usuniety ale zeby nie zepsuc wszystkiego na razie jest tak
    public Animal(Vector2d startPosition, int startingEnergy){
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
        this.energy = startingEnergy;
        this.daysOld = 0;
        this.childrenCnt = 0;
    }

    public Animal() {
        this(new Vector2d(2,2));
    }
    @Override
    public String toString() {
        return switch(direction){
            case NORTH -> "^";
            case SOUTH -> "v";
            case EAST -> ">";
            case WEST -> "<";
        };
    }
    public boolean isAt(Vector2d pos) {
        return this.pos.equals(pos);
    }

    public MapDirection getDirection() {
        return direction;
    }
    public Vector2d getPos() {
        return pos;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }


    private void moveForwardBackward(MoveDirection currMoveDirection, MapDirection direction, MoveValidator validator) throws IncorrectPositionException{
        Vector2d move;
        switch (direction) {
            case NORTH -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.NORTH_UNIT_VECTOR : MapDirection.SOUTH_UNIT_VECTOR);
            case SOUTH -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.SOUTH_UNIT_VECTOR : MapDirection.NORTH_UNIT_VECTOR);
            case EAST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.EAST_UNIT_VECTOR : MapDirection.WEST_UNIT_VECTOR);
            case WEST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        Vector2d newPosition = pos.add(move);
        if (validator.canMoveTo(newPosition)) {
            pos = newPosition;
        }else{
            throw new IncorrectPositionException(newPosition);
        }
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        switch(direction) {
            case LEFT -> this.direction = this.direction.previous();
            case RIGHT -> this.direction = this.direction.next();
            case FORWARD, BACKWARD -> {
                try {
                    moveForwardBackward(direction, this.direction, validator);
                } catch (IncorrectPositionException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public int getEnergy() {
        return energy;
    }

    public void updateEnergy(int energy) {
        this.energy += energy;
    }
    public int getDaysOld() {
        return daysOld;
    }

    public int getChildrenCnt(){
        return childrenCnt;
    }
}
