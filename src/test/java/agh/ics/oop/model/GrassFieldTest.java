package agh.ics.oop.model;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GrassFieldTest {
    @Test
    void countOfGrassElements() {
        int grassCount = 10;

        GrassField grassField = new GrassField(grassCount);

        assertEquals(grassCount, grassField.getGrasses().size());
    }
    @Test
    void grassPositioningWithinBounds() {
        int grassCount = 10;
        GrassField grassField = new GrassField(grassCount);
        int maxBound = (int) Math.sqrt(10 * grassCount);


        for (Vector2d grassPosition : grassField.getGrasses().keySet()) {
            assertTrue(grassPosition.follows(new Vector2d(0, 0)));
            assertTrue(grassPosition.precedes(new Vector2d(maxBound - 1, maxBound - 1)));
        }
    }

    @Test
    void placingAnimalOnEmptyGrassField() {
        GrassField grassField = new GrassField(0);
        Animal animal = new Animal(new Vector2d(2, 2));
        boolean result = false;
        try {
            result = grassField.place(animal);
        }
        catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }

        assertTrue(result);
        assertEquals(animal, grassField.objectAt(new Vector2d(2, 2)));
    }
    @Test
    void placingAnimalOnOccupiedGrassField() {
        GrassField grassField = new GrassField(0);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        try {
            grassField.place(animal1);
            grassField.place(animal2);
            fail("IncorrectPositionException expected");
        }
        catch (IncorrectPositionException e) {
            assertEquals(animal1, grassField.objectAt(new Vector2d(2, 2)));
        }

    }

    @Test
    void placingAnimalOnPositionWithGrass() {
        GrassField grassField = new GrassField(0);
        grassField.getGrasses().put(new Vector2d(2,2), new Grass(new Vector2d(2, 2)));
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        try {
            grassField.place(animal1);
            grassField.place(animal2);
            fail("IncorrectPositionException expected");
        }
        catch (IncorrectPositionException e) {
            assertEquals(animal1, grassField.objectAt(new Vector2d(2, 2)));
        }

    }
    @Test
    void movingAnimalThroughGrasses() {
        GrassField grassField = new GrassField(0);
        Animal animal = new Animal(new Vector2d(2, 2));
        Vector2d pos1 = new Vector2d(2, 2);
        Vector2d pos2 = new Vector2d(3, 2);
        Vector2d pos3 = new Vector2d(4, 2);
        Grass grass1 = new Grass(pos1);
        Grass grass2 = new Grass(pos2);
        Grass grass3 = new Grass(pos3);
        grassField.getGrasses().put(new Vector2d(2, 2), new Grass(new Vector2d(2, 2)));
        grassField.getGrasses().put(new Vector2d(3, 2), new Grass(new Vector2d(3, 2)));
        grassField.getGrasses().put(new Vector2d(4, 2), new Grass(new Vector2d(4, 2)));

        try {
            grassField.place(animal);
        }
        catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }
        grassField.move(animal, MoveDirection.RIGHT);
        grassField.move(animal, MoveDirection.FORWARD);
        grassField.move(animal, MoveDirection.FORWARD);
        grassField.move(animal, MoveDirection.FORWARD);

        assertEquals(new Vector2d(5, 2), animal.getPos());
        assertEquals(new Vector2d(2,2), grass1.getPos());
        assertEquals(new Vector2d(3,2), grass2.getPos());
        assertEquals(new Vector2d(4,2), grass3.getPos());
    }

    @Test
    void mapBoundariesFinding() {
        GrassField grassField = new GrassField(0);
        try {
            grassField.place(new Animal(new Vector2d(1, 2)));
            grassField.place(new Animal(new Vector2d(4, 5)));
            grassField.place(new Animal(new Vector2d(101, 5)));
        } catch (IncorrectPositionException e) {
            fail("IncorrectPositionException not expected");
        }
        grassField.getGrasses().put(new Vector2d(0, 0), new Grass(new Vector2d(0, 0)));
        grassField.getGrasses().put(new Vector2d(6, 6), new Grass(new Vector2d(6, 6)));
        grassField.getGrasses().put(new Vector2d(100,100), new Grass(new Vector2d(100, 100)));

        Vector2d lowerLeft = grassField.findLowerLeftBoundary();
        Vector2d upperRight = grassField.findUpperRightBoundary();

        assertEquals(new Vector2d(0, 0), lowerLeft);
        assertEquals(new Vector2d(101, 100), upperRight);
    }
}
