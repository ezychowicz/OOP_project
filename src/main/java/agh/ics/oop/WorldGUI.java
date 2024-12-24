package agh.ics.oop;

import javafx.application.Application;
import agh.ics.oop.presenter.SimulationPresenter;

public class WorldGUI {
    public static int MAP_WIDTH;
    public static int MAP_HEIGHT;
    public static int GRASSES_AMOUNT;
    public static int GRASS_ENERGY;
    public static int GRASS_GROWTH_EACH_DAY;
    public static int ANIMALS_AMOUNT;
    public static int INITIAL_ANIMAL_ENERGY;
    public static int BREEDING_THRESHOLD;
    public static int BREEDING_COST;
    public static int GENOME_LENGTH;
    public static boolean A_PINCH_OF_INSANITY;
    public static boolean SPRAWLING_JUNGLE;

    public static void initializeConstants() {
        SimulationPresenter presenter = SimulationPresenter.getInstance();
        MAP_WIDTH = (int) presenter.mapWidthSlider.getValue();
        MAP_HEIGHT = (int) presenter.mapHeightSlider.getValue();
        GRASSES_AMOUNT = (int) presenter.grassesAmountSlider.getValue();
        GRASS_ENERGY = (int) presenter.energyOnConsumptionSlider.getValue();
        GRASS_GROWTH_EACH_DAY = (int) presenter.grassGrowthEachDaySlider.getValue();
        ANIMALS_AMOUNT = (int) presenter.animalsAmountSlider.getValue();
        INITIAL_ANIMAL_ENERGY = (int) presenter.initialAnimalEnergySlider.getValue();
        BREEDING_THRESHOLD = (int) presenter.breedingThresholdSlider.getValue();
        BREEDING_COST = (int) presenter.breedingCostSlider.getValue();
        GENOME_LENGTH = (int) presenter.genomeLengthSlider.getValue();
        A_PINCH_OF_INSANITY = presenter.aPinchOfInsanityCheckBox.isSelected();
        SPRAWLING_JUNGLE = presenter.sprawlingJungleCheckBox.isSelected();
    }

    public static void main(String[] args) {
        Application.launch(SimulationApp.class, args);
    }
}