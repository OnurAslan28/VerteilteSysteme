package org.vsp.mechatroniker.dockerfiletesting;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FloodNode {
    // Semaphore to control synchronization
    static final Semaphore proccessSempahore = new Semaphore(1);
    private static final ThreadPoolExecutor serverExecutorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    private static final ThreadPoolExecutor loggingExecutorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    static String taskName = null;
    static int priority = 0;
    static LocalTime time = null;
    static String hostIP;
    static int hostPort;
    static String myContainerName;
    static List<String> connections;
    static int myExposedPort;
    static List<String> taskMemory = new ArrayList<>();
    static List<Integer> priorityMemmory = new ArrayList<>();
    static List<LocalTime> timeMemory = new ArrayList<>();
    static List<String> alreadyproccessed = new ArrayList<>();

    public static void main(String[] args) {

        List<String> allTasks = Arrays.asList(args[0].substring(1, args[0].length() - 1).split(","));
        System.out.println(allTasks);

        int maxPrio = 0;
        int maxPrioPosition = 1;
        for (int i = 1; i < allTasks.size(); i = i + 3) {
            if (maxPrio < Integer.parseInt(allTasks.get(i).trim())) {
                maxPrio = Integer.parseInt(allTasks.get(i).trim());
                maxPrioPosition = i;
            } else if (Integer.parseInt(allTasks.get(maxPrioPosition).trim()) == Integer.parseInt(allTasks.get(i).trim())) {
                // compare timestamp
                LocalTime max = LocalTime.parse(allTasks.get(maxPrioPosition + 1).replace(")", "").trim());
                LocalTime other = LocalTime.parse(allTasks.get(i + 1).replace(")", "").trim());
                int result = max.compareTo(other);
                if (result > 0) {
                    maxPrio = Integer.parseInt(allTasks.get(i).trim());
                    maxPrioPosition = i;
                }
            }
        }
        taskName = allTasks.get(maxPrioPosition - 1).replace("(", "").trim();
        priority = Integer.parseInt(allTasks.get(maxPrioPosition).trim());
        time = LocalTime.parse(allTasks.get(maxPrioPosition + 1).replace(")", "").trim());

        hostIP = args[1].trim();
//        System.out.println(hostIP);
        hostPort = Integer.parseInt(args[2].trim());
        myContainerName = args[3].trim().replaceAll("\\\\", "");
        connections = Arrays.asList(args[4].substring(1, args[4].length() - 1).split(","));
        myExposedPort = Integer.parseInt(args[5].trim());

        loggingExecutorService.submit(() -> sendHandler("Log[\"Initialized all Parameters\"]"));

        // Now you can access the variables
        System.out.println("Task Name: " + taskName);
        System.out.println("Priority: " + priority);
        System.out.println("Time: " + time);
        System.out.println("Host IP: " + hostIP);
        System.out.println("Host Port: " + hostPort);
        System.out.println("Container Name: " + myContainerName);
        System.out.println("Connections: " + connections);


        serverExecutorService.submit(FloodNode::severStart);


    }

    /**
     * Start the Server
     */
    private static void severStart() {
        // this is so the host can talk to container
        serverExecutorService.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(myExposedPort)) {
                // das ist handling von master, von exported kommt nur der master rein normalerweise
                System.out.println("Server listening on port " + myExposedPort + "...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // this is so container can talk to each others.
        serverExecutorService.submit(() -> {
            // ander cont handling
            try (ServerSocket serverSocket = new ServerSocket(8080)) {
                System.out.println("Server listening on port " + 8080 + "...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sendHandler("Log[\"we have started our services and now sending a log to you\"]");
    }

    /**
     * Handle a Client Message from Container or the Host
     *
     * @param clientSocket
     */
    private static void handleClient(Socket clientSocket) {
        try {
            System.out.println("Client connected: " + clientSocket.getInetAddress() + " ");
            // Get the input stream from the client socket
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the data sent by the client
            String clientData = reader.readLine();
            System.out.println("Received from client(" + clientSocket.getInetAddress() + "): " + clientData);

            // Define a regular expression pattern
            String regex = "([a-zA-Z]+)\\[([a-zA-Z0-9,:-]*)\\]";  // Matches one or more digits

            // Compile the pattern
            Pattern pattern = Pattern.compile(regex);

            // Create a matcher object
            Matcher matcher = pattern.matcher(clientData);
            System.out.println("Trying to match it now");
            // Find matches
            String matchFound = "";
            while (matcher.find()) {
                if (matcher.group(1).equals("Message")) {
                    proccessSempahore.acquire();
                    System.out.println("Found a Message");
                    loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " Received a Message from a Container\"]"));
                    matchFound = matcher.group(2).replaceAll("(,Mechatroniker-[0-9]*)", "");
                    if (alreadyproccessed.contains(matchFound)) break;
                    proccessMessage(matcher.group(2));
                } else if (matcher.group(1).equals("Flood")) {
                    System.out.println("I will start the Flooding");
                    loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " I will start the Flooding\"]"));
                    proccessSempahore.acquire();
                    startFlooding();
                } else {
                    System.out.println("Nothing Matched!!");
                }
            }
            if (!alreadyproccessed.contains(matchFound) && !matchFound.equals("\"\"")) {

                alreadyproccessed.add(matchFound);
                //proccessSempahore.release(); // to many releases?
                if (matchFound.contains(",")) {
                    String[] resultArray = matchFound.split(",");
                    sendHandler("Fin[" + resultArray[0] + "," + resultArray[1] + "," + resultArray[2] + "] from " + myContainerName);
                }
                matchFound = "";
            }
            proccessSempahore.release();
            // Close the connection
            System.out.println("Closing Client");
            clientSocket.close(); // is this correct?
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Part of the Flooding proccess to proccess the message and compare to our message.
     *
     * @param message
     */
    private static void proccessMessage(String message) {
        // Split the string using comma as the delimiter
        System.out.println("Proccesing the Message");
        loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " is proccessing the Message\"]"));
        String[] parts = message.split(",");

        String task = parts[0];
        int priority = Integer.parseInt(parts[1]);
        LocalTime time = LocalTime.parse(parts[2]);
        String otherContainerName = parts[3];
        if (taskMemory.contains(task) && priorityMemmory.contains(priority) && timeMemory.contains(time)) return;
        taskMemory.add(task);
        priorityMemmory.add(priority);
        timeMemory.add(time);
        if (FloodNode.priority > priority) {
//            System.out.println("our prio > inc prio");
            loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " will start flood all\"]"));
            startFlooding();
        } else if (FloodNode.priority == priority) {
//            System.out.println("our prio == inc prio");
            int result = FloodNode.time.compareTo(time);
            if (result < 0) { // our time is better than the incoming time, means smaller here e.g. ours 12:30 inc 14:30
//                System.out.println("our time < inc time");
                loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " will start flood all\"]"));
                startFlooding();
            } else if (result == 0) { // time == time

            } else {
//                System.out.println("Inc better time than our, set to their task");

                FloodNode.taskName = task;
                FloodNode.priority = priority;
                FloodNode.time = time;
                loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " will continue flood\"]"));
                continueFlood(otherContainerName);
            }
        } else {
//            System.out.println("Inc better prio than our, set to their task");
            FloodNode.taskName = task;
            FloodNode.priority = priority;
            FloodNode.time = time;
            loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " will continue flood\"]"));
            continueFlood(otherContainerName);
        }


    }


    /**
     * This will send flood to all execpt the parameter
     *
     * @param skipContainerName skip this, because the flood from which it came was not better, so we skip a step
     */
    private static void continueFlood(String skipContainerName) {
//        System.out.println("Continue Flooding, dont go back!");
        boolean connected = false;

        for (int i = 0; i < connections.size(); ) {
            String connectionContainer = connections.get(i);

            if (!connectionContainer.equals(skipContainerName)) {
                loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " will now send to: " + skipContainerName + "\"]"));
                try (Socket socket = new Socket(connectionContainer, 8080)) {
                    connected = sendMessage(connectionContainer, socket, connected);
                    i++;
                } catch (IOException e) {
                    loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " Server not available\"]"));
                    System.out.println(connectionContainer + "Server not available. Retrying in 5 seconds...");

                    // Wait for 5 seconds before retrying
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            } else {
                i++;
            }
        }
        //sendHandlerFin();
    }


    /**
     * This will send flooding to all
     */
    private static void startFlooding() {
        System.out.println("Starting the Flooding Algorithm from beginning/to all");
        boolean connected = false;

        for (int i = 0; i < connections.size(); ) {
            String connection = connections.get(i);
            try (Socket socket = new Socket(connection, 8080)) {
                connected = sendMessage(connection, socket, connected);
                i++;
            } catch (IOException e) {
                loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " Server not available\"]"));
                System.out.println(connection + "Server not available. Retrying in 5 seconds...");

                // Wait for 5 seconds before retrying
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
//        System.out.println("Matching done");
//        System.out.println("startFlooding - And these are my results: " + taskName + " " + priority + " " + time);
        //sendHandlerFin();
    }

    /**
     * Send Message to a Container
     *
     * @param connection message to this container
     * @param socket     build socket
     * @param connected  unused
     * @return if successfull
     * @throws IOException
     */
    private static boolean sendMessage(String connection, Socket socket, boolean connected) throws IOException {
        System.out.println("Connected to the server: " + connection);
        // Get the output stream from the socket
        OutputStream outputStream = socket.getOutputStream();

        // Wrap the output stream in a PrintWriter for convenient writing of text
        PrintWriter out = new PrintWriter(outputStream, true);
        String tmp = String.format("Message[%s,%d,%s,%s]", taskName, priority, time, myContainerName);
        System.out.println("Sending this: " + tmp + " Message to " + connection);
        out.println(tmp);
        out.close();
        // Close the socket when done
        socket.close();
        // send fin() to master

        // If the connection is successful, set connected to true to exit the loop
        connected = true;
        return connected;
    }

    /**
     * Send a Message to the Host
     *
     * @param sendMessage
     */
    private static void sendHandler(String sendMessage) {
        while (true) {
            try (Socket socket = new Socket(hostIP, hostPort)) {
                OutputStream outputStream = socket.getOutputStream();
                // Wrap the output stream in a PrintWriter for convenient writing of text
                PrintWriter out = new PrintWriter(outputStream, true);
                out.println(sendMessage);
                out.close();
                // Close the socket when done
                // send fin() to master
                break;
            } catch (IOException e) {
                loggingExecutorService.submit(() -> sendHandler("Log[\"" + myContainerName + " Server not available\"]"));
                System.out.println(hostIP + ":" + hostPort + " :Server not available. Retrying in 5 seconds...");
                // Wait for 5 seconds before retrying
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        // send master that we are finished!
    }
}
