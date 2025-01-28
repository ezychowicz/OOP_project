package agh.ics.oop;

import agh.ics.oop.model.Animal;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChartUpdater {
    private final LineChart<Number, Number> chart;
    private List<List<Integer>> currData = new LinkedList<>();
    private int dataSize = 0;
    private final Day day;
    private final XYChart.Series<Number, Number> series;
    private int followedAnimalId = -1;
    public ChartUpdater(LineChart<Number, Number> chart, Day day) {
        this.chart = chart;
        this.day = day;
        this.series = new XYChart.Series<>();
        chart.getData().add(series);
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        xAxis.setAnimated(false);  // Wyłącz animacje na osi X
        yAxis.setAnimated(false);
        xAxis.setAutoRanging(false);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
    }


    public void updateData(Animal animal) {
        /**
         * @param animal - followed animal
         * remove the oldest data and add the newest
         */
        if (followedAnimalId != animal.getId()) { // If there is a new followed animal
            currData = new LinkedList<>();
            followedAnimalId = animal.getId();
            dataSize = 0;
        }

        if (dataSize > 100){ // If there is enough points on graph, we remove the oldest
            currData.removeFirst();
            dataSize--;
        }
        currData.add(List.of(day.getDayCnt(), animal.getEnergy())); // Add new point
        dataSize++;
    }

    public void drawChart() {
        // Remove old series from graph
        series.getData().clear();
        // Draw new series
        for (List<Integer> point : currData) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(point.getFirst(), point.getLast());
            dataPoint.setNode(null);
            series.getData().add(dataPoint);

        }
        // Movement of x-axis
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        int firstDay = Math.max(day.getDayCnt() - 40, 0);
        xAxis.setLowerBound(firstDay);
        xAxis.setUpperBound(firstDay + 45);
    }
}
