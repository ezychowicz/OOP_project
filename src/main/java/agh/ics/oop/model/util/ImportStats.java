package agh.ics.oop.model.util;

import agh.ics.oop.Day;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.Animal;
import agh.ics.oop.model.DayObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.Simulation.ANIMAL_STRING;

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
    private final String fileName;
    private final FileWriter writer;
    private final Day day;

    public ImportStats(String fileName, Day day){
        this.fileName = fileName;
        this.writer = handleWriterInit();
        this.day = day;
        initFirstLineSimulationStats();
        initFirstLineAnimalStats();
    }

    private FileWriter handleWriterInit(){
        try{
            return new FileWriter(fileName, true);
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private void initFirstLineSimulationStats(){
        try{
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s".formatted(DAY_STRING, ANIMAL_COUNT_STRING, GRASS_COUNT_STRING, FREE_FIELDS_STRING, AVG_ENERGY_STRING, AVG_LIFESPAN_STRING, AVG_CHILDREN_STRING, GENE_STRING));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void initFirstLineAnimalStats() {
        try {
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s; %s\n".formatted(ANIMAL_STRING,GENE_STRING, ACTIVE_GENOME_PART_STRING, ENERGY_STRING, EATEN_PLANTS_STRING, CHILDREN_COUNT_STRING, DESCENDANTS_COUNT_STRING, DAYS_LIVED_STRING, DEATH_DAY_STRING));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateSimulationInfo() {
        try{
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s".formatted(String.valueOf(day.getDayCnt()),String.valueOf(day.getAnimalsCount()), String.valueOf(day.getPlantsCount()), String.valueOf(day.getFreeFieldsCount()),
                    String.valueOf(day.getAverageEnergy()), String.valueOf(day.getAverageLifespan()), String.valueOf(day.getAverageChildren()), day.getMostPopularGenotypes()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateAnimalInfo(Animal animal) {
        try {
            writer.append("%s; %s;%s; %s; %s; %s; %s; %s; %s".formatted(
                    String.valueOf(animal.getId()),
                    animal.getGenome().toString(),
                    animal.getGenomeAtIdx(animal.getGenomeIdx()),
                    animal.getEnergy(),
                    animal.getEatenGrassCnt(),
                    animal.getChildrenCnt(),
                    animal.getDescendantsCnt(),
                    animal.getDaysOld(),
                    animal.getDeathDay() == -1 ? "Alive" : animal.getDeathDay() // Jeśli zwierzę żyje, zapisz "Alive"
            ));
            writer.append("\n"); // Przejście do nowej linii
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
