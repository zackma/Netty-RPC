package com.zackma.provider;

public class ServerBootstrap {

    public static void main(String... args){
        NettyRPCServer.run("localhost",8089);
    }
}
