package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.CopulationFailedException;
import agh.ics.oop.model.util.Config;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {
    private GrassField grassField;
    private Day day;
    private Map<Vector2d, List<Animal>> animals;
    private AnimalBehaviour animalBehaviour;
    private final AnimalFamilyTree familyTree = new AnimalFamilyTree();

    @Test
    public void normalVariantTest(){
        Config.resetForTests();
        Config config = Config.getInstance();
        config.initialize("normalvarianttest.properties");
        try {
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        grassField = new NormalGrassField(10,10,10);
        Animal upper = new Animal(new Vector2d(5,7), null, null, List.of(4, 0,0,0,0,0,0));
        upper.updateEnergy(100);
        Animal lower = new Animal(new Vector2d(5,3), null, null, List.of(0, 0,0,0,0,0,0));
        lower.updateEnergy(100);
        Animal right = new Animal(new Vector2d(7,5), null, null, List.of(6, 0,0,0,0,0,0));
        Animal left = new Animal(new Vector2d(3,5), null, null, List.of(2, 0,0,0,0,0,0));
        grassField.addAnimalAtPos(grassField.getAnimals(), upper, new Vector2d(5,7));
        grassField.addAnimalAtPos(grassField.getAnimals(), lower, new Vector2d(5,3));
        grassField.addAnimalAtPos(grassField.getAnimals(), right, new Vector2d(7,5));
        grassField.addAnimalAtPos(grassField.getAnimals(), left, new Vector2d(3,5));
        animals = grassField.getAnimals();
        animalBehaviour = new NormalBehaviour();
        int dayCnt = 0;
        day = new Day(grassField, animalBehaviour);
        //Imitate day procedure
        while (dayCnt < 2){
            day.updateAnimalsState();
            animals = day.updateAnimalsPositions(animalBehaviour);
            grassField.setAnimals(animals);
            Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animals.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
                Vector2d position = entry.getKey();
                List<Animal> animalsAtPos = entry.getValue();

                if (animalsAtPos.size() >= 2) {
                    try {
                        Copulation copulation = new Copulation(position, grassField, familyTree);
                        Animal newborn = copulation.copulate();
                        animalsAtPos.add(newborn); // Dodajemy nowo narodzone zwierzę do listy

                        // Zaktualizuj listę zwierząt w mapie
                        animals.put(position, animalsAtPos); // Bez usuwania klucza, po prostu zaktualizuj listę

                    } catch (CopulationFailedException e) {
                        System.out.println("Kopulacja nie powiodła się dla pozycji: " + position);
                    }
                }
            }
            grassField.setAnimals(animals);
            dayCnt++;
        }
        List<Animal> animalsAtPos = grassField.getAnimalsAt(new Vector2d(5,5));
        assertEquals(100 + 10 - 2, upper.getEnergy(), "Wrong energy for upper animal");
        assertEquals(100 + 10 - 2, lower.getEnergy(), "Wrong energy for lower animal");
        assertEquals(20 - 2, right.getEnergy(), "Wrong energy for right animal");
        assertEquals(20 - 2, left.getEnergy(), "Wrong energy for left animal");
        assertEquals(5, animalsAtPos.size(), "Wrong copulation result");
    }

    @RepeatedTest(1000) //zawiera element randomizowany - wzrost roślin
    public void sprawlingJungleTest(){
        /**
         * The test idea is to check preferations state every day:
         * whether every position in preferredSet is really preferred (A)
         * whether every position in unpreferredSet is really unpreferred (B)
         * whether rest of positions are occupied by plants (Ω\(AUB))
         */
        Config.resetForTests();
        Config.resetForTests();
        Config config = Config.getInstance();
        config.initialize("sprawlingjungletest.properties");
        try {
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        grassField = new SprawlingJungle(10,10,10);

        animalBehaviour = new NormalBehaviour();
        int dayCnt = 0;
        day = new Day(grassField, animalBehaviour);
        //Imitate day procedure
        while (dayCnt < 3){
            grassField.plantingGrasses(Config.getInstance().getInt("GRASS_GROWTH_EACH_DAY"));


            List<Vector2d> preferred = grassField.getPreferredPositions();
            List<Vector2d> unpreferred = grassField.getUnpreferredPositions();
            List<Vector2d> rest = grassField.getPosList().stream()
                    .filter(pos -> !preferred.contains(pos) && !unpreferred.contains(pos))
                    .toList();

            // Sprawdzanie, czy pozycje w preferred są rzeczywiście preferowane
            boolean flag = false;
            for (Vector2d pos : preferred) {
                for (Vector2d adj : grassField.getAdjacent(pos)) {
                    Vector2d checkedPos = pos.add(adj, grassField.getWidth());
                    if (grassField.getGrasses().containsKey(checkedPos)) {
                        flag = true;
                        break;
                    }
                }
                assertTrue(flag, "Pozycja w preferredSet nie jest naprawdę preferowana: " + pos);
            }

            // Sprawdzanie, czy pozycje w unpreferred są rzeczywiście niepreferowane
            for (Vector2d pos : unpreferred) {
                flag = false;
                for (Vector2d adj : grassField.getAdjacent(pos)) {
                    Vector2d checkedPos = pos.add(adj, grassField.getWidth());
                    if (grassField.getGrasses().containsKey(checkedPos)) {
                        flag = true;  // To nie powinno się zdarzyć dla unpreferred
                        break;
                    }
                }
                assertFalse(flag, "Pozycja w unpreferred jest naprawdę preferowana: " + pos);
            }

            // Sprawdzanie, czy pozostałe pozycje są wolne od roślin
            for (Vector2d pos : rest) {
                assertTrue(grassField.getGrasses().containsKey(pos), "Pozycja nie jest zajęta przez roślinę i nie należy do preferred ani unpreferred:" + pos);
            }
            dayCnt++;
        }
    }
}
