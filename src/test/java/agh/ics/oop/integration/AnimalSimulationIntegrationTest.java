package agh.ics.oop.integration;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class AnimalSimulationIntegrationTest {
    @Test
    public void isUpperBoundarySolid(){
        List<Vector2d> positions = List.of(new Vector2d(2,4));
        List<MoveDirection> directions = List.of(MoveDirection.FORWARD);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(2,4);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(sim.getWorldMap().isOccupied(desiredEndPos));
    }

    @Test
    public void isLowerBoundarySolid(){
        List<Vector2d> positions = List.of(new Vector2d(2,0));
        List<MoveDirection> directions = List.of(MoveDirection.BACKWARD);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(2,0);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(sim.getWorldMap().isOccupied(desiredEndPos));
    }

    @Test
    public void isLeftBoundarySolid(){
        List<Vector2d> positions = List.of(new Vector2d(0,2));
        List<MoveDirection> directions = List.of(MoveDirection.LEFT, MoveDirection.FORWARD);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(0,2);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(sim.getWorldMap().isOccupied(desiredEndPos));
    }

    @Test
    public void isRightBoundarySolid(){
        List<Vector2d> positions = List.of(new Vector2d(4,2));
        List<MoveDirection> directions = List.of(MoveDirection.RIGHT, MoveDirection.FORWARD);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(4,2);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(sim.getWorldMap().isOccupied(desiredEndPos));
    }

    @Test
    public void stationaryOrientationChanges(){
        //given
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> directions = List.of(
                MoveDirection.RIGHT,
                MoveDirection.RIGHT,
                MoveDirection.LEFT,
                MoveDirection.LEFT,
                MoveDirection.LEFT,
                MoveDirection.LEFT,
                MoveDirection.LEFT);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(2,2);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(worldMap.isOccupied(desiredEndPos));
        MapDirection endingDirection = endingPositionContent.getDirection();
        assertEquals(MapDirection.EAST, endingDirection);
    }

    @Test
    public void AllValidMovement(){
        List<Vector2d> positions = List.of(new Vector2d(2,2));
        List<MoveDirection> directions = List.of(
                MoveDirection.FORWARD,
                MoveDirection.RIGHT,
                MoveDirection.FORWARD,
                MoveDirection.LEFT,
                MoveDirection.FORWARD,
                MoveDirection.BACKWARD,
                MoveDirection.BACKWARD);
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        Vector2d desiredEndPos = new Vector2d(3,2);
        Animal endingPositionContent = (Animal) sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(worldMap.isOccupied(desiredEndPos));
        MapDirection endingDirection = endingPositionContent.getDirection();
        assertEquals(MapDirection.NORTH, endingDirection);
    }

    @Test
    public void collidingAnimals(){
        List<Vector2d> positions = List.of(new Vector2d(2,2),new Vector2d(0,2));
        List<MoveDirection> directions = List.of(
                MoveDirection.LEFT,
                MoveDirection.RIGHT,
                MoveDirection.FORWARD,
                MoveDirection.FORWARD,
                MoveDirection.FORWARD
                );
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        List<Vector2d> desiredEndPos = List.of(new Vector2d(1,2), new Vector2d(0,2));
        List<MapDirection> desiredEndingDirections = List.of(MapDirection.WEST,MapDirection.EAST);
        List<Animal> endingPositionContent = List.of((Animal) sim.getWorldMap().objectAt(desiredEndPos.getFirst()),(Animal) sim.getWorldMap().objectAt(desiredEndPos.get(1)));
        assertTrue(worldMap.isOccupied(desiredEndPos.getFirst()) && worldMap.isOccupied(desiredEndPos.get(1)));
        List<MapDirection> endingDirections = List.of(endingPositionContent.getFirst().getDirection(), endingPositionContent.get(1).getDirection());
        assertEquals(desiredEndingDirections, endingDirections);
    }

    @Test
    public void invalidPlacementOutOfMap(){
        List<Vector2d> positions = List.of(new Vector2d(2,0),new Vector2d(5,2), new Vector2d(-1,2));
        List<MoveDirection> directions = List.of(
                MoveDirection.FORWARD,
                MoveDirection.FORWARD,
                MoveDirection.FORWARD
        );
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        assertEquals(1, sim.getPositions().size());
        Vector2d desiredEndPos = new Vector2d(2,3);
        Animal endingPositionContent = (Animal)sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(worldMap.isOccupied(desiredEndPos));
        MapDirection endingDirection = endingPositionContent.getDirection();
        assertEquals(MapDirection.NORTH, endingDirection);
    }

    @Test
    public void invalidPlacementAlreadyOccupied(){
        List<Vector2d> positions = List.of(new Vector2d(2,0),new Vector2d(2,0), new Vector2d(2,0));
        List<MoveDirection> directions = List.of(
                MoveDirection.FORWARD,
                MoveDirection.FORWARD,
                MoveDirection.FORWARD
        );
        WorldMap worldMap = new RectangularMap(4, 4);

        Simulation sim = new Simulation(positions, directions, worldMap);
        sim.run();

        assertEquals(1, sim.getPositions().size());
        Vector2d desiredEndPos = new Vector2d(2,3);
        Animal endingPositionContent = (Animal)sim.getWorldMap().objectAt(desiredEndPos);
        assertTrue(worldMap.isOccupied(desiredEndPos));
        MapDirection endingDirection = endingPositionContent.getDirection();
        assertEquals(MapDirection.NORTH, endingDirection);
    }
}
