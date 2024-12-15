package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST;
    public static final Vector2d NORTH_UNIT_VECTOR = new Vector2d(0, 1);
    public static final Vector2d SOUTH_UNIT_VECTOR = new Vector2d(0, -1);
    public static final Vector2d EAST_UNIT_VECTOR = new Vector2d(1, 0);
    public static final Vector2d WEST_UNIT_VECTOR = new Vector2d(-1, 0);
    public static final String[] STRING_DIRECTIONS = { "NORTH", "SOUTH",  "WEST", "EAST"};
    public String toString() {
        return switch(this){
            case NORTH -> STRING_DIRECTIONS[0];
            case SOUTH -> STRING_DIRECTIONS[1];
            case WEST -> STRING_DIRECTIONS[2];
            case EAST -> STRING_DIRECTIONS[3];
        };
    }
    public MapDirection next() {
        return switch(this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
        };
    }
    public MapDirection previous() {
        return switch(this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
        };
    }
    public Vector2d toUnitVector() {
        return switch(this){
            case NORTH -> NORTH_UNIT_VECTOR;
            case SOUTH -> SOUTH_UNIT_VECTOR;
            case WEST -> WEST_UNIT_VECTOR;
            case EAST -> EAST_UNIT_VECTOR;
        };
    }
}
