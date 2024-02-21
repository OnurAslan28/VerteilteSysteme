package org.vsp.service;

import io.grpc.stub.StreamObserver;
import org.vsp.chef.logger.FloodLogger;
import org.vsp.chef.measure.PerformanceMeasure;
import org.vsp.proto.Chef;
import org.vsp.proto.ChefServiceGrpc;
import org.vsp.proto.Mechatroniker;

public class ChefImpl extends ChefServiceGrpc.ChefServiceImplBase {
    @Override
    public void addLog(Chef.LoggerRequest req, StreamObserver<Chef.LoggerResponse> responseStreamObserver){
        FloodLogger.addLog(req.getLog());
        responseStreamObserver.onNext(Chef.LoggerResponse.newBuilder().build());
        responseStreamObserver.onCompleted();
    }

    @Override
    public void finished(Chef.FinishedRequest req, StreamObserver<Chef.FinishedResponse> responseStreamObserver){
        PerformanceMeasure pm = PerformanceMeasure.getInstance();
        pm.stopTimer();
        responseStreamObserver.onNext(Chef.FinishedResponse.newBuilder().build());
        responseStreamObserver.onCompleted();
        FloodLogger.stopLogging();
    }
}
