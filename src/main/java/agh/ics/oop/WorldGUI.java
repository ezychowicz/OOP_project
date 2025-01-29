package agh.ics.oop;

import agh.ics.oop.model.util.Config;
import javafx.application.Application;
import agh.ics.oop.presenter.SimulationPresenter;

import java.io.IOException;

public class WorldGUI {
    public static void initializeConstants() throws IOException{
        SimulationPresenter presenter = SimulationPresenter.getInstance();
        Config config = Config.getInstance();
        config.set("MAP_WIDTH", (int) presenter.mapWidthSlider.getValue());
        config.set("MAP_HEIGHT", (int) presenter.mapHeightSlider.getValue());
        config.set("GRASSES_AMOUNT", (int) presenter.grassesAmountSlider.getValue());
        config.set("GRASS_ENERGY", (int) presenter.energyOnConsumptionSlider.getValue());
        config.set("GRASS_GROWTH_EACH_DAY", (int) presenter.grassGrowthEachDaySlider.getValue());
        config.set("ANIMALS_AMOUNT", (int) presenter.animalsAmountSlider.getValue());
        config.set("INITIAL_ANIMAL_ENERGY", (int) presenter.initialAnimalEnergySlider.getValue());
        config.set("BREEDING_THRESHOLD", (int) presenter.breedingThresholdSlider.getValue());
        config.set("BREEDING_COST", (int) presenter.breedingCostSlider.getValue());
        config.set("GENOME_LENGTH", (int) presenter.genomeLengthSlider.getValue());
        config.set("A_PINCH_OF_INSANITY", presenter.aPinchOfInsanityCheckBox.isSelected());
        config.set("SPRAWLING_JUNGLE", presenter.sprawlingJungleCheckBox.isSelected());
        config.set("SAVE_TO_CSV", presenter.excelCheckBox.isSelected());
        config.set("COLORING", presenter.coloringCheckbox.isSelected());

        config.save();
        config.load();
    }

    public static void main(String[] args) {
        try {
            Config config = Config.getInstance();
            config.initialize("config.properties");
            config.load();
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
        Application.launch(SimulationApp.class, args);
    }
}