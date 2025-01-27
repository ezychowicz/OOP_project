package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.CopulationFailedException;
import agh.ics.oop.model.exceptions.IncorrectPositionException;
import agh.ics.oop.model.util.Config;
import agh.ics.oop.presenter.SimulationPresenter;

import java.util.*;



public class Day {
    private int dayCnt = 0;
    private final GrassField grassField;
    private Map<Vector2d, List<Animal>> animals;
    private final AnimalBehaviour animalBehaviour;
    private static final int DAY_EFFORT_ENERGY = 1;
    public int currAnimalsCnt = 0;
    public int currPlantsCnt = 0;
    public int currFreeFieldsCnt = 0;
    public String currMostPopularGenotypes = """
            test
            sfh
            test ok
            """;
    public float currAverageEnergy = 0;
    public float currAverageLifespan = 0;
    public float currAverageChildren = 0;
    private final AnimalFamilyTree familyTree = new AnimalFamilyTree();
    private List<Animal> deadAnimals = new ArrayList<>();
    private final List<DayObserver> observers = new ArrayList<>();
    private int watchedAnimalId = 0;
    private final Consumption consumption = new Consumption();

    private final Config config = Config.getInstance();
    private final int GRASS_GROWTH_EACH_DAY = config.getInt("GRASS_GROWTH_EACH_DAY");

    public Day(GrassField grassField, AnimalBehaviour animalBehaviour) {
        this.grassField = grassField;
        this.animals = grassField.getAnimals(); //pozycje zwierzakow
        this.animalBehaviour = animalBehaviour;
    }


    public void addObserver(DayObserver observer) {
        observers.add(observer);
    }

    public void signalObservers(Animal animal) {
        for (DayObserver observer : observers) {
            observer.updateSimulationInfo();
            observer.updateAnimalInfo(animal);
        }
    }

    public void dayProcedure() throws IncorrectPositionException{ //
        System.out.println("Dzien: " + dayCnt);
        //usuwanie martwych zwierzat z animals i codzienne redukowanie energii
        updateAnimalsState();
        //poruszanie sie zwierzakow
        animals = updateAnimalsPositions(animalBehaviour);
        grassField.setAnimals(animals); // sprawdz czy na pewno to jest git

        //konsumpcja roslin
        for (Vector2d position : animals.keySet()) {
            consumption.consume(grassField, position);
        }

        //rozmnazanie
        //iterator umozliwia modyfikowanie obiektu iterowanego w trakcie iteracji po nim w sposob bezpieczny
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

        //wzrost nowych roslin
        grassField.plantingGrasses(GRASS_GROWTH_EACH_DAY);

        //brzydalstwo fuuuu
        getWatchedAnimal().setDescendantsCnt(getDescendantsCount(watchedAnimalId));
        calculateStats();
        signalObservers(getWatchedAnimal());

    }

    private void updateAnimalsState() {
        List<Vector2d> positionsToRemove = new ArrayList<>();

        // wczesniej animalsy byly usuwane w trakcie iterowania, ale to nie dziala - trzeba stworzyc liste "do usuniecia" i dopiero potem je usunac
        for (Map.Entry<Vector2d, List<Animal>> entry : animals.entrySet()) {
            Vector2d pos = entry.getKey();
            List<Animal> animalsAtPos = entry.getValue();
            List<Integer> toRemoveIdxs = new ArrayList<>();

            for (int i = 0; i < animalsAtPos.size(); i++) {
                Animal animal = animalsAtPos.get(i);
                animal.updateEnergy(-DAY_EFFORT_ENERGY);
                if (animal.getEnergy() <= 0){
                    System.out.println("Zwierze " + animal.getId() + " umarlo");
                    toRemoveIdxs.add(i);
                    animal.setDeathDay(dayCnt);
                }
                else{
                    animal.daysOldIncrement();
                }
            }

            // Remove animals marked for removal
            for (int idx = toRemoveIdxs.size() - 1; idx >= 0; idx--) {
                deadAnimals.add(animalsAtPos.get((int) toRemoveIdxs.get(idx)));
                animalsAtPos.remove((int) toRemoveIdxs.get(idx));
            }

            if (animalsAtPos.isEmpty()) {
                positionsToRemove.add(pos);
            }
        }

        // Remove empty positions from the map
        for (Vector2d pos : positionsToRemove) {
            animals.remove(pos);
        }
    }


    private Map<Vector2d,List<Animal>> updateAnimalsPositions(AnimalBehaviour behaviour){
        Map<Vector2d,List<Animal>> newAnimals =  new HashMap<>();
        for (List<Animal> animalsAtPos : animals.values()){
            for (Animal animal : animalsAtPos){
                animal.move(MoveDirection.geneToDirection(animal.getGenomeAtIdx(behaviour.nextGeneIdx(animal))), grassField);
                System.out.println("id zwierzaka: " + animal.getId() + ", energia: " + animal.getEnergy());
                //to jest okropne. demeter zawiedziona
                grassField.addAnimalAtPos(newAnimals, animal, animal.getPos());
            }
        }
        return newAnimals;
    }


    public int getDayCnt(){
        return dayCnt;
    }

    public void setDayCnt(int i){
        dayCnt = i;
    }

    private void calculateStats(){
        int animalsCnt = 0;
        int plantsCnt = 0;
        int freeFieldsCnt = 0;
        Map<String, Integer> genotypes = new HashMap<>();
        float sumEnergy = 0;
        float sumLifespan = 0;
        float sumChildren = 0;

        for (List<Animal> animalsAtPos : animals.values()){
            for (Animal animal : animalsAtPos){
                animalsCnt++;
                sumEnergy += animal.getEnergy();
                sumLifespan += animal.getDaysOld();
                sumChildren += animal.getChildrenCnt();
                String genotype = animal.getGenome().toString();
                if (genotypes.containsKey(genotype)){
                    genotypes.put(genotype, genotypes.get(genotype) + 1);
                }
                else{
                    genotypes.put(genotype, 1);
                }
            }
        }

        plantsCnt = grassField.getGrasses().size();
        freeFieldsCnt = grassField.getFreeFields().size();

        currAnimalsCnt = animalsCnt;
        currPlantsCnt = plantsCnt;
        currFreeFieldsCnt = freeFieldsCnt;

        StringBuilder topGenotypes = new StringBuilder();
        genotypes.entrySet()
                .stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .forEach(entry -> topGenotypes.append(entry.getKey()).append(", ").append(entry.getValue()).append("\n"));
        //alez dziadostwo

        currMostPopularGenotypes = topGenotypes.toString().trim();
        currAverageEnergy = sumEnergy / animalsCnt;
        currAverageLifespan = sumLifespan / animalsCnt;
        currAverageChildren = sumChildren / animalsCnt;
    }

    public Animal getAnimalWithId(int i){
        for (List<Animal> animalsAtPos : animals.values()){
            for (Animal animal : animalsAtPos){
                if (animal.getId() == i){
                    return animal;
                }
            }
        }
        for (Animal animal : deadAnimals){
            if (animal.getId() == i){
                return animal;
            }
        }
        return null;
    }

    public int getAnimalsCount(){
        return currAnimalsCnt;
    }

    public int getPlantsCount(){
        return currPlantsCnt;
    }

    public int getFreeFieldsCount(){
        return currFreeFieldsCnt;
    }

    public String getMostPopularGenotypes(){
        return currMostPopularGenotypes;
    }

    public float getAverageEnergy(){
        return currAverageEnergy;
    }

    public float getAverageLifespan(){
        return currAverageLifespan;
    }

    public float getAverageChildren(){
        return currAverageChildren;
    }

    public Map<Vector2d, List<Animal>> getAnimals(){
        return animals;
    }

    public int getDescendantsCount(int animalId) {
        return familyTree.getDescendantsCount(animalId);
    }

    public List<Animal> getCurrSimAnimals() { // aby domyslny ogladany animal istnial w obecnej symulacji
        List<Animal> currSimAnimals = new ArrayList<>();
        for (List<Animal> animalsAtPos : animals.values()){
            currSimAnimals.addAll(animalsAtPos);
        }
        return currSimAnimals;
    }

    public Animal getFirstCurrSimAnimal() { // nie jest to pierwsze id tylko pierwszy w liscie a to nie koniecznie to samo ale wychodzi z tego fajna losowosc - chyba mozemy to zostawic i nadpisywac te domyslnosc wyborem uzytkownika ktory zaimplementujemy w gui
        return getCurrSimAnimals().getFirst();
    }

    public void setWatchedAnimalId(int id) {
        watchedAnimalId = id;
    }

    public Animal getWatchedAnimal() {
        return getAnimalWithId(watchedAnimalId);
    }
}
