package com.yzr.RpcPlatform;

import com.alibaba.fastjson2.JSONObject;
import com.yzr.RpcCommon.userService;
import com.yzr.RpcPlatform.common.HessianUtil;
import com.yzr.RpcPlatform.common.Invocation;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;

import static com.yzr.RpcPlatform.common.HessianUtil.deserialize;

class RpcPlatformApplicationTests {
    @Test
    void hs(){
        Invocation invocation = new Invocation(userService.class.getName(),
                "Hi", new Class[]{String.class}, new Object[]{"ddr"});
        byte[] data = HessianUtil.serialize(invocation);
        Invocation inv = (Invocation) HessianUtil.deserialize(data);
        System.out.println(inv);
//        Person person = new Person(3, "ddr", new Date());
//        byte[] data = HessianUtil.serialize(person);
//        Person p1 = (Person) HessianUtil.deserialize(data);
//        System.out.println(p1.name);
    }

    @Test
    void contextLoads() {
        Person person = new Person(3, "ddr", new Date());
        System.out.println(person.name);
        String json = JSONObject.toJSONString(person);
        System.out.println("serialize json " + json);

        Person p = JSONObject.parseObject(json, Person.class);
        System.out.println(p.name);

    }
    @Test
    void invoc(){
        Invocation invocation = new Invocation(userService.class.getName(),
                "Hi", new Class[]{String.class}, new Object[]{"ddr"});
        String json = JSONObject.toJSONString(invocation);
        System.out.println(json);

        Invocation inv = JSONObject.parseObject(json, Invocation.class);
        System.out.println(inv.getMethodName());
    }


    @Data
    class Person implements Serializable {
        int age;
        String name;
        Date birthday;
        public Person(int age, String name, Date birthday){
            this.age = age;
            this.name = name;
            this.birthday = birthday;
        }
    }

}
