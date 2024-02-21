package org.vsp.mechatroniker.server;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.vsp.mechatroniker.DockerActionImpl;
import org.vsp.mechatroniker.IDockerActions;
import org.vsp.mechatroniker.floodmax.NodeInfo;
import org.vsp.proto.Mechatroniker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DockerRequestHandler implements Runnable {

    private static final Object lock = new Object();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static boolean initialized = false;
    private static int amountOfContainers;
    private static List<Mechatroniker.Task> taskListList;
    private static CompletableFuture<Boolean> initializationDoneFuture = new CompletableFuture<>();
    private static CompletableFuture<Boolean> startFuture = new CompletableFuture<>();
    private static CompletableFuture<Boolean> startDoneFuture = new CompletableFuture<>();
    private static CompletableFuture<Boolean> initializationFuture = new CompletableFuture<>();
    private static ApplicationStub applicationStub;
    private final String hostadress;
    private final int hostport;
    List<NodeInfo> nodeInfoList = new ArrayList<>();
    private List<String> listOfContainers;
    private IDockerActions dockerConnection;


    public DockerRequestHandler(String hostadress, int hostport, ApplicationStub applicationStub) {
        this.hostadress = hostadress;
        this.hostport = hostport;
        DockerRequestHandler.applicationStub = applicationStub;
    }

    public static void initRequest(int amountOfContainers2, List<Mechatroniker.Task> taskListList2) {
        applicationStub.addLog("Mechatroniker: Handle init request from GRPC");
        amountOfContainers = amountOfContainers2;
        taskListList = taskListList2;
        initializationFuture.complete(true);
    }

    public static void startRequest() {
        applicationStub.addLog("Mechatroniker: Handle start request from GRPC");
        startFuture.complete(true);
    }

    private static Map<String, List<Mechatroniker.Task>> assignTasks(List<String> containerNames, List<Mechatroniker.Task> tasks) {
        applicationStub.addLog("Mechatroniker: Assigning Task to the Containers");
        Map<String, List<Mechatroniker.Task>> taskAssignment = new HashMap<>();
        tasks = new ArrayList<>(tasks); // Create a modifiable list

        // Initialize each person's task list
        for (String person : containerNames) {
            taskAssignment.put(person, new ArrayList<>());
        }

        // Assign at least one task to each person
        for (String person : containerNames) {
            Mechatroniker.Task task = getRandomTask(tasks);
            taskAssignment.get(person).add(task);
            tasks.remove(task);
            if (tasks.isEmpty()) break;
        }

        // Assign the remaining tasks
        Random random = new Random();
        while (!tasks.isEmpty()) {
            String person = containerNames.get(random.nextInt(containerNames.size()));
            Mechatroniker.Task task = getRandomTask(tasks);
            taskAssignment.get(person).add(task);
            tasks.remove(task);
        }

        return taskAssignment;
    }

    private static Mechatroniker.Task getRandomTask(List<Mechatroniker.Task> tasks) {
        Random random = new Random();
        return tasks.get(random.nextInt(tasks.size()));
    }

    public static boolean awaitInit() {
        try {
            return initializationDoneFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean awaitStart() {
        try {

            return startDoneFuture.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        executorService.submit(() -> {
            boolean ready = false;
            while (!ready) {
                try {
                    ready = initializationFuture.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }
            applicationStub.addLog("Mechatroniker: Will now start init the Containers");
            initialzeContainers();
        });

        executorService.submit(() -> {
            boolean ready = false;
            while (!ready) {
                try {
                    ready = startFuture.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }
            applicationStub.addLog("Mechatroniker: Will now start the Containers");
            startContainers();
        });
        DockerNodeHandler dockerNodeHandler = new DockerNodeHandler(listOfContainers);
        Thread dockerNodeHandlerThread = new Thread(dockerNodeHandler);
        dockerNodeHandlerThread.start();
    }

    private void startContainers() {
//        System.out.println(listOfContainers);
        DockerNodeHandler.setMax(listOfContainers.size());
        for (String containerID : listOfContainers) {
            applicationStub.addLog("Mechatroniker: Starting this Container: " + containerID);
            dockerConnection.startContaienr(containerID.toLowerCase()); // lowerCase is important here! else it get stuck at starting...
        }
        startDoneFuture.complete(true);
        applicationStub.addLog("Mechatroniker: Starting is Done, every container is running now");
    }

    private void initialzeContainers() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("tcp://localhost:2375")
                .build();
        dockerConnection = new DockerActionImpl(DockerClientBuilder.getInstance(config).build());

        listOfContainers = makeMechatronikers();
        Map<String, List<Mechatroniker.Task>> containersWithTasks = assignTasks(listOfContainers, taskListList);

        Map<String, List<String>> connections = generateRandomContainerGraph(listOfContainers);


        // build image for each container
        // map of cont name and image path
        applicationStub.addLog("Mechatroniker: Build the images");
        Map<String, String> buildImageAndContainerMap = buildDockerfiles(containersWithTasks, hostadress, hostport, connections);
        List<String> startContainerID = new ArrayList<>();

        applicationStub.addLog("Mechatroniker: Build the network connection between the containers");
        for (Map.Entry<String, String> entry : buildImageAndContainerMap.entrySet()) {
            String imageID = dockerConnection.buildDockerfileImage(entry.getValue()).awaitImageId();
            int exposedPort = nodeInfoList.stream().filter(x -> x.getContainerName().equals(entry.getKey())).toList().get(0).getPort();
            startContainerID.add(dockerConnection.createContainer(imageID, entry.getKey().toLowerCase(), exposedPort));
        }
//        listOfContainers = startContainerID;
        List<CreateNetworkResponse> networkResponseList = connectContainers(connections);
        applicationStub.addLog("Mechatroniker: Init is done!");

        executorService.submit(() -> {
            DockerNodeHandler.awaitFinish();
            applicationStub.addLog("Mechatroniker: Will now shutdown everything in docker");
            // stop container, delete containers // delete image // delete networks
            for (String container : listOfContainers) {
                dockerConnection.stopContainer(container.toLowerCase());
                dockerConnection.deleteContainer(container.toLowerCase());
            }
            for (Map.Entry<String, String> entry : buildImageAndContainerMap.entrySet()) {
                dockerConnection.removeImage(entry.getValue());
            }
            for (CreateNetworkResponse networkResponse : networkResponseList) {
                dockerConnection.removeNetwork(networkResponse.getId());
            }

        });
        initializationDoneFuture.complete(true);


    }

    private List<CreateNetworkResponse> connectContainers(Map<String, List<String>> connections) {
        List<CreateNetworkResponse> tmp = new ArrayList<>();
        for (String containerName : connections.keySet()) {
            for (String connectWithContainer : connections.get(containerName)) {
                CreateNetworkResponse network = dockerConnection.createNetwork(containerName + "--" + connectWithContainer);
                tmp.add(network);
                dockerConnection.connectTwoContainersWithNetwork(network.getId(), containerName.toLowerCase(), connectWithContainer.toLowerCase());
            }
        }
        applicationStub.addLog("Mechatroniker: Connecting done!");
        return null;
    }

    private Map<String, List<String>> generateRandomContainerGraph(List<String> listOfContainers) {
        Map<String, List<String>> graph = new HashMap<>();

        for (String name : listOfContainers) {
            graph.put(name, new ArrayList<>());
        }

        // Creating bidirectional connections to ensure every node is reachable
        Random random = new Random();
        for (String name : listOfContainers) {
            int numConnections = random.nextInt(listOfContainers.size() / 2) + 1; // Adjust the range as needed

            for (int i = 0; i < numConnections; i++) {
                String connectedNode = listOfContainers.get(random.nextInt(listOfContainers.size()));
                if (!connectedNode.equals(name) && !graph.get(name).contains(connectedNode)) {
                    graph.get(name).add(connectedNode);
                    graph.get(connectedNode).add(name); // Bidirectional connection
                } else {
                    i--; // Retry if the connection already exists
                }
            }
        }

        return graph;
    }

    private Map<String, String> buildDockerfiles(Map<String, List<Mechatroniker.Task>> containersWithTasks, String hostadress, int hostport, Map<String, List<String>> connections) {
        applicationStub.addLog("Mechatroniker: Build the dockerfiles");
        //generate dockerfile for each container
        Map<String, String> result = new HashMap<>();
        int port = 50000; // 50000 because these are mostly free
        hostport++;
        for (Map.Entry<String, List<Mechatroniker.Task>> entry : containersWithTasks.entrySet()) {
            String task = cleanUpValue(entry.getValue()).replaceAll(" ", "");
//            System.out.println(task);
            String connectionsString = connections.get(entry.getKey()).toString().replaceAll(" ", "");

            String dockerfileContent = "FROM openjdk:11\n" + "WORKDIR /usr/src/app\n" + "COPY . .\n" + "EXPOSE " + port + "\n" + "CMD [\"java\", \"FloodNode.java\", \"" + task + "\", \"" + hostadress + "\", \"" + hostport + "\", \"" + entry.getKey() + "\", \"" + connectionsString + "\", \"" + port + "\"]";

            String fileName = "src\\main\\java\\org\\vsp\\mechatroniker\\dockerfiletesting\\Dockerfile_" + entry.getKey();
            result.put(entry.getKey(), fileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write(dockerfileContent);
                nodeInfoList.add(new NodeInfo(entry.getKey(), port++, task));

//                System.out.println("Dockerfile for " + entry.getKey() + " generated successfully: " + fileName);
            } catch (IOException e) {
                applicationStub.addLog("Mechatroniker: Something went wrong while build the dockerfiles.");
                e.printStackTrace();
            }
            // build image for each container with the dockerfile
            // return Map with K = containerName and V = BuildImageResultCallback for the container
        }
        DockerNodeHandler.setNodeInfoList(nodeInfoList);
        applicationStub.addLog("Mechatroniker: Build docker files done");
        return result;
    }

    private String cleanUpValue(List<Mechatroniker.Task> value) {
        String result = "[]";
        if (value.size() != 0) {
//            String[] parts = value.toString().replaceAll("[\\[\\]\"]", "").split("\n");
//            result = Arrays.toString(parts).replace("taskName: ", "").replace("priority: ", "").replace("time: ", "");
            result = value.toString().replace("taskName: ", "(").replace("priority: ", "").replace("time: ", "")
                    .replace("\n", ",").replace(",,", "),").replace("\",]", "\")]").replace(" ", "").replace("\"", "");
        }

        return result;
    }

    private List<String> makeMechatronikers() {
        List<String> containerNames = new ArrayList<>();
        for (int i = 0; i < amountOfContainers; ) {
            containerNames.add("Mechatroniker-" + ++i);
            // generate DockerFiles for every mechatroniker
        }
        return containerNames;
    }
}
