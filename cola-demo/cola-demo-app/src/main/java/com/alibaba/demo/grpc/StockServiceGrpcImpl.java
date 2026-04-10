package com.alibaba.demo.grpc;

import com.alibaba.demo.domain.customer.gateway.StockGateway;
import com.alibaba.demo.domain.stock.Stock;
import com.alibaba.demo.dubbo.grpc.api.DubboStockServiceTriple;
import com.alibaba.demo.dubbo.grpc.api.GetStockRequest;
import com.alibaba.demo.dubbo.grpc.api.StockResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
@Slf4j
public class StockServiceGrpcImpl extends DubboStockServiceTriple.StockServiceImplBase {

    private final StockGateway stockGateway;

    @Override
    public void getStock(GetStockRequest request, StreamObserver<StockResponse> responseObserver) {
        Long id = request.getId();
        Stock stock = stockGateway.getDetailById(id);
        StockResponse stockResponse = StockResponse.newBuilder()
                .setId(stock.getId())
                .setName(stock.getName())
                .setNum(stock.getNum())
                .setDes(stock.getDes())
                .build();
        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();
        log.info("Returning stock info for: " + id);
    }
}
