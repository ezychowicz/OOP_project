package agh.ics.oop.presenter;

import agh.ics.oop.ChartUpdater;
import agh.ics.oop.Day;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Config;
import agh.ics.oop.model.util.ImportStats;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.*;

import static agh.ics.oop.WorldGUI.*;

public class SimulationPresenter implements MapChangeListener, DayObserver {

    private final Image animalImageResource = new Image(Objects.requireNonNull(getClass().getResource("/icons/rat2.png")).toExternalForm());
    private final Image grassImageResource = new Image(Objects.requireNonNull(getClass().getResource("/icons/bush.png")).toExternalForm());
    public Button saveConfigButton;
    public Button loadConfigButton;


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
    public CheckBox coloringCheckbox;

    @FXML
    private CheckBox linkSlidersCheckbox;

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

        setupBreedingSliders();
        setupLinkedSliders();

    }

    @FXML
    private void setupSlider(Slider slider, Label valueLabel) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int intValue = newValue.intValue();
            valueLabel.setText("Current Value: " + intValue);
        });
    }

    private void setupBreedingSliders() {
        // Ensure breedingCostSlider <= breedingThresholdSlider
        breedingCostSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > breedingThresholdSlider.getValue()) {
                breedingCostSlider.setValue(breedingThresholdSlider.getValue());
            }
        });

        // Ensure breedingThresholdSlider >= breedingCostSlider
        breedingThresholdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < breedingCostSlider.getValue()) {
                breedingThresholdSlider.setValue(breedingCostSlider.getValue());
            }
        });
    }

    private void setupLinkedSliders() {
        // Listener for mapWidthSlider
        mapWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (linkSlidersCheckbox.isSelected()) {
                mapHeightSlider.setValue(newValue.doubleValue());
            }
        });

        // Listener for mapHeightSlider
        mapHeightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (linkSlidersCheckbox.isSelected()) {
                mapWidthSlider.setValue(newValue.doubleValue());
            }
        });
    }

    private final Config config = Config.getInstance();
    int MAP_WIDTH = config.getInt("MAP_WIDTH");
    int MAP_HEIGHT = config.getInt("MAP_HEIGHT");
    boolean COLORING = config.getBoolean("COLORING");

    // Save Configurations to File
    public void handleSaveConfig() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Properties Files", "*.properties"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try (OutputStream output = new FileOutputStream(file)) {
                Properties config = new Properties();
                config.setProperty("mapWidth", String.valueOf(mapWidthSlider.getValue()));
                config.setProperty("mapHeight", String.valueOf(mapHeightSlider.getValue()));
                config.setProperty("grassesAmount", String.valueOf(grassesAmountSlider.getValue()));
                config.setProperty("energyOnConsumption", String.valueOf(energyOnConsumptionSlider.getValue()));
                config.setProperty("grassGrowthEachDay", String.valueOf(grassGrowthEachDaySlider.getValue()));
                config.setProperty("animalsAmount", String.valueOf(animalsAmountSlider.getValue()));
                config.setProperty("initialAnimalEnergy", String.valueOf(initialAnimalEnergySlider.getValue()));
                config.setProperty("breedingThreshold", String.valueOf(breedingThresholdSlider.getValue()));
                config.setProperty("breedingCost", String.valueOf(breedingCostSlider.getValue()));
                config.setProperty("genomeLength", String.valueOf(genomeLengthSlider.getValue()));
                config.setProperty("saveToCsv", String.valueOf(excelCheckBox.isSelected()));
                config.setProperty("sprawlingJungle", String.valueOf(sprawlingJungleCheckBox.isSelected()));
                config.setProperty("aPinchOfInsanity", String.valueOf(aPinchOfInsanityCheckBox.isSelected()));
                config.setProperty("coloring", String.valueOf(coloringCheckbox.isSelected()));

                config.store(output, "Simulation Configuration");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Load Configurations from File
    public void handleLoadConfig() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Configuration");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Properties Files", "*.properties"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try (InputStream input = new FileInputStream(file)) {
                Properties config = new Properties();
                config.load(input);

                mapWidthSlider.setValue(Double.parseDouble(config.getProperty("mapWidth", "10")));
                mapHeightSlider.setValue(Double.parseDouble(config.getProperty("mapHeight", "10")));
                grassesAmountSlider.setValue(Double.parseDouble(config.getProperty("grassesAmount", "10")));
                energyOnConsumptionSlider.setValue(Double.parseDouble(config.getProperty("energyOnConsumption", "10")));
                grassGrowthEachDaySlider.setValue(Double.parseDouble(config.getProperty("grassGrowthEachDay", "10")));
                animalsAmountSlider.setValue(Double.parseDouble(config.getProperty("animalsAmount", "10")));
                initialAnimalEnergySlider.setValue(Double.parseDouble(config.getProperty("initialAnimalEnergy", "10")));
                breedingThresholdSlider.setValue(Double.parseDouble(config.getProperty("breedingThreshold", "10")));
                breedingCostSlider.setValue(Double.parseDouble(config.getProperty("breedingCost", "10")));
                genomeLengthSlider.setValue(Double.parseDouble(config.getProperty("genomeLength", "10")));
                excelCheckBox.setSelected(Boolean.parseBoolean(config.getProperty("saveToCsv", "false")));
                aPinchOfInsanityCheckBox.setSelected(Boolean.parseBoolean(config.getProperty("aPinchOfInsanity", "false")));
                sprawlingJungleCheckBox.setSelected(Boolean.parseBoolean(config.getProperty("sprawlingJungle", "false")));
                coloringCheckbox.setSelected(Boolean.parseBoolean(config.getProperty("coloring", "false")));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mapChanged(WorldMap map, String message) {
        this.setWorldMap(map);
        Platform.runLater(() -> {
            drawMap();
        });
    }

    private Vector2d lowerLeft = new Vector2d(-1, -1);
    private Vector2d upperRight = new Vector2d(MAP_WIDTH, MAP_HEIGHT);
    private Vector2d toColorAnimalPos = new Vector2d(-1, -1);
    private Set<Vector2d> toColorPos = new HashSet<>();

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    private int calculateColsCnt(){
        return 1 + upperRight.getX() - lowerLeft.getX();
    }
    private int calculateRowsCnt(){
        return 1 + upperRight.getY() - lowerLeft.getY();
    }

    private void constructAxes() {
        lowerLeft = ((AbstractWorldMap) worldMap).getLowerLeftBoundary();
        upperRight = ((AbstractWorldMap) worldMap).getUpperRightBoundary();
        int cols = calculateColsCnt();
        int rows = calculateRowsCnt();

        double gridWidth = mapGrid.getWidth();
        double gridHeight = mapGrid.getHeight();


        double cellWidthPercentage = gridWidth / cols;
        double cellHeightPercentage = gridHeight / rows;

        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(cellWidthPercentage);
            mapGrid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(cellHeightPercentage);
            mapGrid.getRowConstraints().add(rowConstraints);
        }

    }

    private void fillMapGrid() {
        int cols = calculateColsCnt();
        int rows = calculateRowsCnt();

        double gridWidth = mapGrid.getWidth() * 0.9;
        double gridHeight = mapGrid.getHeight() * 0.9;

        double cellWidth = gridWidth / cols;
        double cellHeight = gridHeight / rows;

        int minX = lowerLeft.getX();
        int minY = lowerLeft.getY();

        COLORING = config.getBoolean("COLORING");

        for (int i = minX; i < minX + cols; i++) {
            for (int j = minY; j < minY + rows; j++) {
                Vector2d pos = new Vector2d(i, j);
                StackPane cellBackground = new StackPane();

                Color groundGreen = Color.LIGHTGOLDENRODYELLOW;
                cellBackground.setBackground(new Background(new BackgroundFill(groundGreen, null, null)));

                if (COLORING) {
                    if (toColorPos.contains(pos)) {
                        Color lessIntenseColor = Color.LIGHTBLUE.deriveColor(0, 1, 1, 0.5);
                        cellBackground.setBackground(new Background(new BackgroundFill(lessIntenseColor, null, null)));
                    }
                    if (toColorAnimalPos != null && toColorAnimalPos.equals(pos)) {
                        Color lessIntenseColor = Color.LIGHTGREEN.deriveColor(0, 1, 1, 0.5);
                        cellBackground.setBackground(new Background(new BackgroundFill(lessIntenseColor, null, null)));
                    }
                }

                // Add objects to cells
                if (worldMap.isOccupied(pos)) {
                    Object object = worldMap.objectAt(pos);
                    if (object instanceof Animal) {
                        ImageView animalImageView = getAnimalImageView((Animal) object);

                        animalImageView.setFitWidth(cellWidth * 0.8);
                        animalImageView.setFitHeight(cellHeight * 0.8);
                        animalImageView.setPreserveRatio(true);

                        GridPane.setHalignment(animalImageView, HPos.CENTER);
                        GridPane.setValignment(animalImageView, VPos.CENTER);
                        cellBackground.getChildren().add(animalImageView);

                        animalImageView.setOnMouseClicked(event -> {
                            day.setWatchedAnimalId(((Animal) object).getId());
                            updateAnimalInfo((Animal) object);
                            if (COLORING) {
                                Color lessIntenseColor = Color.LIGHTGREEN.deriveColor(0, 1, 1, 0.5);
                                cellBackground.setBackground(new Background(new BackgroundFill(lessIntenseColor, null, null)));
                            }
                        });
                    } else if (object instanceof Grass) {
                        ImageView grassImageView = new ImageView(grassImageResource);

                        // Limit image size to 80% of the cell size
                        grassImageView.setFitWidth(cellWidth * 0.8);
                        grassImageView.setFitHeight(cellHeight * 0.8);
                        grassImageView.setPreserveRatio(true);

                        GridPane.setHalignment(grassImageView, HPos.CENTER);
                        GridPane.setValignment(grassImageView, VPos.CENTER);
                        cellBackground.getChildren().add(grassImageView);
                    }
                }
                mapGrid.add(cellBackground, i - minX + 1, (rows - 1) - (j - minY));
            }
        }
    }

    private ImageView getAnimalImageView(Animal object) {
        ImageView animalImageView = new ImageView(animalImageResource);
        MapDirection direction = object.getDirection();
        double angle = switch (direction) {
            case NORTH -> 0;
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            case NORTH_EAST -> 45;
            case NORTH_WEST -> 315;
            case SOUTH_EAST -> 135;
            case SOUTH_WEST -> 225;
        };
        animalImageView.setRotate(angle);
        animalImageView.preserveRatioProperty().set(true);
        return animalImageView;
    }

    @FXML
    public void drawMap() {
        clearGrid();
        constructAxes();
        fillMapGrid();
        mapGrid.setGridLinesVisible(false);
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
    public void startNewSim() throws IOException{
        // jesli trzeba - zatrzymaj symulacje
        if (simEngine != null) {
            simEngine.pause();  // zatrzymaj symulacje
            simEngine = null;   // usun stara symulacje
        }

        initializeConstants(); // zczytaj statiki
        int MAP_WIDTH = config.getInt("MAP_WIDTH");
        int MAP_HEIGHT = config.getInt("MAP_HEIGHT");
        int GRASSES_AMOUNT = config.getInt("GRASSES_AMOUNT");
        boolean A_PINCH_OF_INSANITY = config.getBoolean("A_PINCH_OF_INSANITY");
        boolean SPRAWLING_JUNGLE = config.getBoolean("SPRAWLING_JUNGLE");
        boolean SAVE_TO_CSV = config.getBoolean("SAVE_TO_CSV");
        boolean COLORING = config.getBoolean("COLORING");
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
            toColorPos = new HashSet<>(((GrassField) worldMap).getPrefferedPositions());
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
            if (animal.getDeathDay() == -1) { //is alive
                toColorAnimalPos = animal.getPos();
            } else {
                toColorAnimalPos = null;
            }
        });
    }
}