package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class CrazyBehaviourTest {
    private GrassField grassField;
    private CrazyBehaviour crazyBehaviour;
    private Animal animal;
    @BeforeEach
    void setUp() {
        Config.resetForTests();
        Config config = Config.getInstance();
        config.initialize("crazybehaviourtest.properties");
        try {
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        animal = new Animal(new Vector2d(2, 3),List.of(0, 1, 2, 3, 4, 5));
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
        assertEquals(List.of(0,0,0,0,0,0), indices);
    }
}
