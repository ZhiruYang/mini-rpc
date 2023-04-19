package com.yzr.RpcProvider.impl;

import com.yzr.RpcCommon.userService;

public class userServiceImpl implements userService {
    @Override
    public String Hi(String name) {
        return "Hii " + name;
    }
}
