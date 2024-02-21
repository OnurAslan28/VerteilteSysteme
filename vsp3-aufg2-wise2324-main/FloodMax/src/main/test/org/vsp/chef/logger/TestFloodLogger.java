package org.vsp.chef.logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FloodLoggerTest {

    private static final String TEST_LOG_FILE_PREFIX = "test-logs";
    private static final String TEST_LOG_FILE_SUFFIX = ".txt";
    private static final String LOG_FILE_PATH = "src\\main\\resources\\logs.txt";
    private static String logFilePath;

    @BeforeAll
    static void setup() {
        // Set up a dynamic test log file path
        logFilePath = createTestLogFilePath();
    }

    private static String createTestLogFilePath() {
        try {
            Path tempDir = Files.createTempDirectory(TEST_LOG_FILE_PREFIX);
            return tempDir.resolve(TEST_LOG_FILE_PREFIX + TEST_LOG_FILE_SUFFIX).toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a test log file path", e);
        }
    }

    @AfterAll
    static void cleanup() {
        // Delete the dynamically created test log file
        try {
            Files.deleteIfExists(Paths.get(logFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddErrorLog() {
        // Given
        FloodLogger logger = new FloodLogger(false);

        // When
        FloodLogger.addErrorLog("Test error log message");

        // Then
        assertTrue(Files.exists(Paths.get(LOG_FILE_PATH)));
        // You can add more assertions based on the expected behavior of your logger
    }

    @Test
    void testLoggerWithConsole() {
        // Given
        FloodLogger logger = new FloodLogger(true);

        // When
        FloodLogger.addLog("Test log message with console");

        // Then
        assertTrue(Files.exists(Paths.get(LOG_FILE_PATH)));
        // You can add more assertions based on the expected behavior of your logger
    }

    @Test
    void testWriteLogToFileError() {
        // Given
        FloodLogger logger = new FloodLogger(false);

        // When
//        FloodLogger.setPath("invalid/path/logs.txt");
        FloodLogger.addLog("Test log message with invalid file path");

        // Then
        // Add assertions for expected error handling behavior
    }

    @Test
    void testLogLevel() {
        // Given
        FloodLogger logger = new FloodLogger(false);

        // When
        FloodLogger.addLog("Test log message with INFO level");
        FloodLogger.addErrorLog("Test log message with SEVERE level");

        // Then
        // Add assertions to check if log entries have the expected levels
    }

    @Test
    void testLoggerWithMultipleHandlers() {
        // Given
        FloodLogger logger = new FloodLogger(true);

        // When
        FloodLogger.addLog("Test log message with console");
        FloodLogger.addErrorLog("Test error log message");

        // Then
        // Add assertions to check if log entries appear in both file and console
    }
}
