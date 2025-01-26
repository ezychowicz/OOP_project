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
    private int dataSize = 0; //dla zlozonosci
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
        if (followedAnimalId != animal.getId()) {
            currData = new LinkedList<>();
            followedAnimalId = animal.getId();
            dataSize = 0;
        }

        if (dataSize > 100){ //tylko dodaj dane
            currData.removeFirst();
            dataSize--;
        }
        currData.add(List.of(day.getDayCnt(), animal.getEnergy()));
        dataSize++;
    }

    public void drawChart() {
        // Usuwamy stare dane z serii (czyścimy serię)
        series.getData().clear();
        // Dodajemy nowe punkty do serii
        for (List<Integer> point : currData) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(point.getFirst(), point.getLast());
            dataPoint.setNode(null);
            series.getData().add(dataPoint);

        }
        NumberAxis xAxis = (NumberAxis) chart.getXAxis();
        int firstDay = Math.max(day.getDayCnt() - 40, 0);
        xAxis.setLowerBound(firstDay);
        xAxis.setUpperBound(firstDay + 45);
    }
}
