package org.vsp.mechatroniker.server;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.vsp.service.MechatronikerImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Programm starts here :)
 * e.g args: src\main\resources\.config2
 */
public class MechatronikerStart {
    static int serverPort = 8080;
    static int ownPort = 1234;
    static String host = "localhost";
    private static ApplicationStub applicationStub;


    public static void main(String[] args) {
        checkParameters(args);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, serverPort).usePlaintext() // Nur für Testzwecke, sollte in der Produktion vermieden werden
                .build();
        applicationStub = new ApplicationStub(ownPort, channel, new MechatronikerImpl());
        executorService.submit(() -> applicationStub.startStub());

        DockerNodeHandler.setStub(applicationStub);
        DockerRequestHandler dockerRequestHandler = new DockerRequestHandler(host, ownPort, applicationStub);
        Thread dockerHandlerThread = new Thread(dockerRequestHandler);
        dockerHandlerThread.start();

//        applicationStub.finished();
    }

    /**
     * Check the incoming parameters if they are correct
     *
     * @param args Systems arguments
     *             [0] path to configfile
     */
    private static void checkParameters(String[] args) {
        if (args.length < 1) {
            System.exit(1);
        }
        try {
            mapConfig(args[0]);
        } catch (NumberFormatException e) {
            System.exit(1);
        }

    }

    private static void mapConfig(String configPath) {

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
        }


    }

}
