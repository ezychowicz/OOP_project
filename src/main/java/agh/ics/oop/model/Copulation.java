package agh.ics.oop.model;

import agh.ics.oop.model.exceptions.CopulationFailedException;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static agh.ics.oop.WorldGUI.BREEDING_THRESHOLD;
import static agh.ics.oop.WorldGUI.GENOME_LENGTH;

public class Copulation {
    /*
    Rozmnażanie jest zwykle najciekawszą częścią każdej symulacji ze zwierzakami. Zdrowe młode może mieć
    tylko zdrowa para rodziców, dlatego nasze zwierzaki będą się rozmnażać tylko jeśli mają odpowiednią ilość energii.
    Przy reprodukcji rodzice tracą na rzecz młodego pewną część swojej energii - ta energia będzie rónocześnie stanowić
    startową energię ich potomka.
    Urodzone zwierzę otrzymuje genotyp będący krzyżówką genotypów rodziców. Udział genów jest proporcjonalny do
    energii rodziców i wyznacza miejsce podziału genotypu. Przykładowo, jeśli jeden rodzic ma 50, a drugi 150
    punktów energii, to dziecko otrzyma 25% genów pierwszego oraz 75% genów drugiego rodzica. Udział ten określa
    miejsce przecięcia genotypu, przyjmując, że geny są uporządkowane. W pierwszym kroku losowana jest strona
    genotypu, z której zostanie wzięta część osobnika silniejszego, np. prawa. W tym przypadku dziecko
    otrzymałoby odcinek obejmujący 25% lewych genów pierwszego rodzica oraz 75% prawych genów drugiego rodzica.
    Jeśli jednak wylosowana byłaby strona lewa, to dziecko otrzymałoby 75% lewych genów silniejszego osobnika oraz
    25% prawych genów. Na koniec mają zaś miejsce mutacje: losowa liczba (wybranych również losowo) genów potomka
    zmienia swoje wartości na zupełnie nowe.
    * */
    private final Vector2d position;
    private final GrassField grassField;

    public Copulation(Vector2d position, GrassField grassField) {
        this.position = position;
        this.grassField = grassField;
    }

    private List<Animal> pairPartners() {
        List<Animal> animalsAtPos = grassField.getAnimalsAt(position);
        Animal winner1 = grassField.resolveConflict(grassField.getAnimalsAt(position));
        List<Animal> animalsAtPosWithoutWinner = new ArrayList<>(animalsAtPos); // Kopia listy
        animalsAtPosWithoutWinner.remove(winner1);
        Animal winner2 = grassField.resolveConflict(grassField.getAnimalsAt(position));
        return List.of(winner1, winner2);
    }

    public static int findCrossoverIndex(int energyParent1, int energyParent2) {
        int totalEnergy = energyParent1 + energyParent2;
        if (totalEnergy == 0) {
            throw new IllegalArgumentException("Energy values cannot both be zero.");
        }
        double parent1Ratio = (double) energyParent1 / totalEnergy;
        int crossoverIndex = (int) Math.round(parent1Ratio * GENOME_LENGTH);
        return Math.min(crossoverIndex, GENOME_LENGTH - 1);
    }

    private Map<Integer, Integer> createMutation() {
        Map<Integer, Integer> mutationRecipe = new HashMap<>();
        Random random = new Random();
        int toMutateCnt = random.nextInt(GENOME_LENGTH + 1);
        if (toMutateCnt == 0) {
            return mutationRecipe;
        }
        List<Integer> allIndices = IntStream.range(0, GENOME_LENGTH)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(allIndices);
        for (int i = 0; i < toMutateCnt; i++) {
            mutationRecipe.put(allIndices.get(i), random.nextInt(8));
        }
        return mutationRecipe;
    }


    public Animal copulate() throws CopulationFailedException {
        List<Animal> partners = pairPartners();
        if (partners.getFirst().getEnergy() > BREEDING_THRESHOLD && partners.getLast().getEnergy() > BREEDING_THRESHOLD) {
            int crossIdx = findCrossoverIndex(partners.getFirst().getEnergy(), partners.getLast().getEnergy());
            List<Integer> newGenome = new ArrayList<>();
            Animal dominant = partners.getFirst();
            Animal recessive = partners.getLast();
            Random random = new Random();
            int leftOrRight = random.nextInt(2);
            if (leftOrRight == 0) { //dla dominujacego osobnika bierzemy lewą strone genomu
                newGenome.addAll(dominant.getGenome().subList(0, crossIdx));
                newGenome.addAll(recessive.getGenome().subList(crossIdx, GENOME_LENGTH));
            } else { //dla dominujacego osobnika bierzemy prawą strone genomu
                newGenome.addAll(recessive.getGenome().subList(0, crossIdx));
                newGenome.addAll(dominant.getGenome().subList(crossIdx, GENOME_LENGTH));
            }

            // mutacje
            Map<Integer, Integer> mutationRecipe = createMutation();
            for (Integer gene : mutationRecipe.keySet()) {
                newGenome.set(gene, mutationRecipe.get(gene));
            }
            return new Animal(position, dominant, recessive, newGenome);
        } else {
            throw new CopulationFailedException("Animals do not have enough energy");
        }
    }
}
