package agh.ics.oop.model;

import static agh.ics.oop.model.GrassField.ADJACENT;

public class CumulativePreferations {

    private final GrassField grassField;
    private final int[][] preferences;

    public CumulativePreferations(GrassField grassField) {
        this.grassField = grassField;
        this.preferences = new int[grassField.getCurrentBounds().upperRightBound().getX()][grassField.getCurrentBounds().upperRightBound().getY()];

    }
    public void incrementAdjacentTo(Vector2d position){
        for (Vector2d adj : ADJACENT){
            Vector2d checkedPos = position.add(adj);
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)){
                grassField.transferToPreferred(checkedPos);
            }
            preferences[checkedPos.getX()][checkedPos.getY()]++;
        }
    }

    public void decrementAdjacentTo(Vector2d position){
        for (Vector2d adj : ADJACENT){
            Vector2d checkedPos = position.add(adj);
            if (preferences[checkedPos.getX()][checkedPos.getY()] == 0 && !grassField.getGrasses().containsKey(checkedPos)) {
                grassField.transferToUnpreferred(checkedPos);
            }
            preferences[checkedPos.getX()][checkedPos.getY()]--;
            if (preferences[checkedPos.getX()][checkedPos.getY()] < 0) {
                throw new ArithmeticException();
            }
        }
    }

    public int getPreferenceScoreAtPos(Vector2d position){
        return preferences[position.getX()][position.getY()];
    }
}
