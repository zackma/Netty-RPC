package com.zackma.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext channelHandlerContext;
    private String result;
    private String param;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        channelHandlerContext = ctx;
    }

    /**
     * 收到服务端数据，唤醒等待线程(必须加线程锁！！！！)
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) {
        result = msg.toString();
        notify();
    }

    /**
     * 写出数据，开始等待唤醒(必须加线程锁！！！！)
     */
    @Override
    public synchronized Object call() throws InterruptedException {
        channelHandlerContext.writeAndFlush(param);
        wait();
        return result;
    }

    public void setParam(String param){
        this.param = param;
    }
}
