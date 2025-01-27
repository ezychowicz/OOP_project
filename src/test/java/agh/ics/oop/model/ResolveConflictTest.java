package agh.ics.oop.model;

import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResolveConflictTest {
    private Copulation copulation;
    private GrassField grassField;
    private Vector2d position;

    @BeforeEach
    void setUp(){
        Config.resetForTests();
        Config config = Config.getInstance();
        config.initialize("resolveconflicttest.properties");
        try {
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        position = new Vector2d(2, 3);
        grassField = new NormalGrassField(10,10,10);
    }

    @Test
    void energySortTest() {
        // Given
        Animal a1 = new Animal(position);
        a1.updateEnergy(1);
        Animal a2 = new Animal(position);
        a2.updateEnergy(2);
        Animal a3 = new Animal(position);
        a3.updateEnergy(3);
        Animal a4 = new Animal(position);
        a4.updateEnergy(4);
        Animal a5 = new Animal(position);
        a5.updateEnergy(5);
        grassField.addAnimalAtPos(grassField.getAnimals(),a1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a2, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a3, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a4, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a5, position);

        //When
        List<Animal> order = new ArrayList<Animal>();
        while (!grassField.getAnimalsAt(position).isEmpty()) {
            Animal winner = grassField.resolveConflict(grassField.getAnimalsAt(position));
            order.add(winner);
            grassField.getAnimalsAt(position).remove(winner);
        }

        //Then
        assertEquals(order, List.of(a5,a4,a3,a2,a1));
    }

    @Test
    void equalEnergyTest() {
        // Given
        Animal a1 = new Animal(position);
        Animal a2 = new Animal(position);
        Animal a3 = new Animal(position);
        Animal a4 = new Animal(position);
        Animal a5 = new Animal(position);
        Animal[] animals = {a1, a2, a3, a4, a5};
        for (int i = 0; i < animals.length; i++) {
            for (int j = i; j < animals.length; j++) {
                animals[j].daysOldIncrement();
            }
        }
        grassField.addAnimalAtPos(grassField.getAnimals(), a1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a2, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a3, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a4, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a5, position);

        //When
        List<Animal> order = new ArrayList<Animal>();
        while (!grassField.getAnimalsAt(position).isEmpty()) {
            Animal winner = grassField.resolveConflict(grassField.getAnimalsAt(position));
            order.add(winner);
            grassField.getAnimalsAt(position).remove(winner);
        }

        //Then
        assertEquals(order, List.of(a5,a4,a3,a2,a1));
    }

    @Test
    void equalEnergyAndEqualAgeTest(){
        // Given
        Animal a1 = new Animal(position);
        Animal a2 = new Animal(position);
        Animal a3 = new Animal(position);
        Animal a4 = new Animal(position);
        Animal a5 = new Animal(position);
        Animal[] animals = {a1, a2, a3, a4, a5};
        for (int i = 0; i < animals.length; i++) {
            for (int j = i; j < animals.length; j++) {
                animals[j].childrenCntIncrement();
            }
        }
        grassField.addAnimalAtPos(grassField.getAnimals(), a1, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a2, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a3, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a4, position);
        grassField.addAnimalAtPos(grassField.getAnimals(), a5, position);

        //When
        List<Animal> order = new ArrayList<Animal>();
        while (!grassField.getAnimalsAt(position).isEmpty()) {
            Animal winner = grassField.resolveConflict(grassField.getAnimalsAt(position));
            order.add(winner);
            grassField.getAnimalsAt(position).remove(winner);
        }

        //Then
        assertEquals(order, List.of(a5,a4,a3,a2,a1));
    }


}
