package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class Converter {
    public static int convertToIdx(Vector2d point, int width) {
        return point.getY()*width + point.getX();
    }
    public static Vector2d convertFromIdx(int idx, int width) {
        return new Vector2d(idx%width, idx/width);
    }
}
