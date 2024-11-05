package com.example.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;




public class server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server myserver =  ServerBuilder.forPort(50051)
                            .addService(new MyServiceImpl())
                            .build()
                            .start();
        System.out.println("Server Started, listening in port " + myserver.getPort());
        myserver.awaitTermination();
    }

   static class MyServiceImpl extends MyServiceGrpc.MyServiceImplBase{
    @Override
    public void myMethod (MyRequest request, StreamObserver<MyResponse> responObserver){
        String name = request.getName();

        String message = "Hi, "+ name + "!";
        
        MyResponse response = MyResponse.newBuilder()
                    .setMessage(message)
                    .build();
        
        responObserver.onNext(response);
        responObserver.onCompleted();

    }
    
   }
}