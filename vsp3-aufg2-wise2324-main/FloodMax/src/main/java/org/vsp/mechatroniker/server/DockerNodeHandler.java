package org.vsp.mechatroniker.server;

import org.vsp.mechatroniker.floodmax.NodeInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DockerNodeHandler implements Runnable {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static List<NodeInfo> nodeInfoList;
    private static int maxContainers = 0;
    private static ApplicationStub stub;
    // key = mechatroniker, value = highest prio send
    private static Map<Integer, Integer> finishedMechatroniker = new HashMap<>();
    private static CompletableFuture<Boolean> finishedDoneFuture = new CompletableFuture<>();
    private List<String> listOfContainers = new ArrayList<>(5);

    public DockerNodeHandler(List<String> listOfContainers) {
        this.listOfContainers = listOfContainers;
    }

    public static void setMax(int max) {
        maxContainers = max;
    }

    public static void setStub(ApplicationStub stub) {
        DockerNodeHandler.stub = stub;
    }

    public static boolean awaitFinish() {
        try {
            return finishedDoneFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method will handle client requests
     * @param clientSocket
     */
    private static void handleClient(Socket clientSocket) {
        try {
            stub.addLog("Mechatroniker: Container connected: " + clientSocket.getInetAddress() + " ");
            //lock needs to be implemetned hier!!
            // Get the input stream from the client socket
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the data sent by the client
            String clientData = reader.readLine();

            String regex = "([a-zA-Z]+)\\[([ a-zA-Z0-9,:\\-\\\"]*)\\]";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(clientData);

            while (matcher.find()) {
                if (matcher.group(1).equals("Fin")) {
                    int mechanicNumber = getMechaniker(clientData);
                    handleFin(matcher.group(2), mechanicNumber);
                } else if (matcher.group(1).equals("Log")) {
                    handleLog(matcher.group(2));
                } else {
                    stub.addLog("Mechatroniker: Message from Container was not correct. Something went wrong!");
                }
            }
            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return just the number of the Mechatroniker that has send a message
     * @param clientData
     * @return number of mechatroniker
     */
    private static int getMechaniker(String clientData) {

        // Your regular expression
        String regex = "(Mechatroniker)-([0-9]+)";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(clientData);

        // Check if the regex matches
        if (matcher.find()) {
            // Get the matching result
            int result = Integer.parseInt(matcher.group(2));

            // Print the result
//            System.out.println(result);
            return result;
        } else {
            return 0;
        }
    }

    /**
     * Add log to Stub
     * @param log log text
     */
    private static void handleLog(String log) {
        stub.addLog(log);
    }

    /**
     * Handle a Finish request from the dockercontainer
     * @param fin the fin request text, contains the tasks
     * @param mechanic the mechatroniker number
     */
    private static void handleFin(String fin, int mechanic) {

        // Parse the input fin and extract relevant information
        String[] finParts = fin.split(",");
        // get the prio here
        int priority = 0;
        for (String part : finParts) {

            if (part.matches("\\d+")) {
                priority = Integer.parseInt(part);
            }
        }
        if (!finishedMechatroniker.containsKey(mechanic) || finishedMechatroniker.get(mechanic) < priority) {
            finishedMechatroniker.put(mechanic, priority);
        }
        // check if max reached
        if (finishedMechatroniker.size() == maxContainers) {
            // if reached, has everyone the same number?
            // Get the first value in the map
            Integer firstValue = finishedMechatroniker.values().iterator().next();

            // Check if all values in the map are equal to the first value
            boolean allValuesEqual = finishedMechatroniker.values().stream().allMatch(value -> value.equals(firstValue));

            if (allValuesEqual) {
                // All keys have the same value, call doSomething()
//                System.out.println(finishedMechatroniker);
                stub.addLog("All Containers have finished flood and came to an consensus for task: " + fin);
                maxContainers++;
                stub.finished();
                finishedDoneFuture.complete(true);
            } else {
//                System.out.println(finishedMechatroniker);
            }
        }


    }

    public static void setNodeInfoList(List<NodeInfo> nodeInfoList) {
        DockerNodeHandler.nodeInfoList = nodeInfoList;

    }

    @Override
    public void run() {
        int port = 8081;
        // start server on thread pool
        executorService.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                stub.addLog("Mechatroniker: Server listening on port " + port + "...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // Start the Flooding
        executorService.submit(() -> {
            DockerRequestHandler.awaitStart();
            NodeInfo nodeInfo = chooseRandomContainer(nodeInfoList);
            stub.addLog("Mechatroniker: Will start the Flooding from Container-Node:" + nodeInfo);
            sendFlooding(nodeInfo, "Flood[]");
        });


    }

    private void sendFlooding(NodeInfo nodeInfo, String action) {
        String addr = "localhost"; // docker container address
        while (true) {
            try (Socket socket = new Socket(addr, nodeInfo.getPort())) {
                OutputStream outputStream = socket.getOutputStream();

                // Wrap the output stream in a PrintWriter for convenient writing of text
                PrintWriter out = new PrintWriter(outputStream, true);
                stub.addLog("Mechatroniker: Now sending the " + action);
                out.println(action);
                out.close();
                // Close the socket when done
                // send fin() to master
                break;
            } catch (IOException e) {
                stub.addLog("Mechatroniker: " + addr + " :Server not available. Retrying in 5 seconds...");
                // Wait for 5 seconds before retrying
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    private NodeInfo chooseRandomContainer(List<NodeInfo> nodeInfoList) {
        // Filter nodes with non-empty taskList
        List<NodeInfo> nonEmptyTaskListNodes = nodeInfoList.stream()
                .filter(node -> !node.getTaskList().isEmpty())
                .toList();

        // Check if there are nodes with non-empty taskList
        if (nonEmptyTaskListNodes.isEmpty()) {
            return null;  // No nodes with non-empty taskList found
        }

        // Randomly select a node from the filtered list
        Random random = new Random();
        int randomIndex = random.nextInt(nonEmptyTaskListNodes.size());
        return nonEmptyTaskListNodes.get(randomIndex);
    }
}
