package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.WorldGUI.GENOME_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

public class CrazyBehaviourTest {
    private GrassField grassField;
    private CrazyBehaviour crazyBehaviour;
    private Vector2d position;
    private Animal animal;
    @BeforeEach
    void setUp() {
        GENOME_LENGTH = 7;
        position = new Vector2d(2, 3);
        animal = new Animal(position, null, null, List.of(0, 1, 2, 3, 4, 5));
    }

    @Test
    void crazyBehaviourDrawsDifferentGeneTest() {
        // Given
        Randomizer randomizer = new FixedRandomGenerator(List.of(6,5,4,3,2,1),null);
        // kolejnosc genów nastepująca: 0, 6, 5, 4, 3, 2,`1
        CrazyBehaviour crazyBehaviour = new CrazyBehaviour(randomizer, 0.0);

        // When
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int nextGeneIdx = crazyBehaviour.nextGeneIdx(animal);
            indices.add(nextGeneIdx);
        }

        // Then
        assertEquals(List.of(6, 5, 4, 3, 2, 1), indices);
    }

    @Test
    void crazyBehaviourUsesBoringBehaviourTest() {
        // Given
        Randomizer randomizer = new FixedRandomGenerator(List.of(6, 5, 4, 3, 2, 1), null);
        CrazyBehaviour crazyBehaviour = new CrazyBehaviour(randomizer, 1);

        // When
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int nextGeneIdx = crazyBehaviour.nextGeneIdx(animal);
            indices.add(nextGeneIdx);
        }

        // Then
        assertEquals(List.of(1,1,1,1,1,1), indices);
    }
}
