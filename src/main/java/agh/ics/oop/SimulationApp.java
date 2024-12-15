package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IllegalMoveSpecificationException;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = loader.load();
        SimulationPresenter presenter = loader.getController();

        configureStage(primaryStage, viewRoot);

        primaryStage.show();





//        java.util.List<MoveDirection> directions = null;
//        try {
//            directions = List.of(MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.FORWARD, MoveDirection.RIGHT);
//        }
//        catch (IllegalMoveSpecificationException e) {
//            e.printStackTrace();
//            System.err.println("error: " + e.getMessage());
//            return;
//        }
//        java.util.List<Vector2d> positions = java.util.List.of(new Vector2d(2,2), new Vector2d(3,4));
//        List<Simulation> simulations = new ArrayList<>();
//        AbstractWorldMap grassF = new GrassField(10);
//        grassF.addObserver(new ConsoleMapDisplay());
//        SimulationPresenter simPresenter = loader.getController();
//        simPresenter.setWorldMap(grassF);
//        grassF.addObserver(simPresenter);
//        simulations.add(new Simulation(positions,directions,grassF));
//
//        SimulationEngine simEngine = new SimulationEngine(simulations);
//        simEngine.runSync();
//        simEngine.runAsyncInThreadPool();
    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
