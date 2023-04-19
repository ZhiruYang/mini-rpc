package com.yzr.RpcConsumer;

import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.proxy.ProxyFactory;
import java.io.IOException;


public class Consumer {
    public static void main(String[] args) throws IOException {


        userService service = ProxyFactory.getProxy(userService.class);
        service.Hi("ff");

    }


}
