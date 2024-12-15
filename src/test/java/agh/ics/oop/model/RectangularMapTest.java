package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangularMapTest {
    @Test
    void isOnMapPositionsChecking() {
        int width = 10;
        int height = 5;

        RectangularMap map = new RectangularMap(width, height);

        assertTrue(map.isOnMap(new Vector2d(0, 0)));
        assertTrue(map.isOnMap(new Vector2d(10, 5)));
        assertFalse(map.isOnMap(new Vector2d(11, 5)));
        assertFalse(map.isOnMap(new Vector2d(10, 6)));
    }
    @Test
    void placingAnimalOnUnoccupiedPosition() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(2, 2));
        boolean result = false;
        try {
            result = map.place(animal);
        } catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }

        assertTrue(result);
        assertEquals(animal, map.objectAt(new Vector2d(2, 2)));
    }

    @Test
    void placingAnimalOnOccupiedPosition() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        try {
            map.place(animal1);
            map.place(animal2);
            fail("IncorrectPositionException not expected");
        }
        catch (IncorrectPositionException e) {
            assertEquals(animal1, map.objectAt(new Vector2d(2, 2)));
        }

    }

    @Test
    void movingAnimalInBounds() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(2, 2));
        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }

        map.move(animal, MoveDirection.FORWARD);

        assertEquals(new Vector2d(2, 3), animal.getPos());
        assertEquals(animal, map.objectAt(new Vector2d(2, 3)));
        assertNull(map.objectAt(new Vector2d(2, 2)));
    }

    @Test
    void movingAnimalOutsideOfMapBounds() {
        RectangularMap map = new RectangularMap(5, 5);
        Animal animal = new Animal(new Vector2d(5, 5));

        try {
            map.place(animal);
        } catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }
        map.move(animal, MoveDirection.FORWARD);

        assertEquals(new Vector2d(5, 5), animal.getPos());
        assertEquals(animal, map.objectAt(new Vector2d(5, 5)));
    }


}
