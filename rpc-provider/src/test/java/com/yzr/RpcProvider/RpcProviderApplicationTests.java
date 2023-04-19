package com.yzr.RpcProvider;

import com.yzr.RpcPlatform.register.LocalRegister;
import com.yzr.RpcProvider.impl.userServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;

@SpringBootTest
class RpcProviderApplicationTests {

    @Test
    void contextLoads() {
        Class classImpl = userServiceImpl.class;
//        Method method = classImpl.getMethod("Hi", [String.class])
    }

}
