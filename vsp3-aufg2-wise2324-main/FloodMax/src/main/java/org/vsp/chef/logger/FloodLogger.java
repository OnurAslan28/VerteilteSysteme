package org.vsp.chef.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

/**
 * The class represents the Logger
 * Instead of every Class having to create this object we could just implement all the functionality here and that
 * it would also be quite easy with the middleware.
 */
public class FloodLogger {
    private static volatile boolean isRunning = true;
    private static boolean printToConsole;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static String path = "src\\main\\resources\\logs.txt";
    private static Logger logger;

    public FloodLogger(boolean printToConsole) {

        logger = Logger.getLogger("FloodLogger");

        try {
            // Specify the file name and create a FileHandler
            FileHandler fileHandler = new FileHandler(path);

            // Set the formatter for the FileHandler
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            // Add the FileHandler to the logger
            logger.addHandler(fileHandler);

            if (printToConsole) {
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(formatter);
                logger.addHandler(consoleHandler);
            }

            // Set the logging level (optional, you can configure this based on your needs)
            logger.setLevel(Level.INFO);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void stopLogging() {
        isRunning = false;
        executorService.shutdown();
    }

    public static void addLog(String log) {

        if (isRunning) {
            logger.info(log);
        }
    }


    public static void addErrorLog(String log) {
//        addLog("ERROR:" + log);
        logger.severe(log);
    }
}
