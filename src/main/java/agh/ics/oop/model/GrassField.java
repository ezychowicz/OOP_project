package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.util.Converter;

import java.util.*;
import java.util.stream.Stream;

import static agh.ics.oop.model.MapDirection.*;

public abstract class GrassField extends AbstractWorldMap{
    protected final Map<Vector2d, Grass> grasses;
    protected final int width;
    protected final int height;
    private final int grassCount;
    protected final Set<Integer> preferredSet = new HashSet<>(); //na indeksach zeby wydobyc wspolrzedne mozna uzywac Converter
    protected final Set<Integer> unpreferredSet = new HashSet<>();
    private final List<Vector2d> posList;
    protected final List<List<Integer>> availableIdxs;
    public static final List<Vector2d> ADJACENT = Arrays.asList(NORTH_UNIT_VECTOR, SOUTH_UNIT_VECTOR, WEST_UNIT_VECTOR, EAST_UNIT_VECTOR, SOUTHEAST_UNIT_VECTOR, SOUTHWEST_UNIT_VECTOR, NORTHWEST_UNIT_VECTOR, NORTHEAST_UNIT_VECTOR);
    protected final CumulativePreferences cumulativePrefs;
    private final Config config = Config.getInstance();
    private final int GRASSES_AMOUNT = config.getInt("GRASSES_AMOUNT");
    private final int MAP_WIDTH = config.getInt("MAP_WIDTH");


    public GrassField(int grassCount, int width, int height) {
        this.grasses = new HashMap<Vector2d, Grass>();
        this.grassCount = grassCount;
        this.width = width;
        this.height = height;
        this.posList = this.createPosList();
        this.cumulativePrefs = new CumulativePreferences(this);
        this.availableIdxs = new ArrayList<>(); //lista list: preferred, unpreferred. pomocnicze - tylko dla randomPositionGenerator
        availableIdxs.add(0, new ArrayList<Integer> (preferredSet));
        availableIdxs.add(1, new ArrayList<Integer> (unpreferredSet));
        plantingGrasses(GRASSES_AMOUNT);
    }


    private List<Vector2d> createPosList(){
        List<Vector2d> posList = new ArrayList<Vector2d>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                posList.add(new Vector2d(j, i));
            }
        }
        return posList;
    }

    public List<List<Integer>> getAvailableIdxs(){
        return availableIdxs;
    }


    public abstract void eatenGrassProcedure(Vector2d position);

    public abstract void plantingGrasses(int grassCount);

    public void transferToPreferred(Vector2d position){
        int idx = Converter.convertToIdx(position, width);
        preferredSet.add(idx); //jesli jeszcze nie ma
        unpreferredSet.remove(idx); //jesli tam byl
    }

    public void transferToUnpreferred(Vector2d position){
        int idx = Converter.convertToIdx(position, width);
        unpreferredSet.add(idx);
        preferredSet.remove(idx);
    }

    protected void updatePreferences(Vector2d newGrassPos, Set<Integer> preferredSet, Set<Integer> unpreferredSet){ //dodaj do indeksów sąsiadow tej pozycji, usun samą pozycje
        int newGrassIdx = Converter.convertToIdx(newGrassPos, width);
        preferredSet.remove(newGrassIdx); //usuwaj pozycje na ktorej dodano trawe
        unpreferredSet.remove(newGrassIdx); //to samo ale jesli jest w unpreferred
        cumulativePrefs.incrementAdjacentTo(newGrassPos); //dodaj sasiadow do preferred i usun z unpreferred
    }

    public List<Vector2d> getPosList(){
        return this.posList;
    }



    @Override
    public boolean canMoveTo(Vector2d position) { //validator, nie mozemy wyjsc za biegun
        return position.getY() != height && position.getY() != -1;
    }



    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grasses.containsKey(position);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        if (animals.get(position) != null){ //priorytet animals
            if(animals.get(position).size()!=0){
                return super.objectAt(position);
            }
            return grasses.get(position);
        }
        return grasses.get(position);
    }

    public Vector2d findUpperRightBoundary() {
        Vector2d currUpperRight = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Vector2d position : animals.keySet()) {
            currUpperRight = currUpperRight.upperRight(position);
        }
        for (Vector2d position : grasses.keySet()) {
            currUpperRight = currUpperRight.upperRight(position);
        }
        return currUpperRight;
    }

    public Vector2d findLowerLeftBoundary() {
        Vector2d currLowerLeft = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Vector2d position : animals.keySet()) {
            currLowerLeft = currLowerLeft.lowerLeft(position);
        }
        for (Vector2d position : grasses.keySet()) {
            currLowerLeft = currLowerLeft.lowerLeft(position);
        }
        return currLowerLeft;
    }



    @Override
    public Boundary getCurrentBounds(){
        return new Boundary(new Vector2d(0,0), new Vector2d(width, height));
    }

    public List<Vector2d> getPrefferedPositions(){
        return preferredSet.stream().map(idx -> Converter.convertFromIdx(idx, MAP_WIDTH)).toList();
    }

    public Map<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void setAnimals(Map<Vector2d, List<Animal>> animals){ //rozkminka...
        this.animals = animals;
    }

    public Object getGrassAt(Vector2d position){
        return grasses.get(position);
    }

    public List<Vector2d> getFreeFields(){
        List<Vector2d> freeFields = new ArrayList<Vector2d>();
        for (Vector2d pos : posList){
            if (!isOccupied(pos)){
                freeFields.add(pos);
            }
        }
        return freeFields;
    }
}
