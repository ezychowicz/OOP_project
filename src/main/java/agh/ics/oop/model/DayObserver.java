package agh.ics.oop.model;

public interface DayObserver {
    /**
     * A procedure, that will be called every day for a specific observer. Updates simulation stats.
     */
    public void updateSimulationInfo();
    /**
     * A procedure, that will be called every day for a specific observer. Updates animal stats.
     */
    public void updateAnimalInfo(Animal animal);
}
