package agh.ics.oop.presenter;

import agh.ics.oop.ChartUpdater;
import agh.ics.oop.Day;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.ImportStats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.List;

import static agh.ics.oop.WorldGUI.*;

public class SimulationPresenter implements MapChangeListener, DayObserver {

    private WorldMap worldMap;
    private SimulationEngine simEngine;

    @FXML
    private GridPane mapGrid;

    private static SimulationPresenter instance;
    // Static reference to the controller instance

    @FXML
    public Slider mapWidthSlider;
    @FXML
    private Label mapWidthValue;

    @FXML
    public Slider mapHeightSlider;
    @FXML
    private Label mapHeightValue;

    @FXML
    public Slider grassesAmountSlider;
    @FXML
    private Label grassesAmountValue;

    @FXML
    public Slider energyOnConsumptionSlider;
    @FXML
    private Label energyOnConsumptionValue;

    @FXML
    public Slider grassGrowthEachDaySlider;
    @FXML
    private Label grassGrowthEachDayValue;

    @FXML
    public Slider animalsAmountSlider;
    @FXML
    private Label animalsAmountValue;

    @FXML
    public Slider initialAnimalEnergySlider;
    @FXML
    private Label initialAnimalEnergyValue;

    @FXML
    public Slider breedingThresholdSlider;
    @FXML
    private Label breedingThresholdValue;

    @FXML
    public Slider breedingCostSlider;
    @FXML
    private Label breedingCostValue;

    @FXML
    public Slider genomeLengthSlider;
    @FXML
    private Label genomeLengthValue;

    @FXML
    public CheckBox aPinchOfInsanityCheckBox;

    @FXML
    public CheckBox sprawlingJungleCheckBox;

    @FXML
    public CheckBox excelCheckBox;

    @FXML
    private Label animalsCountLabel;
    @FXML
    private Label grassesCountLabel;
    @FXML
    private Label freeFieldsCountLabel;
    @FXML
    private Label mostPopularGenotypesLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifespanLabel;
    @FXML
    private Label averageChildrenLabel;

    @FXML
    private Label animalGenomeLabel;
    @FXML
    private Label activeGenomePartLabel;
    @FXML
    private Label energyLabel;
    @FXML
    private Label eatenPlantsLabel;
    @FXML
    private Label childrenCountLabel;
    @FXML
    private Label descendantsCountLabel;
    @FXML
    private Label daysLivedLabel;
    @FXML
    private Label deathDayLabel;
    @FXML
    private Label animalIdLabel;
    @FXML
    private LineChart<Number, Number> chart;

    private ChartUpdater chartUpdater;
    private Day day;

    // Getter for the instance
    public static SimulationPresenter getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this;
        setupSlider(mapWidthSlider, mapWidthValue);
        setupSlider(mapHeightSlider, mapHeightValue);
        setupSlider(grassesAmountSlider,grassesAmountValue);
        setupSlider(energyOnConsumptionSlider, energyOnConsumptionValue);
        setupSlider(grassGrowthEachDaySlider, grassGrowthEachDayValue);
        setupSlider(animalsAmountSlider, animalsAmountValue);
        setupSlider(initialAnimalEnergySlider, initialAnimalEnergyValue);
        setupSlider(breedingThresholdSlider, breedingThresholdValue);
        setupSlider(breedingCostSlider, breedingCostValue);
        setupSlider(genomeLengthSlider, genomeLengthValue);
    }

    @FXML
    private void setupSlider(Slider slider, Label valueLabel) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int intValue = newValue.intValue();
            valueLabel.setText("Current Value: " + intValue);
        });
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        this.setWorldMap(map);
        Platform.runLater(() -> {
            drawMap();
        });
    }

    private Vector2d lowerLeft;
    private Vector2d upperRight;

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    private int calculateColsCnt(){
        return 1 + upperRight.getX() - lowerLeft.getX();
    }
    private int calculateRowsCnt(){
        return 1 + upperRight.getY() - lowerLeft.getY();
    }

    private void constructAxes(){
        Label cell = new Label("y/x");
        GridPane.setHalignment(cell, HPos.CENTER); // Wyrównanie poziome
        GridPane.setValignment(cell, VPos.CENTER); // Wyrównanie pionowe
        cell.setFont(Font.font("System", FontWeight.BOLD, 14));
        mapGrid.add(cell, 0, 0);
        lowerLeft = ((AbstractWorldMap) worldMap).getLowerLeftBoundary();
        upperRight = ((AbstractWorldMap) worldMap).getUpperRightBoundary();
        int cols = calculateColsCnt();
        int rows = calculateRowsCnt();
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(80.0 / cols);
            mapGrid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(80.0 / rows);
            mapGrid.getRowConstraints().add(rowConstraints);
        }

        for (int i = rows - 1; i >= 1; i--) {
            Label cellR = new Label("%d".formatted(lowerLeft.getY() + (rows - 1 - i)));
            GridPane.setHalignment(cellR, HPos.CENTER); // Wyrównanie poziome
            GridPane.setValignment(cellR, VPos.CENTER); // Wyrównanie pionowe
            cellR.setFont(Font.font("System", FontWeight.BOLD, 14));
            mapGrid.add(cellR, 0,  i);
        }
        for (int i = 0; i < cols - 1; i++) {
            Label cellC = new Label("%d".formatted(i + lowerLeft.getX()));
            GridPane.setHalignment(cellC, HPos.CENTER); // Wyrównanie poziome
            GridPane.setValignment(cellC, VPos.CENTER); // Wyrównanie pionowe
            cellC.setFont(Font.font("System", FontWeight.BOLD, 14));
            mapGrid.add(cellC, i+1, 0);
        }
    }

    private void fillMapGrid(){
        int cols = calculateColsCnt();
        int rows = calculateRowsCnt();
        int minX = lowerLeft.getX();
        int minY = lowerLeft.getY();
        for (int i = minX; i < minX + cols - 1; i++) {
            for (int j = minY; j < minY + rows - 1; j++) {
                Vector2d pos = new Vector2d(i, j);
                if (worldMap.isOccupied(pos)){
                    Label cell = new Label(worldMap.objectAt(pos).toString());
                    GridPane.setHalignment(cell, HPos.CENTER); // Wyrównanie poziome
                    GridPane.setValignment(cell, VPos.CENTER); // Wyrównanie pionowe
                    cell.setFont(Font.font("System", FontWeight.BOLD, 30));
                    mapGrid.add(cell, i - minX + 1, (rows - 1) - (j - minY));
                }
            }
        }
    }

    @FXML
    public void drawMap() {
        clearGrid();
        constructAxes();
        fillMapGrid();
        mapGrid.setGridLinesVisible(true);
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @FXML
    public void pauseSim() {
        if (simEngine != null) {
            simEngine.pause();
        }
    }

    // Resume simulation
    @FXML
    public void resumeSim() {
        if (simEngine != null && simEngine.isPaused()) {
            simEngine.resume();
        }
    }

    // Zacznij nowa symulacje - zamiast initializesim - nie wiem czy mozemy usunac initializeSim, tu to jest troche inaczej zrobione
    @FXML
    public void startNewSim() {
        // jesli trzeba - zatrzymaj symulacje
        if (simEngine != null) {
            simEngine.pause();  // zatrzymaj symulacje
            simEngine = null;   // usun stara symulacje
        }

        initializeConstants(); // zczytaj statiki
        GrassField grassF;
        if (SPRAWLING_JUNGLE) {
            grassF = new SprawlingJungle(GRASSES_AMOUNT, MAP_WIDTH, MAP_HEIGHT);
        }else{
            grassF = new NormalGrassField(GRASSES_AMOUNT, MAP_WIDTH, MAP_HEIGHT);
        }
        grassF.addObserver(this);


        AnimalGenerator animalGenerator = new AnimalGenerator();
        List<Vector2d> positions = animalGenerator.generateInitialPositions();

        if (A_PINCH_OF_INSANITY){
            day = new Day((GrassField) grassF,new CrazyBehaviour());
        }
        else{
            day = new Day((GrassField) grassF,new NormalBehaviour());
        }
        if (SAVE_TO_CSV){
            ImportStats excel = new ImportStats(String.format("%s%s%s","csvsFolder", File.separator, "data.csv"), String.format("%s%s%s","csvsFolder", File.separator, "animaldata.csv"),day);
            day.addObserver(excel);
        }
        day.addObserver(this);
        Simulation newSim = new Simulation(positions, grassF, day);

        simEngine = new SimulationEngine(List.of(newSim));
        newSim.setSimulationEngine(simEngine);
        chartUpdater = new ChartUpdater(chart, day);
        simEngine.runAsync();
    }


    @Override
    public void updateSimulationInfo() {
        Platform.runLater(() -> {
            animalsCountLabel.setText(String.valueOf(day.getAnimalsCount()));
            grassesCountLabel.setText(String.valueOf(day.getPlantsCount()));
            freeFieldsCountLabel.setText(String.valueOf(day.getFreeFieldsCount()));
            mostPopularGenotypesLabel.setAlignment(Pos.TOP_CENTER); // inaczej sie dziwnie formatuje chyba
            mostPopularGenotypesLabel.setText(day.getMostPopularGenotypes());
            averageEnergyLabel.setText(String.format("%.2f", day.getAverageEnergy()));
            averageLifespanLabel.setText(String.format("%.2f", day.getAverageLifespan()));
            averageChildrenLabel.setText(String.format("%.2f", day.getAverageChildren()));

        });
    }

    @Override
    public void updateAnimalInfo(Animal animal) {
        Platform.runLater(() -> {
            animalGenomeLabel.setText(animal.getGenome().toString());
            activeGenomePartLabel.setText(String.valueOf(animal.getGenomeAtIdx(animal.getGenomeIdx())));
            energyLabel.setText(String.valueOf(animal.getEnergy()));
            eatenPlantsLabel.setText(String.valueOf(animal.getEatenGrassCnt()));
            childrenCountLabel.setText(String.valueOf(animal.getChildrenCnt()));
            descendantsCountLabel.setText(String.valueOf(animal.getDescendantsCnt()));
            daysLivedLabel.setText(String.valueOf(animal.getDaysOld()));
            deathDayLabel.setText(String.valueOf(animal.getDeathDay()));
            animalIdLabel.setText(String.valueOf(animal.getId()));
            chartUpdater.updateData(animal);
            chartUpdater.drawChart();
        });
    }
}