package com.zackma.provider.service;


/**
 * 公共接口：生产者一端和消费者一端必须完全一样
 */
public interface HelloServiceI {
    String say(String words);
}
