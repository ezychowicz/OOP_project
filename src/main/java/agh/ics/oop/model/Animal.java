package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.Config;

import java.util.*;

import static agh.ics.oop.WorldGUI.*;
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
        tworzy losową wariacje o dlugosci GENOME_LENGTH
         */
        Random random = new Random();
        List<Integer> genome = new ArrayList<>();
        for (int i = 0; i < GENOME_LENGTH; i++) {
            genome.add(random.nextInt(8)); // Losowa liczba z zakresu 0-7
        }
        return genome;
    }

    public Animal(Vector2d startPosition){
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
        this.energy = INITIAL_ANIMAL_ENERGY;
        this.daysOld = 0;
        this.childrenCnt = 0;
        this.id = idCounter++;
        this.genome = initializeGenome();
    }

    public Animal(Vector2d startPosition, Animal parent1, Animal parent2, List<Integer> genome){
        this.direction = MapDirection.NORTH;
        this.pos = startPosition;
        this.energy = 2*BREEDING_COST;
        this.daysOld = 0;
        this.childrenCnt = 0;
        this.id = idCounter++;
        this.genome = genome;
        this.genomeIdx = 0;
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
                genomeIdx = (genomeIdx + 1) % GENOME_LENGTH; //przesun sie w genomie
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

    public List<Integer> getGenome(){
        return genome;
    }

    public int getId() {
        return id;
    }

    public void DaysOldIncrement(){
        daysOld++;
    }

    public void ChildrenCntIncrement(){
        childrenCnt++;
    }

    public int getEatenGrassCnt(){
        return eatenGrassCnt;
    }

    public void EatenGrassCntIncrement(){
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

