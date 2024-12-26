package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    public static final String EXECUTOR_TIMEOUT = "Executor did not terminate in time";
    private final List<Simulation> simulations;
    private final List <Thread> threads = new ArrayList<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private volatile boolean isPaused = false;  // volatile uzywa sie przy synchronicznych rzeczach
    private final Object lock = new Object();    // object level lock

    public SimulationEngine(List<Simulation> simulations){
        this.simulations = simulations;
    }

    public void runSync(){
        for (Simulation sim : simulations) {
            sim.run();
        }
    }

    public void runAsync(){
        for (Simulation sim : simulations){
            Thread thread = new Thread(sim);
            threads.add(thread);
            thread.start();
        }
    }

    public void pause(){
        isPaused = true;
    }

    public void resume(){
        isPaused = false;
        synchronized (lock) {
            lock.notifyAll();  // niech wszystkie czekajace watki wznowia prace
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void awaitSimulationEnd() throws InterruptedException {
        for (Thread thread : threads){
            thread.join();
        }
        executor.shutdown();
        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("%s (%d s)".formatted(EXECUTOR_TIMEOUT, 10));
            executor.shutdownNow();
        }
    }

    public void pauseSimulationIfNeeded() throws InterruptedException {
        synchronized (lock){
            while (isPaused){
                lock.wait();  //czekaj az ktos nie wywola notify, patrz resume()
            }
        }
    }
}