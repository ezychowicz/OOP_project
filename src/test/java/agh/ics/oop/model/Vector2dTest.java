package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void equalsVectorWithItself() {
        Vector2d v1 = new Vector2d(1,2);
        assertEquals(v1, v1, "vector is equal to itself");
    }
    @Test
    public void equalsWithSameVectors() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,2);
        assertEquals(v1, v2, "vectors with same cords are equal");
    }
    @Test
    public void equalsWithDifferentVectors() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertNotEquals(v1, v2, "vectors with different values cords are not equal");
    }
    @Test
    public void equalsWithNull() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = null;
        assertNotEquals(v1, v2, "not null vector is not equal null");
    }
    @Test
    public void equalsWithDifferentObject() {
        Vector2d v1 = new Vector2d(1,2);
        Object v2 = new Object();
        assertNotEquals(v1, v2, "non Vector2d class object is not equal to Vector2d object");
    }


    @Test
    public void toStringWithTypicalVector() {
        Vector2d v1 = new Vector2d(1,2);
        assertEquals("(1, 2)", v1.toString(), "toString() method returns an incorrect string");
    }


    @Test
    public void precedesWithBothCordsHigherInOneVector() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertTrue(v1.precedes(v2), "v1 precedes v2, as both cords are lower");
        assertFalse(v2.precedes(v1), "v2 does not precede v1, as both of its cords are higher");
    }
    @Test
    public void precedesWithEqualVectors() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,2);
        assertTrue(v1.precedes(v2), "v1 precedes v2, as both cords are equal");
    }
    @Test
    public void shouldNotPrecedeWhenOnlyOneCoordinateIsLower() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(0,3);
        assertFalse(v1.precedes(v2), "x cord is higher in v1");
        assertFalse(v2.precedes(v1), "y cord is higher in v2");
    }


    @Test
    public void followsWithEqualVectors() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1,2);
        assertTrue(v2.follows(v1), "v2 follows v1, as both cords are equal");
    }
    @Test
    public void followsWithBothCordsHigherInOneVector() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertTrue(v2.follows(v1), "v2 follows v1, as both cords are higher");
        assertFalse(v1.follows(v2), "v1 does not follow v2, as both of its cords are lower");
    }
    @Test
    public void shouldNotFollowWhenOnlyOneCoordinateIsHigher() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(0,3);
        assertFalse(v1.follows(v2), "y cord is lower in v1");
        assertFalse(v2.follows(v1), "x cord is lower in v2");
    }

    @Test
    public void upperRightWithOneVectorPrecedingAnother(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertEquals(new Vector2d(1000,2000), v1.upperRight(v2));
        assertEquals(new Vector2d(1000,2000), v2.upperRight(v1));
    }
    @Test
    public void upperRightWithNonPrecedingVectors() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(0, 3);
        assertEquals(new Vector2d(1, 3), v1.upperRight(v2));
        assertEquals(new Vector2d(1, 3), v2.upperRight(v1));
    }
    @Test
    public void lowerLeftWithOneVectorPrecedingAnother(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertEquals(new Vector2d(1,2), v1.lowerLeft(v2));
        assertEquals(new Vector2d(1,2), v2.lowerLeft(v1));
    }
    @Test
    public void lowerLeftWithNonPrecedingVectors() {
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(0,3);
        assertEquals(new Vector2d(0,2), v1.lowerLeft(v2));
        assertEquals(new Vector2d(0,2), v2.lowerLeft(v1));
    }

    @Test
    public void addTypicalVectors(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertEquals(new Vector2d(1001,2002), v1.add(v2));
    }


    @Test
    public void subtractTypicalVectors(){
        Vector2d v1 = new Vector2d(1,2);
        Vector2d v2 = new Vector2d(1000,2000);
        assertEquals(new Vector2d(-999,-1998), v1.subtract(v2));
    }


    @Test
    public void oppositeTypicalVector(){
        Vector2d v1 = new Vector2d(1,2);
        assertEquals(new Vector2d(-1,-2), v1.opposite());
    }
}
