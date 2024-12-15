package agh.ics.oop.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class MapDirectionTest {
    @Test
    public void nextAllDirections() {
        MapDirection east = MapDirection.EAST;
        MapDirection north = MapDirection.NORTH;
        MapDirection south = MapDirection.SOUTH;
        MapDirection west = MapDirection.WEST;

        assertEquals(MapDirection.SOUTH, east.next());
        assertEquals(MapDirection.EAST, north.next());
        assertEquals(MapDirection.WEST, south.next());
        assertEquals(MapDirection.NORTH, west.next());
    }
    @Test
    public void previousAllDirections() {
        MapDirection east = MapDirection.EAST;
        MapDirection north = MapDirection.NORTH;
        MapDirection south = MapDirection.SOUTH;
        MapDirection west = MapDirection.WEST;

        assertEquals(MapDirection.NORTH, east.previous());
        assertEquals(MapDirection.WEST, north.previous());
        assertEquals(MapDirection.SOUTH, west.previous());
        assertEquals(MapDirection.EAST, south.previous());
    }
}
