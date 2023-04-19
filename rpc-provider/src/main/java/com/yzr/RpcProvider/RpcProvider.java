package com.yzr.RpcProvider;

import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.protocol.HttpServer;
import com.yzr.RpcPlatform.register.LocalRegister;
import com.yzr.RpcProvider.impl.userServiceImpl;

public class RpcProvider {

    public static void main(String[] args) throws Exception{
        LocalRegister.regist(userService.class.getName(), userServiceImpl.class);

        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost", 9999);
    }


}
