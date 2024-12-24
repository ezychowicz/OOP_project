package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.GrassField.ADJACENT;
import static agh.ics.oop.model.MapDirection.*;

public class CumulativePreferences{
    private final GrassField grassField;
    private final int[][] preferences;
    public static final List<Vector2d> ADJACENT_NORTH;
    public static final List<Vector2d> ADJACENT_SOUTH;
    static {
        ADJACENT_NORTH = new ArrayList<>(ADJACENT);
        ADJACENT_SOUTH = new ArrayList<>(ADJACENT);

        ADJACENT_NORTH.remove(NORTH_UNIT_VECTOR);
        ADJACENT_NORTH.remove(NORTHEAST_UNIT_VECTOR);
        ADJACENT_NORTH.remove(NORTHWEST_UNIT_VECTOR);
        ADJACENT_SOUTH.remove(SOUTH_UNIT_VECTOR);
        ADJACENT_SOUTH.remove(SOUTHWEST_UNIT_VECTOR);
        ADJACENT_SOUTH.remove(SOUTHEAST_UNIT_VECTOR);
    }
    public CumulativePreferences(GrassField grassField) {
        this.grassField = grassField;
        this.preferences = new int[grassField.getCurrentBounds().upperRightBound().getX()][grassField.getCurrentBounds().upperRightBound().getY()];
    }
    public void incrementAdjacentTo(Vector2d position){
        List<Vector2d> adjacent = getAdjacent(position);
        for (Vector2d adj : adjacent){
            Vector2d checkedPos = position.add(adj, grassField.getWidth());
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)){
                grassField.transferToPreferred(checkedPos);
            }
            preferences[checkedPos.getX()][checkedPos.getY()]++;
        }
    }

    public void decrementAdjacentTo(Vector2d position){
        List<Vector2d> adjacent = getAdjacent(position);

        for (Vector2d adj : adjacent){
            Vector2d checkedPos = position.add(adj, grassField.getWidth());
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)) {
                grassField.transferToUnpreferred(checkedPos);
            }
            preferences[checkedPos.getX()][checkedPos.getY()]--;
            if (preferences[checkedPos.getX()][checkedPos.getY()] < 0) {
                throw new ArithmeticException();
            }
        }
    }
    private List<Vector2d> getAdjacent(Vector2d position){
        List<Vector2d> adjacent;
        if (position.getY() == grassField.getHeight() - 1){
            adjacent = ADJACENT_NORTH;
        }else if (position.getY() == 0){
            adjacent = ADJACENT_SOUTH;
        }else{
            adjacent = ADJACENT;
        }
        return adjacent;
    }

    public int getPreferenceScoreAtPos(Vector2d position){
        return preferences[position.getX()][position.getY()];
    }
}
