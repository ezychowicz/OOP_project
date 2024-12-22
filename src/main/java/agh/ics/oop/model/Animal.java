package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static agh.ics.oop.WorldGUI.GENOME_LENGTH;
import static agh.ics.oop.WorldGUI.MAP_WIDTH;
import static agh.ics.oop.model.MoveDirection.BACKWARD;

public class Animal implements WorldElement{
    private MapDirection direction;
    private Vector2d pos;
    private int energy;
    private int daysOld;
    private int childrenCnt;
    private final List<Integer> genome = initializeGenome();
    private final int genomeIdx = 0;
    private static final Vector2d RIGHT_BOUNDARY_VECTOR = new Vector2d(4,4);
    private static final Vector2d LEFT_BOUNDARY_VECTOR = new Vector2d(0,0);
    public Animal(Vector2d startPosition){
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
    }

    private List<Integer> initializeGenome() {
        /*
        na razie po prostu tworzy losową wariacje o dlugosci GENOME_LENGTH
         */
        Random random = new Random();
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < GENOME_LENGTH; i++) {
            genome.add(random.nextInt(8)); // Losowa liczba z zakresu 0-7
        }
        return genome;
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
            case NORTH_EAST -> "^>";
            case SOUTH_EAST -> "v>";
            case SOUTH_WEST -> "<v";
            case NORTH_WEST -> "<^";
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

//
//    private void moveForwardBackward(MoveDirection currMoveDirection, MapDirection direction, MoveValidator validator) throws IncorrectPositionException{
//        Vector2d move;
//        switch (direction) {
//            case NORTH -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.NORTH_UNIT_VECTOR : MapDirection.SOUTH_UNIT_VECTOR);
//            case SOUTH -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.SOUTH_UNIT_VECTOR : MapDirection.NORTH_UNIT_VECTOR);
//            case EAST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.EAST_UNIT_VECTOR : MapDirection.WEST_UNIT_VECTOR);
//            case WEST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
//            case NORTH_WEST ->  move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
//            case NORTH_EAST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
//            case SOUTH_EAST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
//            case SOUTH_WEST -> move = (currMoveDirection == MoveDirection.FORWARD ? MapDirection.WEST_UNIT_VECTOR : MapDirection.EAST_UNIT_VECTOR);
//            default -> throw new IllegalStateException("Unexpected value: " + direction);
//        }
//        Vector2d newPosition = pos.add(move, grassField.width);
//        if (validator.canMoveTo(newPosition)) {
//            pos = newPosition;
//        }else{//zwierzak chcial wyjsc za biegun
//
////            throw new IncorrectPositionException(newPosition);
//        }
//    }

//    public void move(MoveDirection direction, MoveValidator validator) {
//        switch(direction) {
//            case LEFT -> this.direction = this.direction.previous();
//            case RIGHT -> this.direction = this.direction.next();
//            case FORWARD, BACKWARD -> {
//                try {
//                    moveForwardBackward(direction, this.direction, validator);
//                } catch (IncorrectPositionException e) {
//                    e.printStackTrace();
//                }
//            }
//            case FORWARD_RIGHT, FORWARD_LEFT ->
//            case BACKWARD_RIGHT, BACKWARD_LEFT ->
//        };
//    }
    private boolean moveForward(MapDirection direction, MoveValidator validator) throws IncorrectPositionException{
        Vector2d move;
        switch (direction) {
            case NORTH -> move =  MapDirection.NORTH_UNIT_VECTOR;
            case SOUTH -> move =  MapDirection.SOUTH_UNIT_VECTOR;
            case EAST -> move = MapDirection.EAST_UNIT_VECTOR;
            case WEST -> move = MapDirection.WEST_UNIT_VECTOR;
            case NORTH_WEST ->  move = MapDirection.NORTHWEST_UNIT_VECTOR;
            case NORTH_EAST -> move = MapDirection.NORTHEAST_UNIT_VECTOR;
            case SOUTH_EAST -> move = MapDirection.SOUTHEAST_UNIT_VECTOR;
            case SOUTH_WEST -> move = MapDirection.SOUTHWEST_UNIT_VECTOR;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        }
        Vector2d newPosition = pos.add(move, MAP_WIDTH);
        if (validator.canMoveTo(newPosition)) {
            pos = newPosition;
            return true;
        }
        return false;
    }

    private MapDirection rotate(MoveDirection direction) {
        /*
        obroc zwierzaka odpowiednia liczbe razy; srednio efektywne
         */
        int numOfRotations = direction.directionToGene(direction);
        MapDirection newDirection = this.direction;
        while (numOfRotations-- > 0) {
            newDirection = newDirection.next();
        }
        return newDirection;
    }

    public void move(MoveDirection direction, MoveValidator validator) {
        try {
            MapDirection newDirection = rotate(direction);
            if (moveForward(newDirection, validator)) {
                this.direction = newDirection; //zaktualizuj zeby byl skierowany zgodnie z kierunkiem w ktorym sie poruszyl
            }else{ //zwierzak chcial wyjsc za biegun
                this.direction = rotate(BACKWARD); //odwroc zwierzaka
            }
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
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

    public int getGenomeIdx() {
        return genomeIdx;
    }

    public int getGenomeAtIdx(int idx){
        return genome.get(idx);
    }
}

