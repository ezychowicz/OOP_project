package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.CopulationFailedException;
import agh.ics.oop.model.util.Config;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static agh.ics.oop.WorldGUI.*;

public class Copulation {
    private final Vector2d position;
    private final GrassField grassField;
    private final AnimalFamilyTree familyTree;
    private final Randomizer randomizer;

    Config config = Config.getInstance();
    private final int GENOME_LENGTH = config.getInt("GENOME_LENGTH");
    private final int BREEDING_THRESHOLD = config.getInt("BREEDING_THRESHOLD");
    private final int BREEDING_COST = config.getInt("BREEDING_COST");
    public Copulation(Vector2d position, GrassField grassField, AnimalFamilyTree familyTree, Randomizer randomizer) {
        this.position = position;
        this.grassField = grassField;
        this.familyTree = familyTree;
        this.randomizer = randomizer;
    }

    public Copulation(Vector2d position, GrassField grassField, AnimalFamilyTree familyTree) {
        this(position, grassField, familyTree, new NonDeterministicRandomGenerator());
    }
    private List<Animal> pairPartners() { // Find the best candidates for copulation
        List<Animal> animalsAtPos = grassField.getAnimalsAt(position);
        Animal winner1 = grassField.resolveConflict(grassField.getAnimalsAt(position));
        List<Animal> animalsAtPosWithoutWinner = new ArrayList<>(animalsAtPos);
        animalsAtPosWithoutWinner.remove(winner1);
        Animal winner2 = grassField.resolveConflict(animalsAtPosWithoutWinner);
        return List.of(winner1, winner2);
    }

    public int findCrossoverIndex(int energyParent1, int energyParent2) { // Find end index of a left part of new genome
        int totalEnergy = energyParent1 + energyParent2;
        if (totalEnergy == 0) {
            throw new IllegalArgumentException("Energy values cannot both be zero.");
        }
        double parent1Ratio = (double) energyParent1 / totalEnergy;
        int crossoverIndex = (int) Math.round(parent1Ratio * GENOME_LENGTH);
        return Math.min(crossoverIndex, GENOME_LENGTH - 1);
    }

    private Map<Integer, Integer> createMutation() { // Formulate a mutation of genome
        Map<Integer, Integer> mutationRecipe = new HashMap<>();
        int toMutateCnt = randomizer.nextInt(GENOME_LENGTH + 1);
        if (toMutateCnt == 0) {
            return mutationRecipe;
        }
        List<Integer> allIndices = IntStream.range(0, GENOME_LENGTH)
                .boxed()
                .collect(Collectors.toList());
        randomizer.shuffle(allIndices);
        for (int i = 0; i < toMutateCnt; i++) {
            mutationRecipe.put(allIndices.get(i), randomizer.nextInt(8));
        }
        return mutationRecipe;
    }


    public Animal copulate() throws CopulationFailedException { // Main procedure
        List<Animal> partners = pairPartners();
        if (partners.getFirst().getEnergy() > BREEDING_THRESHOLD && partners.getLast().getEnergy() > BREEDING_THRESHOLD) {
            int crossIdx = findCrossoverIndex(partners.getFirst().getEnergy(), partners.getLast().getEnergy());
            List<Integer> newGenome = new ArrayList<>();
            Animal dominant = partners.getFirst();
            Animal recessive = partners.getLast();
            int leftOrRight = randomizer.nextInt(2);
            if (leftOrRight == 0) { // if leftOrRight=0: For dominating animal we take left side of genome
                newGenome.addAll(dominant.getGenome().subList(0, crossIdx));
                newGenome.addAll(recessive.getGenome().subList(crossIdx, GENOME_LENGTH));
            } else { // right side for dominating
                newGenome.addAll(recessive.getGenome().subList(0, crossIdx));
                newGenome.addAll(dominant.getGenome().subList(crossIdx, GENOME_LENGTH));
            }

            // Mutations
            Map<Integer, Integer> mutationRecipe = createMutation();
            for (Integer gene : mutationRecipe.keySet()) {
                newGenome.set(gene, mutationRecipe.get(gene));
            }

            // After birth procedure
            Animal newborn = new Animal(position, dominant, recessive, newGenome);
            familyTree.registerParentChild(dominant.getId(), newborn.getId());
            dominant.childrenCntIncrement();
            familyTree.registerParentChild(recessive.getId(), newborn.getId());
            recessive.childrenCntIncrement();

            // Energy cost of copulation
            dominant.updateEnergy(-BREEDING_COST);
            recessive.updateEnergy(-BREEDING_COST);
            return newborn;
        } else {
            throw new CopulationFailedException("Animals do not have enough energy");
        }
    }
}
