package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.Config;

import java.util.*;

import static agh.ics.oop.model.MoveDirection.BACKWARD;
import static agh.ics.oop.Simulation.idCounter;

public class Animal implements WorldElement{
    private MapDirection direction;
    private Vector2d pos;
    private int energy;
    private int daysOld;
    private int childrenCnt;
    private final List<Integer> genome;
    private int genomeIdx;
    private int eatenGrassCnt;
    private int descendantsCnt;
    private int deathDay = -1;
    private final int id;

    private final Config config = Config.getInstance();
    private final int INITIAL_ANIMAL_ENERGY = config.getInt("INITIAL_ANIMAL_ENERGY");
    private final int BREEDING_COST = config.getInt("BREEDING_COST");
    private final int GENOME_LENGTH = config.getInt("GENOME_LENGTH");
    private final int MAP_WIDTH = config.getInt("MAP_WIDTH");

    private List<Integer> initializeGenome() {
        /*
        Create random variation of GENOME_LENGTH length
         */
        Random random = new Random();
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < GENOME_LENGTH; i++) {
            genome.add(random.nextInt(8)); // RNG 0-7
        }
        return genome;
    }

    public Animal(Vector2d startPosition){ // zwierzak startowy
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
        this.energy = INITIAL_ANIMAL_ENERGY;
        this.daysOld = 0;
        this.childrenCnt = 0;
        this.id = idCounter++;
        this.genome = initializeGenome();
    }

    public Animal(Vector2d startPosition, List<Integer> genome){ // zwierzak dziecko
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
        this.energy = 2*BREEDING_COST;
        this.daysOld = 0;
        this.childrenCnt = 0;
        this.id = idCounter++;
        this.genome = genome;
        this.genomeIdx = -1;
        this.eatenGrassCnt = 0;
        this.descendantsCnt = 0;
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

    public boolean moveForward(MapDirection direction,MoveValidator validator) throws IncorrectPositionException{
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
        kreci zwierzorem (max 8 razy)
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
                this.direction = newDirection; // Update animal's position
            }else{  // If the move is illegal (on poles)
                this.direction = rotate(BACKWARD); // Rotate animal backwards
            }
        } catch (IncorrectPositionException e) {
            e.printStackTrace();
        }
    }

    //gettery do statystyk etc

    public void setGenomeIdx(int genomeIdx) {
        this.genomeIdx = genomeIdx;
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

    public List<Integer> getGenome(){
        return genome;
    }

    public int getId() {
        return id;
    }

    public void daysOldIncrement(){
        daysOld++;
    }

    public void childrenCntIncrement(){
        childrenCnt++;
    }

    public int getEatenGrassCnt(){
        return eatenGrassCnt;
    }

    public void eatenGrassCntIncrement(){
        eatenGrassCnt++;
    }

    public int getDescendantsCnt(){
        return descendantsCnt;
    }

    public void setDescendantsCnt(int cnt){
        descendantsCnt = cnt;
    }

    public int getDeathDay(){
        return deathDay;
    }

    public void setDeathDay(int day){
        deathDay = day;
    }
}

