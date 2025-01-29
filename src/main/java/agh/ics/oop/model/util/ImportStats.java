package agh.ics.oop.model.util;

import agh.ics.oop.Day;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.DayObserver;

public class ImportStats implements DayObserver{
    public static final String ANIMAL_COUNT_STRING = "Animal Number";
    public static final String GRASS_COUNT_STRING = "Grass Number";
    public static final String FREE_FIELDS_STRING = "Free Fields";
    public static final String AVG_ENERGY_STRING = "Average Energy";
    public static final String AVG_LIFESPAN_STRING = "Average Lifespan";
    public static final String AVG_CHILDREN_STRING = "Average Children Number";
    public static final String GENE_STRING = "Gene";
    public static final String DAY_STRING = "Day";
    public static final String ACTIVE_GENOME_PART_STRING = "Active Genome Part";
    public static final String ENERGY_STRING = "Energy";
    public static final String EATEN_PLANTS_STRING = "Eaten Plants";
    public static final String CHILDREN_COUNT_STRING = "Children Count";
    public static final String DESCENDANTS_COUNT_STRING = "Descendants Count";
    public static final String DAYS_LIVED_STRING = "Days Lived";
    public static final String DEATH_DAY_STRING = "Death Day";
    private final SimulationStatsWriter simulationStatsWriter;
    private final AnimalStatsWriter animalStatsWriter;
    private final Day day;
    public ImportStats(String simulationFileName, String animalFileName, Day day) {
        this.simulationStatsWriter = new SimulationStatsWriter(simulationFileName);
        this.animalStatsWriter = new AnimalStatsWriter(animalFileName, day);
        this.day = day;
    }

    @Override
    public void updateSimulationInfo() {
        simulationStatsWriter.writeSimulationInfo(day);
    }

    @Override
    public void updateAnimalInfo(Animal animal) {
        animalStatsWriter.writeAnimalInfo(animal);
    }
}