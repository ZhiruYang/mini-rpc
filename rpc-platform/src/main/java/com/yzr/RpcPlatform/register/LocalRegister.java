package com.yzr.RpcPlatform.register;

import java.util.concurrent.ConcurrentHashMap;

public class LocalRegister {
    private static ConcurrentHashMap<String, Class> map = new ConcurrentHashMap<>();
    public static void regist(String interfaceName, Class implClass){
        map.put(interfaceName, implClass);
    }
    public static Class get(String interfaceName){
        return map.get(interfaceName);
    }
}
