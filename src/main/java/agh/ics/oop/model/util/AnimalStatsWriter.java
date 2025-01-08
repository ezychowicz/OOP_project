package agh.ics.oop.model.util;

import agh.ics.oop.Day;
import agh.ics.oop.model.Animal;

import java.io.FileWriter;
import java.io.IOException;

import static agh.ics.oop.Simulation.ANIMAL_STRING;

public class AnimalStatsWriter {
    private final String fileName;
    private final FileWriter writer;
    private final Day day;
    public AnimalStatsWriter(String fileName, Day day) {
        this.fileName = fileName;
        this.writer = handleWriterInit();
        this.day = day;
        initFirstLine();
    }

    private FileWriter handleWriterInit() {
        try {
            return new FileWriter(fileName, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initFirstLine() {
        try {
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s; %s; %s\n".formatted(
                    ANIMAL_STRING,
                    ImportStats.GENE_STRING,
                    ImportStats.ACTIVE_GENOME_PART_STRING,
                    ImportStats.ENERGY_STRING,
                    ImportStats.EATEN_PLANTS_STRING,
                    ImportStats.CHILDREN_COUNT_STRING,
                    ImportStats.DESCENDANTS_COUNT_STRING,
                    ImportStats.DAYS_LIVED_STRING,
                    ImportStats.DEATH_DAY_STRING,
                    ImportStats.DAY_STRING
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAnimalInfo(Animal animal) {
        try {
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s; %s; %s\n".formatted(
                    String.valueOf(animal.getId()),
                    animal.getGenome().toString(),
                    animal.getGenomeAtIdx(animal.getGenomeIdx()),
                    animal.getEnergy(),
                    animal.getEatenGrassCnt(),
                    animal.getChildrenCnt(),
                    animal.getDescendantsCnt(),
                    animal.getDaysOld(),
                    animal.getDeathDay() == -1 ? "Alive" : animal.getDeathDay(),
                    day.getDayCnt()
            ));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
