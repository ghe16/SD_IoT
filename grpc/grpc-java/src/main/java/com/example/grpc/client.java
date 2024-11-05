package com.example.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class client {
    public static void main (String[] args ){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                        .usePlaintext()    
                        .build();

        MyServiceGrpc.MyServiceBlockingStub stub = MyServiceGrpc.newBlockingStub(channel);
        MyRequest request = MyRequest.newBuilder()
                    .setName("Gustavo")
                    .build();

        MyResponse response = stub.myMethod(request);
        System.out.println("The server replies :"+response.getMessage());
        channel.shutdown();

    }
    
}
