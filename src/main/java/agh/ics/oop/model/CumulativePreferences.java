package agh.ics.oop.model;

import java.util.List;

public class CumulativePreferences{
    private final GrassField grassField;
    private final int[][] preferences;

    public CumulativePreferences(GrassField grassField) {
        this.grassField = grassField;
        this.preferences = new int[grassField.getCurrentBounds().upperRightBound().getX()][grassField.getCurrentBounds().upperRightBound().getY()];
    }

    public void incrementAdjacentTo(Vector2d position){
        List<Vector2d> adjacent = grassField.getAdjacent(position);
        for (Vector2d adj : adjacent){
            Vector2d checkedPos = position.add(adj, grassField.getWidth());
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)){
                grassField.transferToPreferred(checkedPos);
            }
            preferences[checkedPos.getX()][checkedPos.getY()]++;
        }
    }

    public void decrementAdjacentTo(Vector2d position){
        List<Vector2d> adjacent = grassField.getAdjacent(position);

        for (Vector2d adj : adjacent){
            Vector2d checkedPos = position.add(adj, grassField.getWidth());
            preferences[checkedPos.getX()][checkedPos.getY()]--;
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)) {
                grassField.transferToUnpreferred(checkedPos);
            }

            if (preferences[checkedPos.getX()][checkedPos.getY()] < 0) {
                throw new ArithmeticException();
            }
        }
    }

    public int getPreferenceScoreAtPos(Vector2d position){
        return preferences[position.getX()][position.getY()];
    }
}
