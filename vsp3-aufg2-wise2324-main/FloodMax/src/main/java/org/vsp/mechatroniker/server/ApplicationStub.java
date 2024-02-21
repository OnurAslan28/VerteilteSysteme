package org.vsp.mechatroniker.server;

import io.grpc.BindableService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.vsp.proto.Chef;
import org.vsp.proto.ChefServiceGrpc;
import java.io.IOException;

public class ApplicationStub {
    private final int port;
    private final BindableService service;
    private ChefServiceGrpc.ChefServiceBlockingStub stub;

    public ApplicationStub(int port, ManagedChannel channel, BindableService service){
        this.port = port;
        this.service = service;

        stub = ChefServiceGrpc.newBlockingStub(channel);
    }
    public void startStub() {
        try {
            Server server = ServerBuilder.forPort(port).addService(service).build();
            server.start();
            stub.addLog(Chef.LoggerRequest.newBuilder().setLog("Mecha started at " + server.getPort()).build());
            server.awaitTermination();
        } catch (IOException e) {
            stub.addLog(Chef.LoggerRequest.newBuilder().setLog("Mecha Error: " + e).build());
        } catch (InterruptedException e) {
            stub.addLog(Chef.LoggerRequest.newBuilder().setLog("Mecha Error: " + e).build());
        }
    }
    public void addLog(String log){
        stub.addLog(Chef.LoggerRequest.newBuilder().setLog(log).build());
    }

    public void finished(){
        stub.finished(Chef.FinishedRequest.newBuilder().build());
    }
}
