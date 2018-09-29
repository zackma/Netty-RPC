package com.zackma.provider.service.impl;

import com.zackma.provider.service.HelloServiceI;

public class HelloServiceImpl implements HelloServiceI {
    @Override
    public String say(String words) {
        return words==null?"Hello, there, message from remote provider ~":"Hello, "+words;
    }
}
