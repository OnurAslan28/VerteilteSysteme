package org.vsp.chef;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.vsp.chef.logger.FloodLogger;
import org.vsp.chef.measure.PerformanceMeasure;
import org.vsp.chef.schema.Task;
import org.vsp.service.ChefImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Programm starts here :)
 * e.g. args: 5 false [(Hausaufgabe1,2,20:12:22),(Hausaufgabe2,3,20:12:23),(HA3,7,20:21:23),(HA20,1,12:12:12),(HA21,4,17:12:12),(HA22,5,15:12:12),(HA23,6,13:12:12)] src\main\resources\.config
 */
public class FloodMaxStart {
    static private int amountOfContainers = 0;
    static private boolean consoleLog = false;
    static private List<Task> taskList = new ArrayList<>();
    static private String pathToConfig = "";
    static private int serverPort = 8080;
    static private int ownPort = 1234;
    static private String host = "localhost";

    /**
     * This is where the Application will start
     *
     * @param args [0]: int - The amount of container Nodes that should be created
     *             [1]: boolean (default=false) - If console log should be active
     *             [2]: List - A list of Tasks
     *             A task consists of:
     *             - taskName
     *             - taskPriority
     *             - timestamp in HH:mm:ss
     *             [3]: path to configfile
     */
    public static void main(String[] args) {
        new FloodLogger(false);
        checkParameters(args);

        ExecutorService executorService = Executors.newFixedThreadPool(5);


        //performance measure init
        PerformanceMeasure pm = PerformanceMeasure.getInstance();
        // logger starten


        // init chef stub channel & chef server starten
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext() // Nur für Testzwecke, sollte in der Produktion vermieden werden
                .build();
        ApplicationStub applicationStub = new ApplicationStub(ownPort, channel, new ChefImpl());
        executorService.submit(() -> applicationStub.startStub());

        // init container an mechatroniker senden
        if (applicationStub.initContainers(amountOfContainers, taskList)) {
            //if okay dann start mechatroniker
            if (applicationStub.startContainers()) {
                // if okay dann start perfmeasure
                pm.startTimer();
            } else {
                FloodLogger.addErrorLog("Container start went wrong...");
            }
        } else {
            FloodLogger.addErrorLog("Container init went wrong...");
        }
        FloodLogger.addLog("Flooding Done, FloodMaxStart is Done");
    }


    /**
     * Check the incoming parameters if they are correct
     *
     * @param args Systems arguments
     */
    private static void checkParameters(String[] args) {
        FloodLogger.addLog("Check the incoming arguments");
        if (args.length < 4) {
            FloodLogger.addErrorLog("Usage: java FloodMaxStart <amountOfContainers> <consoleLog> <taskList> <configPath>");
            System.exit(1);
        }
        try {
            amountOfContainers = Integer.parseInt(args[0]);
            consoleLog = Boolean.parseBoolean(args[1]);
            mapTasks(args[2]);
            System.out.println(taskList);
            mapConfig(args[3]);
            FloodLogger.addLog("Arguments are fine");
        } catch (NumberFormatException e) {
            FloodLogger.addErrorLog("Error parsing numeric argument: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Map the config file acordingly to the parameters
     *
     * @param configPath Path to config file
     */
    private static void mapConfig(String configPath) {
        //read configfile each line
        //map accordingly

        Path path = Paths.get(configPath);


        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            // Read all lines from the file
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null && lineNumber < 3) {
                switch (lineNumber) {
                    case 0:
                        ownPort = Integer.parseInt(line.trim());
                        break;
                    case 1:
                        serverPort = Integer.parseInt(line.trim());
                        break;
                    case 2:
                        host = line.trim();
                        break;
                }

                lineNumber++;
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading or parsing the config file: " + e.getMessage());
            FloodLogger.addErrorLog("Error reading or parsing the config file: " + e.getMessage());
        }


    }

    /**
     * Parses a formatted string with task information and maps each tuple to create Task objects,
     * adding them to a task list.
     *
     * @param listOfTasks Formatted string with task tuples.
     */
    private static void mapTasks(String listOfTasks) {
        String[] tuples = listOfTasks.replaceAll("\\[|\\]", "").split("\\),\\(");

        for (String tuple : tuples) {
            // Remove parentheses and split the tuple into parts
            String[] parts = tuple.replaceAll("[()]", "").split(",");

            // Extract task information
            String taskName = parts[0].trim();
            int priority = Integer.parseInt(parts[1].trim());
            LocalTime time = LocalTime.parse(parts[2].trim());

            // Create Task object and add it to the list
            Task task = new Task(taskName, priority, time);
            taskList.add(task);
        }
    }
}