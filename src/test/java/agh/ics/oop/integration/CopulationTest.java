package agh.ics.oop.integration;

import agh.ics.oop.WorldGUI;
import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.CopulationFailedException;
import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static agh.ics.oop.WorldGUI.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

class CopulationTest {
    private int GENOME_LENGTH;
    private int BREEDING_COST;
    private int BREEDING_THRESHOLD;
    private Copulation copulation;
    private GrassField grassField;
    private AnimalFamilyTree familyTree;
    private Vector2d position;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException{
        Config.resetForTests();
        Config config = Config.getInstance();
        config.initialize("copulationtest.properties");
        try {
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        position = new Vector2d(2, 3);
        grassField = new NormalGrassField(10,10,10);
        familyTree = new AnimalFamilyTree();
        GENOME_LENGTH = Config.getInstance().getInt("GENOME_LENGTH");
        BREEDING_COST = Config.getInstance().getInt("BREEDING_COST");
        BREEDING_THRESHOLD = Config.getInstance().getInt("BREEDING_THRESHOLD");
    }

    @Test
    void copulationWhenAnimalsHaveSufficientEnergyTest() throws CopulationFailedException {
        // Given
        Animal parent1 = new Animal(position);
        Animal parent2 = new Animal(position);

        grassField.addAnimalAtPos(grassField.getAnimals(), parent1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), parent2, position);

        parent1.updateEnergy(BREEDING_THRESHOLD + 1);
        parent2.updateEnergy(BREEDING_THRESHOLD + 1);
        copulation = new Copulation(position, grassField, familyTree);
        // When
        Animal newborn = copulation.copulate();

        // Then
        assertNotNull(newborn);
        assertEquals(BREEDING_THRESHOLD + 1 - BREEDING_COST, parent1.getEnergy());
        assertEquals(BREEDING_THRESHOLD + 1 - BREEDING_COST, parent2.getEnergy());
        assertEquals(newborn.getGenome().size(), GENOME_LENGTH);
        assertEquals(1, parent1.getChildrenCnt());
        assertEquals(1, parent2.getChildrenCnt());
    }

    @Test
    void animalWithInsufficientEnergyTest() {
        // Given
        Animal parent1 = new Animal(position);
        Animal parent2 = new Animal(position);
        parent1.updateEnergy(Math.min(parent1.getEnergy(), BREEDING_THRESHOLD - 1));

        grassField.addAnimalAtPos(grassField.getAnimals(), parent1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), parent2, position);
        copulation = new Copulation(position, grassField, familyTree);
        // When & Then
        CopulationFailedException exception = assertThrows(CopulationFailedException.class, () -> {
            copulation.copulate();
        });
        assertEquals("Animals do not have enough energy", exception.getMessage());
    }

    @Test
    void findCrossoverIndexTest() {
        // Given
        int energyParent1 = BREEDING_COST;
        int energyParent2 = 4*BREEDING_COST;
        copulation = new Copulation(position, grassField, familyTree);

        // When
        int crossoverIndex = copulation.findCrossoverIndex(energyParent1, energyParent2);

        // Then
        double expectedRatio = (double) energyParent1 / (energyParent1 + energyParent2);
        int expectedIndex = (int) Math.round(expectedRatio * GENOME_LENGTH);
        assertEquals(expectedIndex, crossoverIndex);
    }

    @Test
    void mutationCreationTest() throws CopulationFailedException {
        // Given
        Animal parent1 = new Animal(position);
        Animal parent2 = new Animal(position);

        grassField.addAnimalAtPos(grassField.getAnimals(), parent1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), parent2, position);
        FixedRandomGenerator randomizer = new FixedRandomGenerator(List.of(0, 3, 1, 2, 3), List.of(List.of(0,4)));
        // czyli: lewą strone bierzemy od dominującego (bez znaczenia dla testu)
        // 3 geny mutowane, na indeksach 4,1,2, nowe wartości dla tych indeksów to: 1, 2, 3
        copulation = new Copulation(position, grassField, familyTree, randomizer);
        parent1.updateEnergy(BREEDING_THRESHOLD + 1);
        parent2.updateEnergy(BREEDING_THRESHOLD + 1);

        // When
        List<Integer> mutationResult = copulation.copulate().getGenome();

        // Then
        assertEquals(1, mutationResult.get(4));
        assertEquals(2, mutationResult.get(1));
        assertEquals(3, mutationResult.get(2));
    }
//    @Test
//    void pairingPriorityTest() {
//        // Given
//        Animal parent1 = new Animal(position);
//        Animal parent2 = new Animal(position);
//        Animal parent3 = new Animal(position);
//        Animal parent4 = new Animal(position);
//        Animal parent5 = new Animal(position);
//        grassField.addAnimalAtPos(grassField.getAnimals(), parent1, position);
//        grassField.addAnimalAtPos(grassField.getAnimals(), parent2, position);
//        grassField.addAnimalAtPos(grassField.getAnimals(), parent3, position);
//        grassField.addAnimalAtPos(grassField.getAnimals(), parent4, position);
//        grassField.addAnimalAtPos(grassField.getAnimals(), parent5, position);
//
//        // When
//        List<Animal> partners = copulation.pairPartners();
//
//        // Then
//        assertEquals(2, partners.size());
//        assertTrue(partners.contains(parent1) || partners.contains(parent2));
//        assertTrue(partners.contains(parent2) || partners.contains(parent3));
//    }
}