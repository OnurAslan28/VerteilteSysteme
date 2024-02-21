package org.vsp.service;

import io.grpc.stub.StreamObserver;
import org.vsp.mechatroniker.server.DockerRequestHandler;
import org.vsp.proto.Mechatroniker;
import org.vsp.proto.MechatronikerServiceGrpc;

/**
 * Server side, das die ist implemtierung von der methode was die macht
 */
public class MechatronikerImpl extends MechatronikerServiceGrpc.MechatronikerServiceImplBase {

    @Override
    public void initContainers(Mechatroniker.InitContainersRequest req, StreamObserver<Mechatroniker.InitContainersResponse> responseStreamObserver){
        DockerRequestHandler.initRequest(req.getAmountOfContainers(), req.getTaskListList());
        responseStreamObserver.onNext(Mechatroniker.InitContainersResponse.newBuilder().setSuccess(DockerRequestHandler.awaitInit()).build());
        responseStreamObserver.onCompleted();
    }

    @Override
    public void start(Mechatroniker.StartRequest req, StreamObserver<Mechatroniker.StartResponse> responseStreamObserver){
        DockerRequestHandler.startRequest();
        responseStreamObserver.onNext(Mechatroniker.StartResponse.newBuilder().setSuccess(DockerRequestHandler.awaitStart()).build());
        responseStreamObserver.onCompleted();

    }


}
