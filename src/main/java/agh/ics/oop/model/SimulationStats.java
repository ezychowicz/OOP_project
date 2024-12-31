package agh.ics.oop.model;

import agh.ics.oop.Day;
import agh.ics.oop.model.Animal;

import java.util.List;

public class SimulationStats {

    private final Day day;

    public SimulationStats(Day day) {
        this.day = day;
    }

    public Animal getAnimalWithId(int id) {
        for (List<Animal> animalsAtPos : day.getAnimals().values()) {
            for (Animal animal : animalsAtPos) {
                if (animal.getId() == id) {
                    return animal;
                }
            }
        }
        return null;
    }

    public void displayAnimalStats() {
        Animal trackedAnimal = getAnimalWithId(0);
        if (trackedAnimal != null) {
            System.out.println("Animal ID: " + trackedAnimal.getId());
            System.out.println("Position: " + trackedAnimal.getPos());
            System.out.println("Energy: " + trackedAnimal.getEnergy());
            System.out.println("Days Alive: " + trackedAnimal.getDaysOld());
            System.out.println("Children Count: " + trackedAnimal.getChildrenCnt());
            System.out.println("Genome: " + trackedAnimal.getGenome());
            System.out.println("Active Genome Part: " + trackedAnimal.getGenomeAtIdx(trackedAnimal.getGenomeIdx()));
        } else {
            System.out.println("Animal with ID 0 not found.");
        }
    }
}