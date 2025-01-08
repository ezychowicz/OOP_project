package agh.ics.oop.model.util;

import agh.ics.oop.Day;

import java.io.FileWriter;
import java.io.IOException;

public class SimulationStatsWriter {
    private final String fileName;
    private final FileWriter writer;
    public SimulationStatsWriter(String fileName) {
        this.fileName = fileName;
        this.writer = handleWriterInit();
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
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s\n".formatted(
                    ImportStats.DAY_STRING,
                    ImportStats.ANIMAL_COUNT_STRING,
                    ImportStats.GRASS_COUNT_STRING,
                    ImportStats.FREE_FIELDS_STRING,
                    ImportStats.AVG_ENERGY_STRING,
                    ImportStats.AVG_LIFESPAN_STRING,
                    ImportStats.AVG_CHILDREN_STRING,
                    ImportStats.GENE_STRING
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeSimulationInfo(Day day) {
        try {
            writer.append("%s; %s; %s; %s; %s; %s; %s; %s\n".formatted(
                    String.valueOf(day.getDayCnt()),
                    String.valueOf(day.getAnimalsCount()),
                    String.valueOf(day.getPlantsCount()),
                    String.valueOf(day.getFreeFieldsCount()),
                    String.valueOf(day.getAverageEnergy()),
                    String.valueOf(day.getAverageLifespan()),
                    String.valueOf(day.getAverageChildren()),
                    day.getMostPopularGenotypes().substring(0,day.getMostPopularGenotypes().indexOf("]") + 1)
            ));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
