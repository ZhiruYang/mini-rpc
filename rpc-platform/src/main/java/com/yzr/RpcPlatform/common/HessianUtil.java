package com.yzr.RpcPlatform.common;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.Cleanup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessianUtil {
    public static <T> byte[] serialize(T javaBean){
        try {
            @Cleanup ByteArrayOutputStream bos = new ByteArrayOutputStream();
            @Cleanup Hessian2Output ho = new Hessian2Output(bos);

            ho.writeObject(javaBean);
            ho.flush();
            return bos.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] serializeData){
        try {
            @Cleanup ByteArrayInputStream bis = new ByteArrayInputStream(serializeData);
            @Cleanup Hessian2Input hi = new Hessian2Input(bis);
            return (T) hi.readObject();
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
