package com.zackma.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 生产者根据消费者提供的Service参数信息，
     * 把完整接口路径利用反射还原成生产者一端
     * 接口类，并按路径规则找到接口实现类后执行
     * 请求方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String result = "";
        if (msg.toString().contains("#")){
            String[] args = msg.toString().split("#");
            try{
                Method[] methods = Class.forName(args[0]).getDeclaredMethods();
                for(Method method:methods){
                    if (method.getName().equals(args[1])){
                        String implPkgName = Class.forName(args[0]).getPackage().getName()+".impl";
                        String interfaceName = (args[0].split("\\."))[args[0].split("\\.").length-1];
                        result = method.invoke(Class.forName(implPkgName+"."+interfaceName+"mpl").newInstance(),args[2]).toString();
                    }
                }
                ctx.writeAndFlush(result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
