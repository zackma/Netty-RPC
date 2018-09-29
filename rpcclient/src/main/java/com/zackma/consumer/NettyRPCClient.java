package com.zackma.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.concurrent.*;

public class NettyRPCClient {

    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static ClientHandler client;

    public Object createProxy(final Class<?> serviceClass, final String serviceName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if(client == null){
                            initClient();
                        }
                        client.setParam(serviceName + args[0]);
                        return executor.submit(client).get();
                    }
                });
    }

    /**
     * 初始化客户端
     */
    public static void initClient(){
        client = new ClientHandler();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.TCP_NODELAY,true)
        .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                channelPipeline.addLast(new StringDecoder());
                channelPipeline.addLast(new StringEncoder());
                channelPipeline.addLast(client);
            }
        });
        try{
            bootstrap.connect("localhost",8089).sync();
            System.out.println("Netty RPC Client Started..."+new Date());
        }catch (Exception e){
            throw new RuntimeException("RPC client start Exception");
        }
    }
}
