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
    comments = "Source: mechatroniker.proto")
public final class MechatronikerServiceGrpc {

  private MechatronikerServiceGrpc() {}

  public static final String SERVICE_NAME = "MechatronikerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.InitContainersRequest,
      org.vsp.proto.Mechatroniker.InitContainersResponse> getInitContainersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "initContainers",
      requestType = org.vsp.proto.Mechatroniker.InitContainersRequest.class,
      responseType = org.vsp.proto.Mechatroniker.InitContainersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.InitContainersRequest,
      org.vsp.proto.Mechatroniker.InitContainersResponse> getInitContainersMethod() {
    io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.InitContainersRequest, org.vsp.proto.Mechatroniker.InitContainersResponse> getInitContainersMethod;
    if ((getInitContainersMethod = MechatronikerServiceGrpc.getInitContainersMethod) == null) {
      synchronized (MechatronikerServiceGrpc.class) {
        if ((getInitContainersMethod = MechatronikerServiceGrpc.getInitContainersMethod) == null) {
          MechatronikerServiceGrpc.getInitContainersMethod = getInitContainersMethod = 
              io.grpc.MethodDescriptor.<org.vsp.proto.Mechatroniker.InitContainersRequest, org.vsp.proto.Mechatroniker.InitContainersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "MechatronikerService", "initContainers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Mechatroniker.InitContainersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Mechatroniker.InitContainersResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MechatronikerServiceMethodDescriptorSupplier("initContainers"))
                  .build();
          }
        }
     }
     return getInitContainersMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.StartRequest,
      org.vsp.proto.Mechatroniker.StartResponse> getStartMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "start",
      requestType = org.vsp.proto.Mechatroniker.StartRequest.class,
      responseType = org.vsp.proto.Mechatroniker.StartResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.StartRequest,
      org.vsp.proto.Mechatroniker.StartResponse> getStartMethod() {
    io.grpc.MethodDescriptor<org.vsp.proto.Mechatroniker.StartRequest, org.vsp.proto.Mechatroniker.StartResponse> getStartMethod;
    if ((getStartMethod = MechatronikerServiceGrpc.getStartMethod) == null) {
      synchronized (MechatronikerServiceGrpc.class) {
        if ((getStartMethod = MechatronikerServiceGrpc.getStartMethod) == null) {
          MechatronikerServiceGrpc.getStartMethod = getStartMethod = 
              io.grpc.MethodDescriptor.<org.vsp.proto.Mechatroniker.StartRequest, org.vsp.proto.Mechatroniker.StartResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "MechatronikerService", "start"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Mechatroniker.StartRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.vsp.proto.Mechatroniker.StartResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new MechatronikerServiceMethodDescriptorSupplier("start"))
                  .build();
          }
        }
     }
     return getStartMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MechatronikerServiceStub newStub(io.grpc.Channel channel) {
    return new MechatronikerServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MechatronikerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MechatronikerServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MechatronikerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MechatronikerServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class MechatronikerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void initContainers(org.vsp.proto.Mechatroniker.InitContainersRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.InitContainersResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getInitContainersMethod(), responseObserver);
    }

    /**
     */
    public void start(org.vsp.proto.Mechatroniker.StartRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.StartResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStartMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getInitContainersMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vsp.proto.Mechatroniker.InitContainersRequest,
                org.vsp.proto.Mechatroniker.InitContainersResponse>(
                  this, METHODID_INIT_CONTAINERS)))
          .addMethod(
            getStartMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                org.vsp.proto.Mechatroniker.StartRequest,
                org.vsp.proto.Mechatroniker.StartResponse>(
                  this, METHODID_START)))
          .build();
    }
  }

  /**
   */
  public static final class MechatronikerServiceStub extends io.grpc.stub.AbstractStub<MechatronikerServiceStub> {
    private MechatronikerServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechatronikerServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechatronikerServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechatronikerServiceStub(channel, callOptions);
    }

    /**
     */
    public void initContainers(org.vsp.proto.Mechatroniker.InitContainersRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.InitContainersResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getInitContainersMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void start(org.vsp.proto.Mechatroniker.StartRequest request,
        io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.StartResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStartMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class MechatronikerServiceBlockingStub extends io.grpc.stub.AbstractStub<MechatronikerServiceBlockingStub> {
    private MechatronikerServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechatronikerServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechatronikerServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechatronikerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.vsp.proto.Mechatroniker.InitContainersResponse initContainers(org.vsp.proto.Mechatroniker.InitContainersRequest request) {
      return blockingUnaryCall(
          getChannel(), getInitContainersMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.vsp.proto.Mechatroniker.StartResponse start(org.vsp.proto.Mechatroniker.StartRequest request) {
      return blockingUnaryCall(
          getChannel(), getStartMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class MechatronikerServiceFutureStub extends io.grpc.stub.AbstractStub<MechatronikerServiceFutureStub> {
    private MechatronikerServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MechatronikerServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MechatronikerServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MechatronikerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vsp.proto.Mechatroniker.InitContainersResponse> initContainers(
        org.vsp.proto.Mechatroniker.InitContainersRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getInitContainersMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.vsp.proto.Mechatroniker.StartResponse> start(
        org.vsp.proto.Mechatroniker.StartRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStartMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_INIT_CONTAINERS = 0;
  private static final int METHODID_START = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MechatronikerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(MechatronikerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INIT_CONTAINERS:
          serviceImpl.initContainers((org.vsp.proto.Mechatroniker.InitContainersRequest) request,
              (io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.InitContainersResponse>) responseObserver);
          break;
        case METHODID_START:
          serviceImpl.start((org.vsp.proto.Mechatroniker.StartRequest) request,
              (io.grpc.stub.StreamObserver<org.vsp.proto.Mechatroniker.StartResponse>) responseObserver);
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

  private static abstract class MechatronikerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MechatronikerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.vsp.proto.Mechatroniker.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MechatronikerService");
    }
  }

  private static final class MechatronikerServiceFileDescriptorSupplier
      extends MechatronikerServiceBaseDescriptorSupplier {
    MechatronikerServiceFileDescriptorSupplier() {}
  }

  private static final class MechatronikerServiceMethodDescriptorSupplier
      extends MechatronikerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MechatronikerServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (MechatronikerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MechatronikerServiceFileDescriptorSupplier())
              .addMethod(getInitContainersMethod())
              .addMethod(getStartMethod())
              .build();
        }
      }
    }
    return result;
  }
}
