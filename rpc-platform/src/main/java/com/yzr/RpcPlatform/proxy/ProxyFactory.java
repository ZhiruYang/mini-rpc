package com.yzr.RpcPlatform.proxy;

import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.common.Invocation;
import com.yzr.RpcPlatform.protocol.miniClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T> T getProxy(Class interfaceClass){
        Object newProxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Invocation invocation = new Invocation(interfaceClass.getName(),
                            method.getName(), method.getParameterTypes(), args);

                        miniClient client = new miniClient();
                        client.send(invocation);
                        return null;
                    }
                });
        return (T) newProxyInstance;
    }
}
