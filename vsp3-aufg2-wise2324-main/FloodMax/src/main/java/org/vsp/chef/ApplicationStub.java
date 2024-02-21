package org.vsp.chef;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.vsp.chef.logger.FloodLogger;
import org.vsp.chef.schema.Task;
import org.vsp.proto.Mechatroniker;
import org.vsp.proto.MechatronikerServiceGrpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Client Application Stub
 */
public class ApplicationStub {
    private final int port;
    private final BindableService service;
    private MechatronikerServiceGrpc.MechatronikerServiceBlockingStub stub;

    public ApplicationStub(int port, ManagedChannel channel, BindableService service){
        this.port = port;
        this.service = service;

        stub = MechatronikerServiceGrpc.newBlockingStub(channel);
    }

    /**
     * Start the stub for this proccess so this can receive incoming data from Server
     */
    public void startStub() {
        try {
            Server server = ServerBuilder.forPort(port).addService(service).build();
            server.start();
            FloodLogger.addLog("Chef started at " + server.getPort());
            server.awaitTermination();
        } catch (IOException e) {
            FloodLogger.addErrorLog("Chef Error: " + e);
        } catch (InterruptedException e) {
            FloodLogger.addErrorLog("Chef Error: " + e);
        }
    }

    /**
     * Make a request to the server to intizialize the Containers
     * @param amountOfContainers
     * @param taskList List of Tasks
     * @return true if the init was successfull
     */
    public boolean initContainers(int amountOfContainers, List<Task> taskList) {
        FloodLogger.addLog("GRPC send Init Container");
        List<Mechatroniker.Task> tasksList = convertToMechatronikerTask(taskList);
        return stub.initContainers(Mechatroniker.InitContainersRequest.newBuilder().setAmountOfContainers(amountOfContainers).addAllTaskList(tasksList).build()).getSuccess();
    }

    /**
     * Convert to Mechtroniker Task type
     * need to convert our custom Task in schema package to the Task of the generated GRPC Mechatroniker.Task
     * @param taskList
     * @return
     */
    private static List<Mechatroniker.Task> convertToMechatronikerTask(List<Task> taskList) {
        List<Mechatroniker.Task> tmp = new ArrayList<>();

        for (Task task : taskList) {
            tmp.add(Mechatroniker.Task.newBuilder().setTaskName(task.getTaskName()).setPriority(task.getPriority()).setTime(String.valueOf(task.getTime())).build());
        }
        return tmp;
    }

    /**
     * Make a requuest to start the container
     * @return true if the start was successfull
     */
    public boolean startContainers() {
        FloodLogger.addLog("GRPC send start Container");
        return stub.start(Mechatroniker.StartRequest.newBuilder().build()).getSuccess();
    }
}
