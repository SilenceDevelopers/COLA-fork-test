package com.alibaba.demo.dubbo.grpc.api;

import io.grpc.MethodDescriptor;
import io.grpc.stub.annotations.RpcMethod;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class StockServiceGrpc {

  private StockServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "StockService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.alibaba.demo.dubbo.grpc.api.GetStockRequest,
      com.alibaba.demo.dubbo.grpc.api.StockResponse> getGetStockMethod;

  @RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetStock",
      requestType = GetStockRequest.class,
      responseType = StockResponse.class,
      methodType = MethodDescriptor.MethodType.UNARY)
  public static MethodDescriptor<GetStockRequest,
      StockResponse> getGetStockMethod() {
    MethodDescriptor<GetStockRequest, StockResponse> getGetStockMethod;
    if ((getGetStockMethod = StockServiceGrpc.getGetStockMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getGetStockMethod = StockServiceGrpc.getGetStockMethod) == null) {
          StockServiceGrpc.getGetStockMethod = getGetStockMethod =
              MethodDescriptor.<GetStockRequest, StockResponse>newBuilder()
              .setType(MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetStock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  GetStockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  StockResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("GetStock"))
              .build();
        }
      }
    }
    return getGetStockMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StockServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceStub>() {
        @java.lang.Override
        public StockServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceStub(channel, callOptions);
        }
      };
    return StockServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static StockServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingV2Stub>() {
        @java.lang.Override
        public StockServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return StockServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StockServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceBlockingStub>() {
        @java.lang.Override
        public StockServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceBlockingStub(channel, callOptions);
        }
      };
    return StockServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StockServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StockServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StockServiceFutureStub>() {
        @java.lang.Override
        public StockServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StockServiceFutureStub(channel, callOptions);
        }
      };
    return StockServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request,
        io.grpc.stub.StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetStockMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StockService.
   */
  public static abstract class StockServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StockServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StockService.
   */
  public static final class StockServiceStub
      extends io.grpc.stub.AbstractAsyncStub<StockServiceStub> {
    private StockServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceStub(channel, callOptions);
    }

    /**
     */
    public void getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request,
        io.grpc.stub.StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetStockMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StockService.
   */
  public static final class StockServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<StockServiceBlockingV2Stub> {
    private StockServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public com.alibaba.demo.dubbo.grpc.api.StockResponse getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetStockMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service StockService.
   */
  public static final class StockServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StockServiceBlockingStub> {
    private StockServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.alibaba.demo.dubbo.grpc.api.StockResponse getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetStockMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StockService.
   */
  public static final class StockServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<StockServiceFutureStub> {
    private StockServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StockServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.alibaba.demo.dubbo.grpc.api.StockResponse> getStock(
        com.alibaba.demo.dubbo.grpc.api.GetStockRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetStockMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_STOCK = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_STOCK:
          serviceImpl.getStock((com.alibaba.demo.dubbo.grpc.api.GetStockRequest) request,
              (io.grpc.stub.StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetStockMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.alibaba.demo.dubbo.grpc.api.GetStockRequest,
              com.alibaba.demo.dubbo.grpc.api.StockResponse>(
                service, METHODID_GET_STOCK)))
        .build();
  }

  private static abstract class StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StockServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.alibaba.demo.dubbo.grpc.api.StockServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StockService");
    }
  }

  private static final class StockServiceFileDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier {
    StockServiceFileDescriptorSupplier() {}
  }

  private static final class StockServiceMethodDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    StockServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (StockServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StockServiceFileDescriptorSupplier())
              .addMethod(getGetStockMethod())
              .build();
        }
      }
    }
    return result;
  }
}
