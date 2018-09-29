package com.zackma.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

public class NettyRPCServer {

    private static String host = "localhost";
    private static int port = 58080;
    private ServerBootstrap server;
    private NioEventLoopGroup nioEventLoopGroup;
    private static NettyRPCServer me = new NettyRPCServer();

    public NettyRPCServer(){}

    private ServerBootstrap initServer(){
        server = new ServerBootstrap();
        nioEventLoopGroup = new NioEventLoopGroup();
        server.group(nioEventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new ServerHandler());
            }
        });
        return server;
    }

    public static void run(){
        try{
            me.initServer().bind(host, port).sync();
            System.out.println("Netty RPC Server Started..."+new Date());
        }catch (Exception e){
            throw new RuntimeException("Netty start Exception");
        }
    }

    public static void run(String host, int port){
        try{
            me.initServer().bind(host, port).sync();
            System.out.println("Netty RPC Server Started..."+new Date());
        }catch (Exception e){
            throw new RuntimeException("Netty start Exception");
        }
    }

}
