package agh.ics.oop.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnimalFamilyTree {
    private final Map<Integer, Set<Integer>> descendants = new HashMap<>(); // zamysl jest taki ze klucz to id rodzica, a wartosc to zbior id dzieci, drzewo genealogiczne to taki troche dag

    public void registerParentChild(int parentId, int childId) {
        if (parentId == childId){
            throw new IllegalArgumentException("Parent id and child id are the same");
        }else{
        descendants.computeIfAbsent(parentId, k -> new HashSet<>()).add(childId);
        }
    }

    public Set<Integer> getDescendants(int animalId) {
        Set<Integer> allDescendants = new HashSet<>();
        getDescendantsRecursive(animalId, allDescendants);
        return allDescendants;
    }

    private void getDescendantsRecursive(int animalId, Set<Integer> allDescendants) {
        if (!descendants.containsKey(animalId)) return;

        for (int childId : descendants.get(animalId)) {
            if (allDescendants.add(childId)) {
                getDescendantsRecursive(childId, allDescendants);
            }
        }
    }

    public int getDescendantsCount(int animalId) {
        return getDescendants(animalId).size();
    }
}
