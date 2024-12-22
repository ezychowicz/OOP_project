package agh.ics.oop;

import javafx.application.Application;
import agh.ics.oop.presenter.SimulationPresenter;

public class WorldGUI {

    public static final int MAP_WIDTH = (int) SimulationPresenter.getInstance().mapWidthSlider.getValue();
    public static final int MAP_HEIGHT = (int) SimulationPresenter.getInstance().mapHeightSlider.getValue();
    public static final int PLANTS_AMOUNT = (int) SimulationPresenter.getInstance().plantsAmountSlider.getValue();
    public static final int PLANT_ENERGY = (int) SimulationPresenter.getInstance().energyOnConsumptionSlider.getValue();
    public static final int PLANT_GROWTH_EACH_DAY = (int) SimulationPresenter.getInstance().plantGrowthEachDaySlider.getValue();
    public static final int ANIMALS_AMOUNT = (int) SimulationPresenter.getInstance().animalsAmountSlider.getValue();
    public static final int INITIAL_ANIMAL_ENERGY = (int) SimulationPresenter.getInstance().initialAnimalEnergySlider.getValue();
    public static final int BREEDING_THRESHHOLD = (int) SimulationPresenter.getInstance().breedingThreshholdSlider.getValue();
    public static final int BREEDING_COST = (int) SimulationPresenter.getInstance().breedingCostSlider.getValue();
    public static final int GENOME_LENGTH = (int) SimulationPresenter.getInstance().genomeLengthSlider.getValue();
    public static final boolean A_PINCH_OF_INSANITY = SimulationPresenter.getInstance().aPinchOfInsaniityCheckBox.isSelected();
    public static final boolean SPRAWLING_JUNGLE = SimulationPresenter.getInstance().sprawlingJungleCheckBox.isSelected();
    public static final int DAY_EFFORT_ENERGY = 1;

    public static void main(String[] args) {
        //to co normalnie bym w World robil teraz powinno byc w WorldGUI
        Application.launch(SimulationApp.class, args);
    }
}
