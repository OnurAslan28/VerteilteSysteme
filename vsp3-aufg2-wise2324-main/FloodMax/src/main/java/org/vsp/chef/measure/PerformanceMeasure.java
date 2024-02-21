package org.vsp.chef.measure;

import org.vsp.chef.logger.FloodLogger;

import java.util.concurrent.TimeUnit;

/**
 * This singleton class measures the time for the algorithm
 */
public class PerformanceMeasure {
    private static PerformanceMeasure instance;

    /**
     * Tracks the start time
     */
    private long startTime;

    /**
     * Duration of the algorithm
     */
    private long resultTime;

    /**
     * Make it a singleton
     */
    private PerformanceMeasure() {
        // Private constructor to enforce singleton pattern
    }

    public static PerformanceMeasure getInstance() {
        if (instance == null) {
            synchronized (PerformanceMeasure.class) {
                if (instance == null) {
                    instance = new PerformanceMeasure();
                }
            }
        }
        return instance;
    }

    /**
     * The method start the timer for counting the time.
     * {@code startTime} will be set here.
     */
    public void startTimer() {
        FloodLogger.addLog("Start Performance Measure");
        startTime = System.nanoTime();
    }

    /**
     * The method stops the timer and sets the result.
     * {@code resultTime} final duration of the algorithm will be stored here.
     */
    public void stopTimer() {
        resultTime = System.nanoTime() - startTime;
        FloodLogger.addLog("Performance Measure end");
    }

    /**
     * Getter for the duration of the Algorithm
     *
     * @return result time
     */
    public String getResultTime() {
//        return resultTime;


        long durationInSeconds = TimeUnit.NANOSECONDS.toSeconds(resultTime);
        long durationInMilliSeconds = TimeUnit.NANOSECONDS.toMillis(resultTime);
        long durationInMinutes = TimeUnit.NANOSECONDS.toMinutes(resultTime);

        // Calculate remaining seconds and milliseconds
        long remainingSeconds = durationInSeconds % 60;
        long remainingMilliSeconds = (durationInMilliSeconds % 1000);

        String result = "Time{" + "durationInNanos=" + resultTime + ", durationInMinutes=" + durationInMinutes + ", remainingSeconds=" + remainingSeconds + ", remainingMilliSeconds=" + remainingMilliSeconds + '}';
        FloodLogger.addLog("Performance measure reuslt: " + result);
        return result;

    }
}
