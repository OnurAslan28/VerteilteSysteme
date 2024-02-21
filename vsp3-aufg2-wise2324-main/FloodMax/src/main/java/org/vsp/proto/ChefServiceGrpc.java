package org.vsp.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: chef.proto")
public final class ChefServiceGrpc {

  private ChefServiceGrpc() {}

  public static final String SERVICE_NAME = "ChefService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.vsp.proto.Chef.LoggerRequest,
      org.vsp.proto.Chef.LoggerResponse> getAddLogMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "addLog",
      requestType = org.vsp.proto.Chef.LoggerRequest.class,
      responseType = org.vsp.proto.Chef.LoggerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vsp.proto.Chef.LoggerRequest,
      org.vsp.proto.Chef.LoggerResponse> getAddLogMethod() {
    io.grpc.MethodDescriptor<org.vsp.proto.Chef.LoggerRequest, org.vsp.proto.Chef.LoggerResponse> getAddLogMethod;
    if ((getAddLogMethod = ChefServiceGrpc.getAddLogMethod) == null) {
      synchronized (ChefServiceGrpc.class) {
        if ((getAddLogMethod = ChefServiceGrpc.getAddLogMethod) == null) {
          ChefServiceGrpc.getAddLogMethod = getAddLogMethod = 
              io.grpc.MethodDescriptor.<org.vsp.proto.Chef.LoggerRequest, org.vsp.proto.Chef.LoggerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ChefService", "addLog"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Chef.LoggerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Chef.LoggerResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ChefServiceMethodDescriptorSupplier("addLog"))
                  .build();
          }
        }
     }
     return getAddLogMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.vsp.proto.Chef.FinishedRequest,
      org.vsp.proto.Chef.FinishedResponse> getFinishedMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "finished",
      requestType = org.vsp.proto.Chef.FinishedRequest.class,
      responseType = org.vsp.proto.Chef.FinishedResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vsp.proto.Chef.FinishedRequest,
      org.vsp.proto.Chef.FinishedResponse> getFinishedMethod() {
    io.grpc.MethodDescriptor<org.vsp.proto.Chef.FinishedRequest, org.vsp.proto.Chef.FinishedResponse> getFinishedMethod;
    if ((getFinishedMethod = ChefServiceGrpc.getFinishedMethod) == null) {
      synchronized (ChefServiceGrpc.class) {
        if ((getFinishedMethod = ChefServiceGrpc.getFinishedMethod) == null) {
          ChefServiceGrpc.getFinishedMethod = getFinishedMethod = 
              io.grpc.MethodDescriptor.<org.vsp.proto.Chef.FinishedRequest, org.vsp.proto.Chef.FinishedResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "ChefService", "finished"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Chef.FinishedRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Chef.FinishedResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new ChefServiceMethodDescriptorSupplier("finished"))
                  .build();
          }
        }
     }
     return getFinishedMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ChefServiceStub newStub(io.grpc.Channel channel) {
    return new ChefServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ChefServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ChefServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ChefServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ChefServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class ChefServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void addLog(org.vsp.proto.Chef.LoggerRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Chef.LoggerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAddLogMethod(), responseObserver);
    }

    /**
     */
    public void finished(org.vsp.proto.Chef.FinishedRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Chef.FinishedResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFinishedMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAddLogMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vsp.proto.Chef.LoggerRequest,
                org.vsp.proto.Chef.LoggerResponse>(
                  this, METHODID_ADD_LOG)))
          .addMethod(
            getFinishedMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vsp.proto.Chef.FinishedRequest,
                org.vsp.proto.Chef.FinishedResponse>(
                  this, METHODID_FINISHED)))
          .build();
    }
  }

  /**
   */
  public static final class ChefServiceStub extends io.grpc.stub.AbstractStub<ChefServiceStub> {
    private ChefServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChefServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChefServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChefServiceStub(channel, callOptions);
    }

    /**
     */
    public void addLog(org.vsp.proto.Chef.LoggerRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Chef.LoggerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAddLogMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void finished(org.vsp.proto.Chef.FinishedRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Chef.FinishedResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFinishedMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ChefServiceBlockingStub extends io.grpc.stub.AbstractStub<ChefServiceBlockingStub> {
    private ChefServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChefServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChefServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChefServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.vsp.proto.Chef.LoggerResponse addLog(org.vsp.proto.Chef.LoggerRequest request) {
      return blockingUnaryCall(
          getChannel(), getAddLogMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.vsp.proto.Chef.FinishedResponse finished(org.vsp.proto.Chef.FinishedRequest request) {
      return blockingUnaryCall(
          getChannel(), getFinishedMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ChefServiceFutureStub extends io.grpc.stub.AbstractStub<ChefServiceFutureStub> {
    private ChefServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ChefServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ChefServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ChefServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vsp.proto.Chef.LoggerResponse> addLog(
        org.vsp.proto.Chef.LoggerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAddLogMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vsp.proto.Chef.FinishedResponse> finished(
        org.vsp.proto.Chef.FinishedRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFinishedMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_LOG = 0;
  private static final int METHODID_FINISHED = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ChefServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ChefServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_ADD_LOG:
          serviceImpl.addLog((org.vsp.proto.Chef.LoggerRequest) request,
              (io.grpc.stub.StreamObserver<org.vsp.proto.Chef.LoggerResponse>) responseObserver);
          break;
        case METHODID_FINISHED:
          serviceImpl.finished((org.vsp.proto.Chef.FinishedRequest) request,
              (io.grpc.stub.StreamObserver<org.vsp.proto.Chef.FinishedResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ChefServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ChefServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.vsp.proto.Chef.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ChefService");
    }
  }

  private static final class ChefServiceFileDescriptorSupplier
      extends ChefServiceBaseDescriptorSupplier {
    ChefServiceFileDescriptorSupplier() {}
  }

  private static final class ChefServiceMethodDescriptorSupplier
      extends ChefServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ChefServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ChefServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ChefServiceFileDescriptorSupplier())
              .addMethod(getAddLogMethod())
              .addMethod(getFinishedMethod())
              .build();
        }
      }
    }
    return result;
  }
}
