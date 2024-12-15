package agh.ics.oop;
import agh.ics.oop.model.*;
import agh.ics.oop.model.exceptions.IllegalMoveSpecificationException;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) throws Exception {
        List<MoveDirection> directions = null;
        try {
            directions = OptionsParser.parse(args);
        }
        catch (IllegalMoveSpecificationException e) {
            e.printStackTrace();
            System.err.println("error: " + e.getMessage());
            return;
        }
        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
        List<Simulation> simulations = new ArrayList<>();
        for (int i = 0; i < 1000;i++){
            AbstractWorldMap grassF = new GrassField(10);
            grassF.addObserver(new ConsoleMapDisplay());
            AbstractWorldMap rectangularF = new RectangularMap(5,5);
            rectangularF.addObserver(new ConsoleMapDisplay());
            simulations.add(new Simulation(positions,directions,rectangularF));
            simulations.add(new Simulation(positions,directions,grassF));
        }
        SimulationEngine simEngine = new SimulationEngine(simulations);
//        simEngine.runSync();
        simEngine.runAsyncInThreadPool();
        System.out.println("system zakonczyl dzialanie");
//        SimulationApp simulationApp = new SimulationApp();
//        Stage stage = new Stage();
//        simulationApp.start(stage);
    }
    public static void run(MoveDirection[] input){
        for (MoveDirection arg : input) {
            switch (arg) {
                case MoveDirection.FORWARD -> System.out.println("Zwierzak idzie do przodu");
                case MoveDirection.BACKWARD -> System.out.println("Zwierzak idzie do tyłu");
                case MoveDirection.RIGHT -> System.out.println("Zwierzak skręca w prawo");
                case MoveDirection.LEFT -> System.out.println("Zwierzak skręca w lewo");
            }
        }
    }
}
