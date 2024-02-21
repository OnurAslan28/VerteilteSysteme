package org.vsp.chef.measure;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.vsp.chef.logger.FloodLogger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestPerformanceMeasure {
    @BeforeAll
    static void setup() {
        // Initialize the FloodLogger before running tests
        FloodLogger floodLogger = new FloodLogger(false);
    }

    @Test
    void testPerformanceMeasure() {
        // Given
        PerformanceMeasure performanceMeasure = PerformanceMeasure.getInstance();

        // When
        performanceMeasure.startTimer();

        // Simulate some computation
        performComputation();

        performanceMeasure.stopTimer();

        // Then
        String resultTime = performanceMeasure.getResultTime();

        assertNotNull(resultTime);

        // Print the result time for manual verification
        System.out.println("Performance Measure Result: " + resultTime);

        // You can add more assertions based on the expected behavior of your performance measure
    }

    private void performComputation() {
        // Simulate some computation here
        try {
            Thread.sleep(1000); // Simulate 1 second of computation
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

