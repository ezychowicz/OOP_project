package agh.ics.oop.presenter;

import agh.ics.oop.OptionsParser;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class SimulationPresenter implements MapChangeListener {
    private WorldMap worldMap;

    @FXML
    private Label moveInfo;

    @FXML
    private TextField textField;

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

    // Getter for the instance
    public static SimulationPresenter getInstance() {
        return instance;
    }

    @FXML
    private void initialize() {
        instance = this; // Assign the current instance to the static field

        // Set up the sliders
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
            moveInfo.setText(message);
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
        System.out.println(moveInfo.getText());
        moveInfo.setText(worldMap.toString());
    }

    @FXML
    public void initializeSim() {
        int mapWidth = (int) mapWidthSlider.getValue();
        int mapHeight = (int) mapHeightSlider.getValue();

        lowerLeft = new Vector2d(0, 0);
        upperRight = new Vector2d(mapWidth, mapHeight);
        AbstractWorldMap grassF = new GrassField(10, mapWidth, mapHeight);
        grassF.addObserver(this);

        List<MoveDirection> directions = OptionsParser.parse(textField.getText().split("\\s+"));
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
        Simulation sim = new Simulation(positions, directions, grassF);
        SimulationEngine simEngine = new SimulationEngine(List.of(sim));
        new Thread(simEngine::runSync).start();
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
}