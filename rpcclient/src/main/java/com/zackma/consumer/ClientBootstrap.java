package com.zackma.consumer;

import com.zackma.consumer.service.HelloServiceI;

import java.util.Date;

public class ClientBootstrap {

    //根据服务端约定的服务接口类路径完整地址进行请求以便服务端利用反射还原接口类和对应实现类，然后完成方法调用
    public static final String serviceName = "com.zackma.provider.service.HelloServiceI#say#";

    public static void main(String[] args){
        NettyRPCClient rpcClient = new NettyRPCClient();
        // 创建一个代理对象
        HelloServiceI service = (HelloServiceI) rpcClient.createProxy(HelloServiceI.class, serviceName);
        for(; ; ){
            try{
                Thread.sleep(1000);
                System.out.println(service.say("that is Netty RPC taste --"+new Date()));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
