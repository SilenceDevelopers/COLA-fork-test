/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.alibaba.demo.dubbo.grpc.api;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.PathResolver;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.ServerService;
import org.apache.dubbo.rpc.TriRpcStatus;
import org.apache.dubbo.rpc.model.MethodDescriptor;
import org.apache.dubbo.rpc.model.ServiceDescriptor;
import org.apache.dubbo.rpc.model.StubMethodDescriptor;
import org.apache.dubbo.rpc.model.StubServiceDescriptor;
import org.apache.dubbo.rpc.service.Destroyable;
import org.apache.dubbo.rpc.stub.BiStreamMethodHandler;
import org.apache.dubbo.rpc.stub.ServerStreamMethodHandler;
import org.apache.dubbo.rpc.stub.StubInvocationUtil;
import org.apache.dubbo.rpc.stub.StubInvoker;
import org.apache.dubbo.rpc.stub.StubMethodHandler;
import org.apache.dubbo.rpc.stub.StubSuppliers;
import org.apache.dubbo.rpc.stub.UnaryStubMethodHandler;

import com.google.protobuf.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.concurrent.CompletableFuture;

public final class DubboStockServiceTriple {

    public static final String SERVICE_NAME = StockService.SERVICE_NAME;

    private static final StubServiceDescriptor serviceDescriptor = new StubServiceDescriptor(SERVICE_NAME,StockService.class);

    static {
        org.apache.dubbo.rpc.protocol.tri.service.SchemaDescriptorRegistry.addSchemaDescriptor(SERVICE_NAME,StockServiceProto.getDescriptor());
        StubSuppliers.addSupplier(SERVICE_NAME, DubboStockServiceTriple::newStub);
        StubSuppliers.addSupplier(StockService.JAVA_SERVICE_NAME,  DubboStockServiceTriple::newStub);
        StubSuppliers.addDescriptor(SERVICE_NAME, serviceDescriptor);
        StubSuppliers.addDescriptor(StockService.JAVA_SERVICE_NAME, serviceDescriptor);
    }

    @SuppressWarnings("all")
    public static StockService newStub(Invoker<?> invoker) {
        return new StockServiceStub((Invoker<StockService>)invoker);
    }

    private static final StubMethodDescriptor getStockMethod = new StubMethodDescriptor("GetStock",
    com.alibaba.demo.dubbo.grpc.api.GetStockRequest.class, com.alibaba.demo.dubbo.grpc.api.StockResponse.class, MethodDescriptor.RpcType.UNARY,
    obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(), com.alibaba.demo.dubbo.grpc.api.GetStockRequest::parseFrom,
    com.alibaba.demo.dubbo.grpc.api.StockResponse::parseFrom);

    private static final StubMethodDescriptor getStockAsyncMethod = new StubMethodDescriptor("GetStock",
    com.alibaba.demo.dubbo.grpc.api.GetStockRequest.class, java.util.concurrent.CompletableFuture.class, MethodDescriptor.RpcType.UNARY,
    obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(), com.alibaba.demo.dubbo.grpc.api.GetStockRequest::parseFrom,
    com.alibaba.demo.dubbo.grpc.api.StockResponse::parseFrom);

    private static final StubMethodDescriptor getStockProxyAsyncMethod = new StubMethodDescriptor("GetStockAsync",
    com.alibaba.demo.dubbo.grpc.api.GetStockRequest.class, com.alibaba.demo.dubbo.grpc.api.StockResponse.class, MethodDescriptor.RpcType.UNARY,
    obj -> ((Message) obj).toByteArray(), obj -> ((Message) obj).toByteArray(), com.alibaba.demo.dubbo.grpc.api.GetStockRequest::parseFrom,
    com.alibaba.demo.dubbo.grpc.api.StockResponse::parseFrom);




    static{
        serviceDescriptor.addMethod(getStockMethod);
        serviceDescriptor.addMethod(getStockProxyAsyncMethod);
    }

    public static class StockServiceStub implements StockService, Destroyable {
        private final Invoker<StockService> invoker;

        public StockServiceStub(Invoker<StockService> invoker) {
            this.invoker = invoker;
        }

        @Override
        public void $destroy() {
              invoker.destroy();
         }

        @Override
        public com.alibaba.demo.dubbo.grpc.api.StockResponse getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request){
            return StubInvocationUtil.unaryCall(invoker, getStockMethod, request);
        }

        public CompletableFuture<com.alibaba.demo.dubbo.grpc.api.StockResponse> getStockAsync(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request){
            return StubInvocationUtil.unaryCall(invoker, getStockAsyncMethod, request);
        }

        public void getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request, StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse> responseObserver){
            StubInvocationUtil.unaryCall(invoker, getStockMethod , request, responseObserver);
        }



    }

    public static abstract class StockServiceImplBase implements StockService, ServerService<StockService> {

        private <T, R> BiConsumer<T, StreamObserver<R>> syncToAsync(java.util.function.Function<T, R> syncFun) {
            return new BiConsumer<T, StreamObserver<R>>() {
                @Override
                public void accept(T t, StreamObserver<R> observer) {
                    try {
                        R ret = syncFun.apply(t);
                        observer.onNext(ret);
                        observer.onCompleted();
                    } catch (Throwable e) {
                        observer.onError(e);
                    }
                }
            };
        }

        @Override
        public CompletableFuture<com.alibaba.demo.dubbo.grpc.api.StockResponse> getStockAsync(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request){
                return CompletableFuture.completedFuture(getStock(request));
        }

        /**
        * This server stream type unary method is <b>only</b> used for generated stub to support async unary method.
        * It will not be called if you are NOT using Dubbo3 generated triple stub and <b>DO NOT</b> implement this method.
        */
        public void getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request, StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse> responseObserver){
            getStockAsync(request).whenComplete((r, t) -> {
                if (t != null) {
                    responseObserver.onError(t);
                } else {
                    responseObserver.onNext(r);
                    responseObserver.onCompleted();
                }
            });
        }

        @Override
        public final Invoker<StockService> getInvoker(URL url) {
            PathResolver pathResolver = url.getOrDefaultFrameworkModel()
            .getExtensionLoader(PathResolver.class)
            .getDefaultExtension();
            Map<String,StubMethodHandler<?, ?>> handlers = new HashMap<>();

            pathResolver.addNativeStub( "/" + SERVICE_NAME + "/GetStock");
            pathResolver.addNativeStub( "/" + SERVICE_NAME + "/GetStockAsync");
            // for compatibility
            pathResolver.addNativeStub( "/" + JAVA_SERVICE_NAME + "/GetStock");
            pathResolver.addNativeStub( "/" + JAVA_SERVICE_NAME + "/GetStockAsync");


            BiConsumer<com.alibaba.demo.dubbo.grpc.api.GetStockRequest, StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse>> getStockFunc = this::getStock;
            handlers.put(getStockMethod.getMethodName(), new UnaryStubMethodHandler<>(getStockFunc));
            BiConsumer<com.alibaba.demo.dubbo.grpc.api.GetStockRequest, StreamObserver<com.alibaba.demo.dubbo.grpc.api.StockResponse>> getStockAsyncFunc = syncToAsync(this::getStock);
            handlers.put(getStockProxyAsyncMethod.getMethodName(), new UnaryStubMethodHandler<>(getStockAsyncFunc));




            return new StubInvoker<>(this, url, StockService.class, handlers);
        }


        @Override
        public com.alibaba.demo.dubbo.grpc.api.StockResponse getStock(com.alibaba.demo.dubbo.grpc.api.GetStockRequest request){
            throw unimplementedMethodException(getStockMethod);
        }





        @Override
        public final ServiceDescriptor getServiceDescriptor() {
            return serviceDescriptor;
        }
        private RpcException unimplementedMethodException(StubMethodDescriptor methodDescriptor) {
            return TriRpcStatus.UNIMPLEMENTED.withDescription(String.format("Method %s is unimplemented",
                "/" + serviceDescriptor.getInterfaceName() + "/" + methodDescriptor.getMethodName())).asException();
        }
    }

}
