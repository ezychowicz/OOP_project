package agh.ics.oop;

import javafx.application.Application;

public class WorldGUI {
    public static final int PLANT_ENERGY = 5;
    public static final int PLANT = 6;
    public static final int DAY_EFFORT_ENERGY = 1;
    public static final int GENOME_LENGTH = 10;
    public static final int MAP_WIDTH = 15;
    public static void main(String[] args) {
        //to co normalnie bym w World robil teraz powinno byc w WorldGUI
        Application.launch(SimulationApp.class, args);
    }
}
