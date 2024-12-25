package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    SOUTH,
    EAST,
    WEST,
    NORTH_EAST,
    NORTH_WEST,
    SOUTH_EAST,
    SOUTH_WEST;
    public static final Vector2d NORTH_UNIT_VECTOR = new Vector2d(0, 1);
    public static final Vector2d SOUTH_UNIT_VECTOR = new Vector2d(0, -1);
    public static final Vector2d EAST_UNIT_VECTOR = new Vector2d(1, 0);
    public static final Vector2d WEST_UNIT_VECTOR = new Vector2d(-1, 0);
    public static final Vector2d NORTHWEST_UNIT_VECTOR = new Vector2d(-1, 1);
    public static final Vector2d SOUTHWEST_UNIT_VECTOR = new Vector2d(-1, -1);
    public static final Vector2d NORTHEAST_UNIT_VECTOR = new Vector2d(1, 1);
    public static final Vector2d SOUTHEAST_UNIT_VECTOR = new Vector2d(1, -1);
    public static final String[] STRING_DIRECTIONS = { "NORTH", "SOUTH",  "WEST", "EAST", "NORTHWEST", "NORTHEAST","SOUTHEAST", "SOUTHWEST"};
    public String toString() {
        return switch(this){
            case NORTH -> STRING_DIRECTIONS[0];
            case SOUTH -> STRING_DIRECTIONS[1];
            case WEST -> STRING_DIRECTIONS[2];
            case EAST -> STRING_DIRECTIONS[3];
            case NORTH_WEST -> STRING_DIRECTIONS[4];
            case NORTH_EAST -> STRING_DIRECTIONS[5];
            case SOUTH_EAST -> STRING_DIRECTIONS[6];
            case SOUTH_WEST -> STRING_DIRECTIONS[7];
        };
    }
    public MapDirection next() {
        return switch(this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }
    public MapDirection previous() {
        return switch(this) {
            case NORTH -> WEST;
            case NORTH_WEST -> WEST;
            case WEST -> SOUTH_WEST;
            case SOUTH_WEST -> SOUTH;
            case SOUTH -> SOUTH_EAST;
            case SOUTH_EAST -> EAST;
            case EAST -> NORTH_EAST;
            case NORTH_EAST -> NORTH;

        };
    }
    public Vector2d toUnitVector() {
        return switch(this){
            case NORTH -> NORTH_UNIT_VECTOR;
            case SOUTH -> SOUTH_UNIT_VECTOR;
            case WEST -> WEST_UNIT_VECTOR;
            case EAST -> EAST_UNIT_VECTOR;
            case NORTH_EAST -> NORTHEAST_UNIT_VECTOR;
            case NORTH_WEST -> NORTHWEST_UNIT_VECTOR;
            case SOUTH_EAST -> SOUTHEAST_UNIT_VECTOR;
            case SOUTH_WEST -> SOUTHWEST_UNIT_VECTOR;
        };
    }
}
